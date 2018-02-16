// Copyright © 2016-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit.internal;


/**
 * Enapsulates local caret coordinates.  
 * A caret is expected to be a single vertical line.
 */
public class CaretLocation
{
	public final double x;
	public final double y0;
	public final double y1;
	

	public CaretLocation(double x, double y0, double y1)
	{
		this.x = x;
		this.y0 = y0;
		this.y1 = y1;
	}
	
	
	public String toString()
	{
		return "(" + x + "," + y0 + ".." + y1 + ")";
	}


	public boolean containsY(double y)
	{
		return (y >= y0) && (y < y1);
	}
}
