// Copyright © 2016-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock.internal;
import goryachev.common.util.FH;


/**
 * Drop zone position in the screen coordinates.
 */
public class WhereScreen
{
	public final double screenx;
	public final double screeny;
	
	
	public WhereScreen(double screenx, double screeny)
	{
		this.screenx = screenx;
		this.screeny = screeny;
	}
	
	
	@Override
	public boolean equals(Object x)
	{
		if(x == this)
		{
			return true;
		}
		else if(x instanceof WhereScreen)
		{
			WhereScreen w = (WhereScreen)x;
			return 
				(screenx == w.screenx) &&
				(screeny == w.screeny);
		}
		else
		{
			return false;
		}
	}
	
	
	@Override
	public int hashCode()
	{
		int h = FH.hash(0, WhereScreen.class);
		h = FH.hash(h, screenx);
		return FH.hash(h, screeny);
	}
	
	
	@Override
	public String toString()
	{
		return "(" + screenx + "," + screeny + ")";
	}
}
