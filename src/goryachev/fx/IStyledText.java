// Copyright © 2022-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.scene.paint.Color;


/**
 * Defines an interface for monospaced rich text with limited styling,
 * as provided by TextCellStyle.
 * 
 * This class only works in situations where there is 1:1 correspondence 
 * between characters and glyphs, and therefore does not support combining characters
 * or Unicode symbols beyond the Basic Multilingual Plane.
 */
public interface IStyledText
{
	/** returns the plain text, or null */
	public String getPlainText();
	
	
	/** length of the plain text, or 0 if unknown */
	public int getTextLength();


	/** 
	 * returns cell styles at the given char index, or null if no styling exists.
	 * The styles should not include view-specific styles such as current line or cursor.
	 */
	public TextCellStyle getCellStyle(int charOffset);
	
	
	/** returns a line color or null */
	public Color getLineColor();


	/** returns a character in the specific cell */
	public char charAt(int charOffset);
}
