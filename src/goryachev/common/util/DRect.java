// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.awt.geom.Rectangle2D;


public class DRect
	extends Rectangle2D.Double
{
	public DRect(double x, double y, double w, double h)
	{
		super(x, y, w, h);
	}


	public DRect()
	{
	}
	
	
	public static DRect union(DRect a, DRect b)
	{
		if(a == null)
		{
			return b;
		}
		else if(b == null)
		{
			return a;
		}
		
		DRect rv = new DRect();
		union(a, b, rv);
		return rv;
	}
}
