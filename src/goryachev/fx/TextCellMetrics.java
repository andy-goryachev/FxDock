// Copyright © 2018-2025 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.scene.text.Font;


/**
 * Monospaced Text Cell Metrics.
 */
public final class TextCellMetrics
{
	public final Font font;
	public final double baseLine;
	/** snapped */
	public final double cellWidth;
	/** snapped */
	public final double cellHeight;
	
	
	public TextCellMetrics(Font font, double baseline, double cellWidth, double cellHeight)
	{
		this.font = font;
		this.cellHeight = cellHeight;
		this.baseLine = baseline;
		this.cellWidth = cellWidth;
	}
}
