// Copyright © 2018-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.scene.text.Font;


/**
 * Monospaced Text Cell Metrics.
 */
public class TextCellMetrics
{
	public final Font font;
	public final double baseline;
	public final int cellWidth;
	public final int cellHeight;
	
	
	public TextCellMetrics(Font f, double baseline, int cellWidth, int cellHeight)
	{
		this.font = f;
		this.cellHeight = cellHeight;
		this.baseline = baseline;
		this.cellWidth = cellWidth;
	}
}
