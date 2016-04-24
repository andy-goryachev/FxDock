// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.text;
import goryachev.common.util.Log;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;


public class CDocument
	extends DefaultStyledDocument
{
	public static final Object AttributeLink = new Object();
	
	
	public CDocument()
	{
	}


	public CDocument(StyleContext sc)
	{
		super(sc);
	}
	
	
	public void clear()
	{
		try
		{
			remove(0, getLength());
		}
		catch(BadLocationException e)
		{ }
	}
	
	
	public String getText()
	{
		try
		{
			return getText(0, getLength());
		}
		catch(BadLocationException e)
		{
			return null;
		}
	}
	
	
	public CDocument addIcon(ImageIcon icon)
	{
		SimpleAttributeSet as = new SimpleAttributeSet();
		as.addAttribute(AbstractDocument.ElementNameAttribute, StyleConstants.IconElementName);
		as.addAttribute(StyleConstants.IconAttribute, icon);
		append(" ", as);
		return this;
	}
	
	
	public CDocument add(Object x)
	{
		if(x != null)
		{
			append(x.toString(), null);
		}
		return this;
	}


	public void append(String s, AttributeSet as)
	{
		try
		{
			insertString(getLength(), s, as);
		}
		catch(BadLocationException ignore)
		{ }
	}
	

	public void append(String s)
	{
		try
		{
			insertString(getLength(), s, null);
		}
		catch(Exception e)
		{
			Log.err(e);
		}
	}


	/** sets PARAGRAPH style */
	public void setStyle(Style s)
	{
		setLogicalStyle(getLength(), s);
	}


	public void applyStyle(int start, int end, Style s)
	{
		setParagraphAttributes(start, end - start, s, false);
	}
	

	public void insertComponent(Component c, int offset) throws Exception
	{
		SimpleAttributeSet a = new SimpleAttributeSet();
		StyleConstants.setComponent(a, c);
		super.insertString(offset, " ", a);
	}


	protected void insertStringPrivate(int offset, String s, AttributeSet a) throws BadLocationException
	{
		super.insertString(offset, s, a);
	}


	public static CDocument get(JTextComponent c)
	{
		Document d = c.getDocument();
		if(d instanceof CDocument)
		{
			return (CDocument)d;
		}
		return null;
	}


	public Object getLink(int offset)
	{
		Element em = getCharacterElement(offset);
		return em.getAttributes().getAttribute(AttributeLink);
	}
}
