// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.ui.text.CEditorKit;
import goryachev.common.ui.text.DocumentTools;
import goryachev.common.util.CKit;
import goryachev.common.util.Log;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.Element;


public class CTextPane
	extends JTextPane
{
	private boolean trackWidth;
	private boolean trackHeight;
	private boolean autoIndent;
	
	
	public CTextPane()
	{ 
		setSelectionColor(UI.setAlpha(getSelectionColor(), 128));
		
		addKeyListener(new Handler());
	}
	
	
	public void setBorder(int sz)
	{
		setBorder(new CBorder(sz));
	}
	
	
	public void setBorder(int v, int h)
	{
		setBorder(new CBorder(v, h));
	}


	protected EditorKit createDefaultEditorKit()
	{
		return new CEditorKit();
	}

	
	public void setText0(String s)
	{
		setText(s);
		setCaretPosition(0);
	}


	public void setAutoIndent(boolean on)
	{
		autoIndent = on;
	}
	
	
	public boolean getAutoIndent()
	{
		return autoIndent;
	}


	// FIX I think this is wrong
	public boolean getScrollableTracksViewportWidth()
	{
		if(trackWidth)
		{
			return super.getScrollableTracksViewportWidth();
		}
		else
		{
			// ?
			if(getParent() instanceof JViewport)
			{
				Dimension dim = getUI().getPreferredSize(this);
				int portWidth = ((JViewport)getParent()).getWidth();
				if((portWidth >= dim.width))
				{
					return true;
				}
			}
			return false;
		}
	}
	
	
	public boolean getScrollableTracksViewportHeight()
	{
		if(trackHeight)
		{
			return true;
		}
		else
		{
			return super.getScrollableTracksViewportHeight();
		}
	}
	
	
	public void setScrollableTracksViewportWidth(boolean on)
	{
		trackWidth = on;
	}
	
	
	public void setScrollableTracksViewportHeight(boolean on)
	{
		trackHeight = on;
	}
	

	public int getOffsetAtLine(int lineNumber)
	{
		return DocumentTools.getOffsetAtLine(getStyledDocument(), lineNumber);
	}


	public int getLineAtOffset(int offset) throws Exception
	{
		return DocumentTools.getLineNumber(getStyledDocument(), offset);
	}
	
	
	protected void handleKeyTyped(KeyEvent ev)
	{
		switch(ev.getKeyChar())
		{
		case '\n':
			try
			{
				if((ev.getModifiersEx() & DocumentTools.MASK_ALL_MODIFIERS) == 0)
				{
					if(autoIndent())
					{
						ev.consume();
					}
				}
			}
			catch(Exception e)
			{
				Log.err(e);
			}
			break;
		}
	}
	
	
	protected boolean autoIndent() throws Exception
	{
		int pos = getCaretPosition();
		int line = getLineAtOffset(pos);
		if(line > 0)
		{
			int start = getOffsetAtLine(line - 1);
			String s = getText(start, pos - start);
			if(CKit.isNotBlank(s))
			{
				s = DocumentTools.getLeadingWhitespace(s);
				if(s.length() > 0)
				{
					getDocument().insertString(pos, s, null);
					return true;
				}
			}
		}
		
		return false;
	}
	
	
	public void replaceSelection(String s, AttributeSet a) throws Exception
	{
		int start = getSelectionStart();
		int end = getSelectionEnd();
		AbstractDocument d = (AbstractDocument)getDocument();
		if(start == end)
		{
			d.insertString(start, s, a);
		}
		else
		{
			d.replace(start, end - start, s, a);
		}
	}


	public int getLineCount()
	{
		Element em = getDocument().getDefaultRootElement();
		return em.getElementCount();
	}


	@Deprecated // use DocumentTools
	public int getLineStartOffset(int line) throws BadLocationException
	{
		int lineCount = getLineCount();
		if(line < 0)
		{
			throw new BadLocationException("Negative line", -1);
		}
		else if(line >= lineCount)
		{
			throw new BadLocationException("No such line", getDocument().getLength() + 1);
		}
		else
		{
			Element root = getDocument().getDefaultRootElement();
			Element em = root.getElement(line);
			return em.getStartOffset();
		}
	}
	
	
	public void setFixedFontMode(boolean on)
	{
		Object x = getEditorKit();
		if(x instanceof CEditorKit)
		{
			((CEditorKit)x).setNoWrapMode(on);
		}
	}
	
	
	public void setDocument0(Document d)
	{
		setDocument(d);
		setCaretPosition(0);
	}
	
	
	//


	public class Handler
		implements KeyListener
	{
		public void keyTyped(KeyEvent ev)
		{
			handleKeyTyped(ev);
		}

		public void keyPressed(KeyEvent ev)
		{
		}

		public void keyReleased(KeyEvent ev)
		{
		}
	}
}
