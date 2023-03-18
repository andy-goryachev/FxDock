// Copyright © 2020-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.fx.internal.ColorMixer;
import goryachev.fx.internal.TextStyleFlags;
import javafx.scene.paint.Color;


/**
 * A naive implementation of IStyledText, to be used when the text is known
 * at the time of construction, and styles are added incrementally.
 * 
 * Does not support combining characters, does not go beyond Unicode BMP.
 */
public class BasicStyledText
	implements IStyledText
{
	private final String text;
	private final byte[] styles;
	private Color[] textColor;
	private ColorMixer[] background;
	
	
	public BasicStyledText(String text)
	{
		this.text = text;
		this.styles = new byte[text.length()];
	}


	public String getPlainText()
	{
		return text;
	}
	
	
	public String toString()
	{
		return getPlainText();
	}


	public int getTextLength()
	{
		return text.length();
	}


	public TextCellStyle getCellStyle(int ix)
	{
		Color fg = getForeground(ix);
		Color bg = getBackground(ix);
		byte st = styles[ix];
		boolean bold = TextStyleFlags.isBold(st);
		boolean italic = TextStyleFlags.isItalic(st);
		boolean strikeThrough = TextStyleFlags.isStrikeThrough(st);
		boolean underscore = TextStyleFlags.isUnderscore(st);
		return new TextCellStyle(fg, bg, bold, italic, strikeThrough, underscore);
	}
	
	
	public Color getBackground(int ix)
	{
		if(background != null)
		{
			ColorMixer cs = background[ix];
			if(cs != null)
			{
				return cs.getColor();
			}
		}
		return null;
	}

	
	public Color getForeground(int ix)
	{
		if(textColor != null)
		{
			return textColor[ix];
		}
		return null;
	}


	public Color getLineColor()
	{
		return null;
	}


	public char charAt(int ix)
	{
		return text.charAt(ix);
	}
	
	
	public void setBold(int start, int end, boolean on)
	{
		start = Math.max(0, start);
		end = Math.min(text.length(), end);
		
		for(int i=start; i<end; i++)
		{
			TextStyleFlags.setBold(styles, i, on);
		}
	}
	
	
	public void setItalic(int start, int end, boolean on)
	{
		start = Math.max(0, start);
		end = Math.min(text.length(), end);
		
		for(int i=start; i<end; i++)
		{
			TextStyleFlags.setItalic(styles, i, on);
		}
	}
	
	
	public void setStrikeThrough(int start, int end, boolean on)
	{
		start = Math.max(0, start);
		end = Math.min(text.length(), end);
		
		for(int i=start; i<end; i++)
		{
			TextStyleFlags.setStrikeThrough(styles, i, on);
		}
	}
	
	
	public void setUnderscore(int start, int end, boolean on)
	{
		start = Math.max(0, start);
		end = Math.min(text.length(), end);
		
		for(int i=start; i<end; i++)
		{
			TextStyleFlags.setUnderscore(styles, i, on);
		}
	}
	
	
	public void highlight(int start, int end, Color c)
	{
		start = Math.max(0, start);
		end = Math.min(text.length(), end);
		
		if(background == null)
		{
			background = new ColorMixer[getTextLength()];
		}
		
		for(int i=start; i<end; i++)
		{
			ColorMixer cs = background[i];
			if(cs == null)
			{
				cs = new ColorMixer(c);
			}
			else
			{
				cs.add(c);
			}
			background[i] = cs;
		}
	}
	
	
	public void textColor(int start, int end, Color c)
	{
		start = Math.max(0, start);
		end = Math.min(text.length(), end);
		
		if(textColor == null)
		{
			textColor = new Color[getTextLength()];
		}
		
		for(int i=start; i<end; i++)
		{
			textColor[i] = c;
		}
	}
}
