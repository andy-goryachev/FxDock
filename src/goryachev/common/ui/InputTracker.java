// Copyright (c) 2006-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.util.Rex;
import java.awt.ItemSelectable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;


// see also UniversalChangeListener - combine?
public abstract class InputTracker 
	implements DocumentListener, ItemListener, PropertyChangeListener, ActionListener
{
	public abstract void onInputEvent();
	
	//
	
	public static final String PROPERTY_DOCUMENT = "document";
	private boolean enabled = true;
	private Timer timer;
	
	
	public InputTracker()
	{
	}
	
	
	public InputTracker(JComponent c)
	{
		add(c);
	}
	
	
	public InputTracker(JComponent ... cs)
	{
		for(JComponent c: cs)
		{
			add(c);
		}
	}
	
	
	public InputTracker(int delay, JComponent c)
	{
		setDelay(delay);
		add(c);
	}
	
	
	public InputTracker(int delay, JComponent ... cs)
	{
		setDelay(delay);
		
		for(JComponent c: cs)
		{
			add(c);
		}
	}
	
	
	public void add(JComponent c)
	{
		if(c instanceof JTextComponent)
		{
			JTextComponent tc = (JTextComponent)c;
			tc.getDocument().addDocumentListener(this);
			tc.addPropertyChangeListener(PROPERTY_DOCUMENT, this);
		}
		else if(c instanceof ItemSelectable)
		{
			((ItemSelectable)c).addItemListener(this);
		}
		else
		{
			throw new Rex("ignored: " + c);
		}
	}
	
	
	protected void fireEvent()
	{
		if(enabled)
		{
			onInputEvent();
		}
	}
	
	
	public void setDelay(int ms)
	{
		if(ms > 0)
		{
			if(timer == null)
			{
				timer = new Timer(ms, this);
				timer.setRepeats(false);
			}
			
			timer.setInitialDelay(ms);
		}
		else
		{
			if(timer != null)
			{
				timer.stop();
				timer = null;
			}
		}
	}
	
	
	public void actionPerformed(ActionEvent ev)
	{
		fireEvent();
	}
	
	
	public void triggerInputTracker()
	{
		if(enabled)
		{
			if(timer == null)
			{
				fireEvent();
			}
			else
			{
				timer.stop();
				timer.start();
			}
		}
	}
	
	
	public void propertyChange(PropertyChangeEvent ev)
	{
		if(PROPERTY_DOCUMENT.equals(ev.getPropertyName()))
		{
			if(ev.getOldValue() instanceof Document)
			{
				((Document)ev.getOldValue()).removeDocumentListener(this);
			}
			
			if(ev.getNewValue() instanceof Document)
			{
				((Document)ev.getNewValue()).addDocumentListener(this);
				// fire event?
			}
		}
	}
	
	
	public void fire()
	{
		onInputEvent();
	}
	
	
	public void setEnabled(boolean on)
	{
		enabled = on;
	}


	public void disable()
	{
		enabled = false;
	}
	
	
	public void enable()
	{
		enabled = true;
	}
	
	
	public void itemStateChanged(ItemEvent ev)
	{
		if(handleEvent(ev))
		{
			triggerInputTracker();
		}
	}
	
	
	protected boolean handleEvent(ItemEvent ev)
	{
		if(ev.getSource() instanceof JComboBox)
		{
			// care about selection only
			return (ev.getStateChange() == ItemEvent.SELECTED);
		}
		return true;
	}
	
	
	public void changedUpdate(DocumentEvent ev) { }
	public void insertUpdate(DocumentEvent ev) { triggerInputTracker(); }
	public void removeUpdate(DocumentEvent ev) { triggerInputTracker(); }
}
