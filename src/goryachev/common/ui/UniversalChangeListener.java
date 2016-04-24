// Copyright (c) 2008-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import javax.swing.AbstractButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;


// see also InputTracker - combine?
public abstract class UniversalChangeListener
	implements DocumentListener, ChangeListener, ItemListener
{
	public abstract void onChangeEvent();
	
	//
	
	private ArrayList<JComponent> list = new ArrayList();
	
	
	public UniversalChangeListener(JComponent ... components)
	{
		for(JComponent c: components)
		{
			add(c);
		}
	}
	
	
	public void add(JComponent c)
	{
		list.add(c);
		
		if(c instanceof JTextComponent)
		{
			((JTextComponent)c).getDocument().addDocumentListener(this);
		}
		else if(c instanceof AbstractButton)
		{
			((AbstractButton)c).addChangeListener(this);
		}
		else if(c instanceof JComboBox)
		{
			((JComboBox)c).addItemListener(this);
		}
		else
		{
			throw new RuntimeException("don't know how to monitor change in: " + c);
		}
	}


	public void changedUpdate(DocumentEvent e) { onChangeEvent(); }
	public void insertUpdate(DocumentEvent e)  { onChangeEvent(); }
	public void removeUpdate(DocumentEvent e)  { onChangeEvent(); }
	public void stateChanged(ChangeEvent e)    { onChangeEvent(); }
	public void itemStateChanged(ItemEvent e) 
	{
		if(e.getStateChange() == ItemEvent.SELECTED)
		{
			onChangeEvent();
		}
	}
}
