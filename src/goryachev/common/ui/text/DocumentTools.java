// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.text;
import goryachev.common.util.CKit;
import goryachev.common.util.Log;
import goryachev.common.util.TextTools;
import goryachev.common.util.TextTools.SeparatorFunction;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;


/** 
 * Utility methods associated with JTextComponents and their Documents. 
 * See also StringTools
 */
public class DocumentTools
{
	public static final int MASK_ALL_MODIFIERS = 
		InputEvent.CTRL_DOWN_MASK | 
		InputEvent.SHIFT_DOWN_MASK | 
		InputEvent.ALT_DOWN_MASK | 
		InputEvent.ALT_GRAPH_DOWN_MASK | 
		InputEvent.META_DOWN_MASK;
	
	//
	
	
	private static DocumentFilter readOnlyFilter;
	

	public static int getLineNumber(StyledDocument d, int offset) throws BadLocationException
	{
		checkValidOffset(d, offset);

		Element em = d.getParagraphElement(0).getParentElement();
		return em.getElementIndex(offset);
	}
	
	
	public static int getLineNumber_PLEASE_VERIFY(Document d, int offset)
	{
		Element root = d.getDefaultRootElement();
		return root.getElementIndex(offset);
	}
	
	
	/** returns string of text at the line specified by the offset */
	public static String getTextLine(StyledDocument d, int offset) throws Exception
	{
		checkValidOffset(d, offset);

		Element em = d.getParagraphElement(offset);
		int start = em.getStartOffset();
		int end = em.getEndOffset();
		return d.getText(start, end - start);
	}
	
	
	public static int getLineStartOffset(StyledDocument d, int offset) throws Exception
	{
		checkValidOffset(d, offset);

		Element em = d.getParagraphElement(offset);
		return em.getStartOffset();
	}


	public static void checkValidOffset(Document doc, int offset) throws BadLocationException
	{
		if(offset < 0)
		{
			throw new BadLocationException("Negative offset", offset);
		}

		int len = doc.getLength();
		if(offset > len)
		{
			throw new BadLocationException("Invalid offset", offset);
		}
	}


	public static int getRowCount(StyledDocument doc)
	{
		return doc.getParagraphElement(0).getParentElement().getElementCount();
	}
	

	public static int getOffsetAtLine(StyledDocument doc, int lineNumber_0_based)
	{
		Element root = doc.getDefaultRootElement();
		Element x = root.getElement(lineNumber_0_based);
		return x.getStartOffset();
	}
	
	
	public static String getLeadingWhitespace(String s)
	{
		for(int i=0; i<s.length(); i++)
		{
			char c = s.charAt(i);
			if((c == '\n') || !Character.isWhitespace(c))
			{
				return s.substring(0, i);
			}
		}
		return "";
	}
	
	
	public static void replace(Document d, int off, int len, String text) throws Exception
	{
		if(d instanceof AbstractDocument)
		{
			((AbstractDocument)d).replace(off, len, text, null);
		}
		else
		{
			d.remove(off, len);
			d.insertString(off, text, null);
		}
	}


	public static int lastIndexOfWhitespace(String s, int pos)
	{
		int len = s.length();
		if(pos < 0)
		{
			throw new IllegalArgumentException("pos<0");
		}
		else if(pos >= len)
		{
			throw new IllegalArgumentException("pos>len");
		}
		
		for(int i=pos-1; i>=0; i--)
		{
			char c = s.charAt(i);
			if(CKit.isBlank(c))
			{
				return i+1;
			}
		}
		
		return -1;
	}
	
	
	public static int indexOfWhitespace(String s, int pos)
	{
		int len = s.length();
		if(pos < 0)
		{
			throw new IllegalArgumentException("pos<0");
		}
		else if(pos >= len)
		{
			throw new IllegalArgumentException("pos>len");
		}
		
		for(int i=pos; i<len; i++)
		{
			char c = s.charAt(i);
			if(CKit.isBlank(c))
			{
				return i;
			}
		}
		
		return -1;
	}


	/** set caret at position specified by point, unless there is a selection.  useful in handling of right click menus */
	public static void setCaret(JTextComponent c, Point p)
	{
		if(c.getCaret().getMark() == c.getCaret().getDot())
		{
			int pos = c.viewToModel(p);
			if(pos < 0)
			{
				pos = 0;
			}
			c.setCaretPosition(pos);
		}
	}
	
	
	public static String getWordAtCursor(JTextComponent c, TextTools.SeparatorFunction f)
	{
		int pos = c.getCaretPosition();
		Document doc = c.getDocument();
		if(doc instanceof StyledDocument)
		{
			try
			{
				StyledDocument d = (StyledDocument)doc;
				String text = getTextLine(d, pos);
				int line = getLineStartOffset(d, pos);
		
				pos -= line;
				
				int beg = TextTools.lastIndexOfSeparator(text, pos, f);
				int end = TextTools.indexOfSeparator(text, pos, f);
				if(beg == end)
				{
					return null;
				}
				
				if(beg < 0)
				{
					beg = 0;
				}
		
				if(end < 0)
				{
					end = text.length();
				}
							
				String s = text.substring(beg, end);
				return s;
			}
			catch(Exception e)
			{
				Log.err(e);
			}
		}
		return null;
	}


	public static String getSelectedText(JTextComponent t, int mark, int dot)
	{
		try
		{
			int off = Math.min(mark, dot);
			int len = Math.max(mark, dot) - off;
			return t.getDocument().getText(off, len);
		}
		catch(Exception e)
		{
			Log.err(e);
			return null;
		}
	}
	
	
	public static String getSelectionOrText(JTextComponent t)
	{
		try
		{
			Caret c = t.getCaret();
			int mark = c.getMark();
			int dot = c.getDot();
			if(mark == dot)
			{
				return t.getText();
			}
			else
			{
				int off = Math.min(mark, dot);
				int len = Math.max(mark, dot) - off;
				return t.getDocument().getText(off, len);
			}
		}
		catch(Exception e)
		{
			Log.err(e);
			return null;
		}
	}
	
	
	/** this filter makes the text component non-editable while showing the caret */
	public static DocumentFilter readOnlyFilter()
	{
		if(readOnlyFilter == null)
		{
			readOnlyFilter = new CDocumentFilter(false);
		}
		return readOnlyFilter;
	}
	
	
	public static int getLineStartOffset_PLEASE_VERIFY(JTextComponent c, int line) throws BadLocationException
	{
		Element root = c.getDocument().getDefaultRootElement();
		Element em = root.getElement(line);
		if(em == null)
		{
			return -1;
		}
		return em.getStartOffset();
	}
	
	
	/** navigates to line (0-based) and scrolls it to view */
	public static void goToLine(JTextComponent c, Integer line)
	{
		if(line == null)
		{
			return;
		}
		
		try
		{
			if(line >= 0)
			{
				int off = getLineStartOffset_PLEASE_VERIFY(c, line);
				if(off >= 0)
				{
					c.setCaretPosition(off);
					
					Rectangle r = c.modelToView(off);
					c.scrollRectToVisible(r);
				}
			}
		}
		catch(Exception e)
		{
			Log.err(e);
		}
	}


	public static String toString(Document d)
	{
		if(d != null)
		{
			try
            {
	            return d.getText(0, d.getLength());
            }
            catch(Exception ignore)
            {
            }
		}
		return null;
	}
}
