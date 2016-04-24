// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.text;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.CButton;
import goryachev.common.ui.Cursors;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import goryachev.common.util.CBrowser;
import goryachev.common.util.Log;
import goryachev.common.util.SB;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;


public class CDocumentBuilder
{
	private CDocument d;
	private SimpleAttributeSet a;


	public CDocumentBuilder()
	{
		this(null);
	}


	public CDocumentBuilder(CDocument doc)
	{
		this.a = new SimpleAttributeSet();
		this.d = doc;

		if(d == null)
		{
			d = new CDocument();
		}
	}


	public CDocument getDocument()
	{
		return d;
	}


	public int getLength()
	{
		return d.getLength();
	}
	
	
	public boolean isNotEmpty()
	{
		return !isEmpty();
	}
	
	
	public boolean isEmpty()
	{
		return d.getLength() == 0;
	}


	public CDocumentBuilder a(Object x)
	{
		if(x != null)
		{
			insert(x.toString(), a);
		}
		return this;
	}
	
	
	public CDocumentBuilder line(Object x)
	{
		a(x);
		return nl();
	}


	public CDocumentBuilder append(String s)
	{
		insert(s, a);
		return this;
	}
	
	
	public CDocumentBuilder append(char c)
	{
		insert(String.valueOf(c), a);
		return this;
	}


	public CDocumentBuilder bold(String s)
	{
		setBold(true);
		insert(s, a);
		setBold(false);
		return this;
	}


	public CDocumentBuilder append(int n)
	{
		return append(String.valueOf(n));
	}


	public CDocumentBuilder setFont(Font f)
	{
		setAttribute(StyleConstants.FontFamily, f == null ? null : f.getName());
		setAttribute(StyleConstants.FontSize, f == null ? null : f.getSize());
		return this;
	}
	
	
	public CDocumentBuilder setFontSize(int size)
	{
		setAttribute(StyleConstants.FontSize, size);
		return this;
	}


	public CDocumentBuilder setBold(boolean on)
	{
		StyleConstants.setBold(a, on);
		return this;
	}


	public CDocumentBuilder setItalic(boolean on)
	{
		StyleConstants.setItalic(a, on);
		return this;
	}


	public CDocumentBuilder setUnderline(boolean on)
	{
		StyleConstants.setUnderline(a, on);
		return this;
	}


	public CDocumentBuilder setStrikeThrough(boolean on)
	{
		StyleConstants.setStrikeThrough(a, on);
		return this;
	}


	public CDocumentBuilder setForeground(Color c)
	{
		setAttribute(StyleConstants.Foreground, c);
		return this;
	}


	public CDocumentBuilder setBackground(Color c)
	{
		setAttribute(StyleConstants.Background, c);
		return this;
	}
	
	
	/** FIX does not work */
	public CDocumentBuilder setLeftIndent(float x)
	{
		setAttribute(StyleConstants.LeftIndent, x);
		return this;
	}


	public void setAttribute(Object name, Object value)
	{
		if(value == null)
		{
			a.removeAttribute(name);
		}
		else
		{
			a.addAttribute(name, value);
		}
	}


	public void insert(String s, AttributeSet as)
	{
		try
		{
			d.insertString(d.getLength(), s, as);
		}
		catch(BadLocationException ignore)
		{
		}
	}


	public void addButton(ImageIcon icon, String text, Action action)
	{
		CButton b = new CButton(icon, text, action);
		b.setMargin(1, 4);
		b.setFocusable(false);
		b.setAlignmentY(1.0f);

		SimpleAttributeSet as = new SimpleAttributeSet();
		StyleConstants.setComponent(as, b);
		insert(" ", as);
	}


	public void addIcon(ImageIcon icon)
	{
		SimpleAttributeSet as = new SimpleAttributeSet();
		as.addAttribute(AbstractDocument.ElementNameAttribute, StyleConstants.IconElementName);
		as.addAttribute(StyleConstants.IconAttribute, icon);
		insert(" ", as);
	}
	
	
	public CDocumentBuilder tab()
	{
		append("\t");
		return this;
	}


	public CDocumentBuilder tab(int n)
	{
		for(int i=0; i<n; i++)
		{
			append('\t');
		}
		return this;
	}


	public CDocumentBuilder nl()
	{
		append("\n");
		return this;
	}


	public CDocumentBuilder nl(int n)
	{
		for(int i=0; i<n; i++)
		{
			append('\n');
		}
		return this;
	}
	

	public CDocumentBuilder sp()
	{
		return a(' ');
	}
	
	
	public CDocumentBuilder sp(int n)
	{
		for(int i=0; i<n; i++)
		{
			append(' ');
		}
		return this;
	}
	

	// FIX does not work
	public CDocumentBuilder setCenter(boolean on)
	{
		a.addAttribute(StyleConstants.Alignment, on ? StyleConstants.ALIGN_CENTER : StyleConstants.ALIGN_LEFT);
		return this;
	}
	
	
	public void addComponent(Component c)
	{
		try
		{
			d.insertComponent(c, d.getLength());
		}
		catch(Exception e)
		{
			Log.err(e);
		}
	}


	public void addLink(Object link, String text)
	{
		boolean ul = StyleConstants.isUnderline(a);
		Color fg = StyleConstants.getForeground(a);
		
		StyleConstants.setUnderline(a, true);
		StyleConstants.setForeground(a, Theme.LINK_COLOR);
		a.addAttribute(CDocument.AttributeLink, link);
		
		insert(text, a);
		
		a.removeAttribute(CDocument.AttributeLink);
		StyleConstants.setForeground(a, fg);
		StyleConstants.setUnderline(a, ul);
	}
	
	
	public void link(String url)
	{
		link(url, null);
	}
	
	
	// FIX link handler property
	// FIX special element type and view
	public void link(final String url, String text)
	{
		SB sb = new SB();
		sb.a("<html><u>");
		if(text == null)
		{
			sb.safeHtml(url); // TODO decode url
		}
		else
		{
			sb.safeHtml(text);
		}
		sb.a("</html>");

		JLabel b = new JLabel(sb.toString());
		b.setBorder(CBorder.NONE);
		b.setForeground(Theme.LINK_COLOR);
		b.setMaximumSize(b.getPreferredSize());
		b.setBackground(Color.red);
		b.setCursor(Cursors.HAND);
		b.setAlignmentY(0.8f); // I am afraid this may not work for all font sizes
		b.setBorder(CBorder.NONE);		
		b.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent ev)
			{
				if(UI.isLeftButton(ev))
				{
					CBrowser.openLinkQuiet(url);
				}
			}
		});
		
		addComponent(b);
	}
}
