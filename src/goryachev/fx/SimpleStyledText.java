// Copyright © 2021-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.scene.paint.Color;


/**
 * Single-style Styled Text.
 */
public class SimpleStyledText
	implements IStyledText
{
	private final String text;
	private final TextCellStyle style;
	
	
	public SimpleStyledText(String text, TextCellStyle style)
	{
		this.text = text;
		this.style = style;
	}
	
	
	public static SimpleStyledText of(String text, Color textColor)
	{
		if(text == null)
		{
			return null;
		}
		
		TextCellStyle s = new TextCellStyle(textColor);
		
		return new SimpleStyledText(text, s);
	}
	
	
	public static SimpleStyledText of(String text, Color textColor, Color lineColor)
	{
		if(text == null)
		{
			text = "";
		}
		
		TextCellStyle s = new TextCellStyle(textColor);
		
		return new SimpleStyledText(text, s)
		{
			@Override
			public Color getLineColor()
			{
				return lineColor;
			}
		};
	}


	@Override
	public String getPlainText()
	{
		return text;
	}


	@Override
	public int getTextLength()
	{
		return text.length();
	}


	@Override
	public TextCellStyle getCellStyle(int charOffset)
	{
		return style;
	}


	@Override
	public Color getLineColor()
	{
		return null;
	}


	@Override
	public char charAt(int charOffset)
	{
		return text.charAt(charOffset);
	}
}
