// Copyright © 2017-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.common.util.CKit;
import goryachev.common.util.FH;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


/**
 * Text Segment Style.
 * 
 * TODO relative font size? font family?
 */
public class TStyle
{
	private static final int BOLD = 1;
	private static final int ITALIC = 1 << 2;
	private static final int UNDERLINE = 1 << 3;
	private static final int SUPERSCRIPT = 1 << 4;
	private static final int SUBSCRIPT = 1 << 5;
	private static final int STRIKETHROUGH = 1 << 6;
	
	private int flags;
	private Color bg;
	private Color fg;
	
	// TODO or CssStyle ?
	private String style;
	// TODO or family-style-size ?
	private Font font;
	
	
	public TStyle()
	{
	}
	
	
	public boolean equals(Object x)
	{
		if(x == this)
		{
			return true;
		}
		else if(x instanceof TStyle)
		{
			TStyle z = (TStyle)x;
			return
				(flags == z.flags) &&
				CKit.equals(fg, z.fg) &&
				CKit.equals(bg, z.bg) &&
				CKit.equals(style, z.style) &&
				CKit.equals(font, z.font);
		}
		else
		{
			return false;
		}
	}
	
	
	public int hashCode()
	{
		int h = FH.hash(TStyle.class);
		h = FH.hash(h, flags);
		h = FH.hash(h, bg);
		h = FH.hash(h, fg);
		h = FH.hash(h, style);
		h = FH.hash(h, font);
		return h;
	}
	
	
	protected boolean getFlag(int flag)
	{
		return ((flags & flag) != 0);
	}
	
	
	protected void setFlag(int flag, boolean on)
	{
		if(on)
		{
			flags |= flag;
		}
		else
		{
			flags &= (~flag);
		}
	}
	

	public boolean isBold()
	{
		return getFlag(BOLD);
	}
	
	
	public void setBold(boolean on)
	{
		setFlag(BOLD, on);
	}
	
	
	public TStyle bold()
	{
		setBold(true);
		return this;
	}
	
	
	public boolean isItalic()
	{
		return getFlag(ITALIC);
	}
	
	
	public void setItalic(boolean on)
	{
		setFlag(ITALIC, on);
	}
	
	
	public TStyle italic()
	{
		setItalic(true);
		return this;
	}
	
	
	public boolean isUnderline()
	{
		return getFlag(UNDERLINE);
	}
	
	
	public void setUnderline(boolean on)
	{
		setFlag(UNDERLINE, on);
	}
	
	
	public TStyle underline()
	{
		setUnderline(true);
		return this;
	}
	
	
	public boolean isSuperScript()
	{
		return getFlag(SUPERSCRIPT);
	}
	
	
	public void setSuperScript(boolean on)
	{
		setFlag(SUPERSCRIPT, on);
	}
	
	
	public TStyle superScript()
	{
		setSuperScript(true);
		return this;
	}
	
	
	public boolean isSubScript()
	{
		return getFlag(SUBSCRIPT);
	}
	
	
	public void setSubScript(boolean on)
	{
		setFlag(SUBSCRIPT, on);
	}
	
	
	public TStyle subScript()
	{
		setSubScript(true);
		return this;
	}
	
	
	public boolean isStrikeThrough()
	{
		return getFlag(STRIKETHROUGH);
	}
	
	
	public void setStrikeThrough(boolean on)
	{
		setFlag(STRIKETHROUGH, on);
	}
	
	
	public TStyle strikeThrough()
	{
		setStrikeThrough(true);
		return this;
	}
	
	
	public Color getForeground()
	{
		return fg;
	}
	
	
	public void setForeground(Color c)
	{
		fg = c;
	}
	
	
	public TStyle foreground(Color c)
	{
		setForeground(c);
		return this;
	}
	
	
	public Color getBackground()
	{
		return bg;
	}
	
	
	public void setBackground(Color c)
	{
		bg = c;
	}
	
	
	public TStyle background(Color c)
	{
		setBackground(c);
		return this;
	}
	
	
	public String getStyle()
	{
		return style;
	}
	
	
	public void setStyle(String s)
	{
		style = s;
	}
	
	
	public TStyle style(String s)
	{
		setStyle(s);
		return this;
	}
	
	
	public Font getFont()
	{
		return font;
	}
	
	
	public void setFont(Font f)
	{
		font = f;
	}

	
	public TStyle font(Font f)
	{
		setFont(f);
		return this;
	}
}
