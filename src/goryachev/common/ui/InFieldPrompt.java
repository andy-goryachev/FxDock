// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.util.CKit;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;


public class InFieldPrompt 
	implements DocumentListener, FocusListener, ActionListener
{
	// override to handle ENTER keys in JTextField
	public void onEnterPressed() { }
	
	//

	private JTextComponent component;
	private String prompt;
	private boolean handleEvents = true;
	private boolean modified;
	private Color originalColor;
	private Color promptColor = Color.gray;
	
	
	public InFieldPrompt(JTextComponent c, String prompt)
	{
		this.component = c;
		this.prompt = prompt;
		this.originalColor = c.getForeground();
		
		setTextInternal(prompt);
		component.setForeground(promptColor);
		component.getDocument().addDocumentListener(this);
		component.addFocusListener(this);
		
		if(component instanceof JTextField)
		{
			((JTextField)component).addActionListener(this);
		}
	}
	
	
	public void setText(String s)
	{
		if(CKit.isBlank(s))
		{
			s = prompt;
			component.setForeground(promptColor);
		}
		else
		{
			component.setForeground(originalColor);
		}
		
		setTextInternal(s);
		setModified(false);
	}
	
	
	protected void setTextInternal(String s)
	{
		handleEvents = false;
		component.setText(s);
		handleEvents = true;
	}
	
	
	public String getText()
	{
		if(isModified())
		{
			return component.getText();
		}
		else
		{
			return null;
		}
	}
	
	
	public boolean isModified()
	{
		return modified;
	}


	public void setModified(boolean on)
	{
		modified = on;
	}


	public void changedUpdate(DocumentEvent e)
	{
	}


	public void insertUpdate(DocumentEvent e)
	{
		modified();
	}


	public void removeUpdate(DocumentEvent e)
	{
		modified();
	}


	protected void modified()
	{
		if(handleEvents)
		{
			setModified(true);
			component.setForeground(originalColor);
		}
	}


	public void focusGained(FocusEvent e)
	{
		component.setForeground(originalColor);

		if(!isModified())
		{
			setTextInternal(null);
		}
	}


	public void focusLost(FocusEvent e)
	{
		if(CKit.isBlank(component.getText()))
		{
			component.setForeground(promptColor);
			setTextInternal(prompt);
			setModified(false);
		}
	}


	public void actionPerformed(ActionEvent ev)
	{
		onEnterPressed();
	}
}
