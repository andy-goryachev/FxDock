// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;


/**
 * Enapsulates local caret coordinates.  A caret is expected to be a single vertical line.
 */
public class CaretLocation
{
	public final double x0;
	public final double x1;
	public final double y0;
	public final double y1;
	

	public CaretLocation(double x0, double x1, double y0, double y1)
	{
		this.x0 = x0;
		this.x1 = x1;
		this.y0 = y0;
		this.y1 = y1;
	}
}
