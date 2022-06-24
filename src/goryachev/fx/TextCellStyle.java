// Copyright © 2019-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.util.CKit;
import goryachev.common.util.FH;
import goryachev.common.util.SB;
import goryachev.fx.internal.StandardFxProperties;
import javafx.scene.paint.Color;


/**
 * (Monospaced) text cell style.
 */
public class TextCellStyle
	implements Cloneable
{
	public static final TextCellStyle NONE = new TextCellStyle();

	private Color textColor;
	private Color backgroundColor;
	private boolean bold;
	private boolean italic;
	private boolean strikeThrough;
	private boolean underscore;
	private String style;
	
	
	public TextCellStyle(Color fg, Color bg, boolean bold, boolean italic, boolean strikeThrough, boolean underscore)
	{
		this.textColor = fg;
		this.backgroundColor = bg;
		this.bold = bold;
		this.italic = italic;
		this.strikeThrough = strikeThrough;
		this.underscore = underscore;
	}
	
	
	public TextCellStyle(Color fg)
	{
		this.textColor = fg;
	}
	
	
	public TextCellStyle()
	{
	}
	
	
	public TextCellStyle copy()
	{
		return (TextCellStyle)clone();
	}
	
	
	public Object clone()
	{
		return new TextCellStyle(textColor, backgroundColor, bold, italic, strikeThrough, underscore);
	}
	
	
	public boolean isEmpty()
	{
		return
			!bold &&
			!italic &&
			!strikeThrough &&
			!underscore &&
			(backgroundColor == null) &&
			(textColor == null);
	}
	
	
	public void init(TextCellStyle x)
	{
		this.backgroundColor = x.backgroundColor;
		this.textColor = x.textColor;
		this.bold = x.bold;
		this.italic = x.italic;
		this.strikeThrough = x.strikeThrough;
		this.underscore = x.underscore;
	}
	
	
	public boolean equals(Object x)
	{
		if(x == this)
		{
			return true;
		}
		else if(x instanceof TextCellStyle)
		{
			TextCellStyle c = (TextCellStyle)x;
			return
				(bold == c.bold) &&
				(italic == c.italic) &&
				(strikeThrough == c.strikeThrough) &&
				(underscore == c.underscore) &&
				CKit.equals(textColor, c.textColor) &&
				CKit.equals(backgroundColor, c.backgroundColor);
		}
		return false;
	}
	
	
	public int hashCode()
	{
		int h = FH.hash(TextCellStyle.class);
		h = FH.hash(h, backgroundColor);
		h = FH.hash(h, textColor);
		h = FH.hash(h, bold);
		h = FH.hash(h, italic);
		h = FH.hash(h, strikeThrough);
		h = FH.hash(h, underscore);
		return h;
	}
	
	
	public void clear()
	{
		this.backgroundColor = null;
		this.textColor = null;
		this.bold = false;
		this.italic = false;
		this.strikeThrough = false;
		this.underscore = false;
	}
	
	
	public Color getBackgroundColor()
	{
		return backgroundColor;
	}
	
	
	public void setBackgroundColor(Color c)
	{
		backgroundColor = c;
		style = null;
	}
	
	
	public Color getTextColor()
	{
		return textColor;
	}
	
	
	public void setTextColor(Color c)
	{
		textColor = c;
		style = null;
	}


	public boolean isBold()
	{
		return bold;
	}
	
	
	public void setBold(boolean on)
	{
		bold = on;
		style = null;
	}


	public boolean isItalic()
	{
		return italic;
	}
	
	
	public void setItalic(boolean on)
	{
		italic = on;
		style = null;
	}
	
	
	public boolean isStrikeThrough()
	{
		return strikeThrough;
	}
	
	
	public void setStrikeThrough(boolean on)
	{
		strikeThrough = on;
		style = null;
	}
	
	
	public boolean isUnderscore()
	{
		return underscore;
	}
	
	
	public void setUnderscore(boolean on)
	{
		underscore = on;
		style = null;
	}
	
	
	public String getStyle()
	{
		if(style == null)
		{
			SB sb = new SB(128);
			
			if(textColor != null)
			{
				StandardFxProperties.textFill(textColor).write(sb);
			}
//			private Color backgroundColor; TODO
			
			if(bold)
			{
				StandardFxProperties.fontWeight(StandardFxProperties.BOLD).write(sb);
			}
			
			if(italic)
			{
				StandardFxProperties.fontStyle("italic").write(sb);
			}
			
//			private boolean strikeThrough; // TODO
//			private boolean underscore;
			
			style = sb.toString();
		}
		return style;
	}
}