// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.text;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.JTextComponent;


public class CDocumentFilter
	extends DocumentFilter
{
	private boolean allowChanges;


	public CDocumentFilter()
	{
		this(false);
	}


	public CDocumentFilter(boolean allowChanges)
	{
		this.allowChanges = allowChanges;
	}
	
	
	public void attachTo(JTextComponent t)
	{
		((AbstractDocument)t.getDocument()).setDocumentFilter(this);
	}
	
	
	public void setAllowChanges(boolean on)
	{
		allowChanges = on;
	}
	
	
	public boolean getAllowChanges()
	{
		return allowChanges;
	}


	public void remove(FilterBypass fb, int offset, int length) throws BadLocationException
	{
		if(allowChanges)
		{
			fb.remove(offset, length);
		}
	}


	public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException
	{
		if(allowChanges)
		{
			fb.insertString(offset, string, attr);
		}
	}


	public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException
	{
		if(allowChanges)
		{
			fb.replace(offset, length, text, attrs);
		}
	}
}
