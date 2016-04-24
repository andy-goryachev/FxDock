// Copyright (c) 2015-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.ui.icons.CIcons;
import goryachev.common.ui.theme.ThemeColor;
import goryachev.common.util.CKit;
import goryachev.common.util.CancelledException;
import goryachev.common.util.Log;
import goryachev.common.util.UserException;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;


public class Panels
{
	private static final ThemeColor ICON_FIELD_BG = ThemeColor.create(ThemeKey.TEXT_FG, 0.04, ThemeKey.TEXT_BG);
	
	
	public static JLabel iconField(Icon ic)
	{
		JLabel t = iconField();
		t.setIcon(ic);
		return t;
	}
	
	
	public static JLabel iconField()
	{
		JLabel t = new JLabel();
		t.setVerticalAlignment(JLabel.TOP);
		t.setBorder(new CBorder(20));
		t.setOpaque(true);
		t.setBackground(ICON_FIELD_BG);
		return t;
	}
	
	
	public static CTextPane textPane()
	{
		CTextPane t = new CTextPane();
		t.setBorder(new CBorder(4));
		t.setFont(Theme.plainFont());
		t.setEditable(false);
		t.setScrollableTracksViewportWidth(true);
		return t;
	}
	
	
	public static CTextPane textPane(String text)
	{
		CTextPane t = textPane();
		t.setText0(text);
		return t;
	}
	
	
	public static CScrollPane scrollText(String text)
	{
		CTextPane t = textPane();
		t.setText(text);
		t.setCaretPosition(0);
		
		return scroll(t);
	}
	
	
	public static CHtmlPane textHtml()
	{
		CHtmlPane t = new CHtmlPane();
		t.setEditable(false);
		return t;
	}
	
	
	public static CHtmlPane textHtml(String html)
	{
		CHtmlPane t = textHtml();
		t.setText(html);
		t.setCaretPosition(0);
		return t;
	}
	
	
	public static CScrollPane scrollHtml(String html)
	{
		CHtmlPane t = textHtml(html);
		return scroll(t);
	}
	
	
	public static CScrollPane scroll(Component c)
	{
		CScrollPane p = new CScrollPane(c, false);
		p.setBackground2(Theme.TEXT_BG);
		return p;
	}
	
	
	public static CScrollPane scrollTransparent(Component c)
	{
		CScrollPane p = new CScrollPane(c, false);
		p.setOpaque2(false);
		return p;
	}
	
	
	public static CScrollPane scroll(Component c, boolean horScrollBar)
	{
		CScrollPane p = new CScrollPane(c, horScrollBar);
		p.setBackground2(Theme.TEXT_BG);
		return p;
	}
	
	
	public static CTextPane textPane(Document d)
	{
		CTextPane t = new CTextPane();
		t.setDocument(d);
		t.setOpaque(false);
		t.setEditable(false);
		t.setBorder(new CBorder(4));
		t.setScrollableTracksViewportWidth(true);
		t.setCaretPosition(0);
		
		return t;
	}
	
	
	public static JTextComponent textComponent(String s)
	{
		if(CKit.startsWithIgnoreCase(s, "<html>"))
		{
			return textHtml(s);
		}
		else
		{
			return textPane(s);
		}
	}
	
	
	public static CScrollPane scrollDocument(Document d)
	{
		CTextPane t = textPane(d);
		return scroll(t);
	}
	
	
	public static CTextArea textArea(String message, boolean wrap)
	{
		CTextArea t = textArea(wrap);
		t.setText0(message);
		return t;
	}
	
	
	public static CTextArea textArea(boolean wrap)
	{
		CTextArea t = new CTextArea();
		t.setMinimumSize(new Dimension(20, 1));
		if(wrap)
		{
			t.setWrapStyleWord(true);
			t.setLineWrap(true);
		}
		t.setOpaque(false);
		t.setEditable(false);
		t.setFont(Theme.plainFont());
		t.setBorder(new CBorder(4));
		return t;
	}
	
	
	
//	@Deprecated // FIX
//	public static JTextArea createTextArea(String message)
//	{
//		JTextArea t = new JTextArea(message);
//		t.setWrapStyleWord(true);
//		t.setLineWrap(true);
//		t.setOpaque(false);
//		t.setEditable(false);
//		t.setFont(Theme.plainFont());
//		return t;
//	}
//	
//	
//	public static CPanel createInfoPanel(String title, ImageIcon icon, String text)
//	{
//		JTextArea t = createTextArea(text);
//		return createContentPanel(title, icon, t);
//	}
//	
//	
//	public static CPanel createHtmlPanel(String title, ImageIcon icon, String html)
//	{
//		CHtmlPane t = new CHtmlPane();
//		t.setOpaque(false);
//		t.setText(html);
//		
//		return createContentPanel(title, icon, t);
//	}
//	
//
//	public static CPanel createContentPanel(String title, ImageIcon icon, JComponent content)
//	{
//		JLabel titleLabel = new JLabel(title);
//		titleLabel.setFont(Theme.titleFont());
//		titleLabel.setBackground(Theme.panelBG().brighter());
//		titleLabel.setOpaque(true);
//		titleLabel.setBorder(new CBorder(0, 0, 1, 0, Color.gray, 20));
//
//		JLabel iconLabel = new JLabel(icon);
//		iconLabel.setBorder(new CBorder(10, 0, 0, 10));
//		iconLabel.setPreferredSize(new Dimension(96, 96));
//
//		content.setBorder(new CBorder(10, 10, 0, 10));
//
//		CPanel p = new CPanel();
//		p.setNorth(titleLabel);
//		p.setWest(iconLabel);
//		p.setCenter(content);
//		return p;
//	}
	
	
//	public CTextArea textArea()
//	{
//		CTextArea t = new CTextArea();
//		t.setLineWrap(true);
//		t.setWrapStyleWord(true);
//		t.setFont(Theme.plainFont());
//		t.setBorder(Theme.BORDER_FIELD);
//		return t;
//	}
	
	
	// perhaps this needs own component to be able to set borders, colors
	public static CPanel info(Icon icon, String text)
	{
		JTextComponent t = textComponent(text);
		t.setOpaque(false);
		
		CPanel p = new CPanel(10, 10);
		p.border();
		
		if(icon != null)
		{
			JLabel ic = new JLabel(icon);
			ic.setVerticalAlignment(JLabel.TOP);
			p.setLeading(ic);
		}
		p.setCenter(scrollTransparent(t));
		return p;
	}
	
	
	public static CPanel errorPane(Object exceptionOrMessage)
	{
		String msg;
		JTextComponent t;
		CScrollPane s;
		Img icon = CIcons.Error96;
		
		if(exceptionOrMessage instanceof Throwable)
		{
			if(exceptionOrMessage instanceof UserException)
			{
				msg = ((UserException)exceptionOrMessage).getMessage();
				t = Panels.textComponent(msg);
				s = Panels.scroll(t);
			}
			else
			{
				Throwable e = (Throwable)exceptionOrMessage;
				
				if(e instanceof CancelledException)
				{
					// ignore
					icon = CIcons.Cancelled96;
				}
				else if(e instanceof InterruptedException)
				{
					// ignore
					icon = CIcons.Cancelled96;
				}
				else
				{
					Log.err(e);
				}
				
				msg = CKit.stackTrace(e);
				t = Panels.textArea(msg, false);
				s = Panels.scroll(t, true);
			}
		}
		else
		{
			msg = String.valueOf(exceptionOrMessage);
			if(CKit.isBlank(msg))
			{
				msg = "Error";
			}
			t = Panels.textComponent(msg);
			s = Panels.scroll(t);
		}
		
		t.setBorder(new CBorder(20));
		
		CPanel p = new CPanel();
		p.setLeading(Panels.iconField(icon));
		p.setCenter(s);
		return p;
	}
}
