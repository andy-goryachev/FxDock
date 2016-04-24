// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.scene.paint.Color;


/**
 * CssTools.
 */
public class CssTools
{
	public static String toColor(Object x)
	{
		if(x == null)
		{
			return "null";
		}
		else if(x instanceof Integer)
		{
			int rgb = (Integer)x;
			int r = (rgb >> 16) & 0xff;
			int g = (rgb >>  8) & 0xff;
			int b = (rgb      ) & 0xff;
			
			return String.format("#%02x%02x%02x", r, g, b);
		}
		else if(x instanceof Color)
		{
			Color c = (Color)x;
			double r = c.getRed();
			double g = c.getGreen();
			double b = c.getBlue();
			
			if(c.isOpaque())
			{
				return String.format("#%02x%02x%02x", to8bit(r), to8bit(g), to8bit(b));
			}
			else
			{
				double a = c.getOpacity();
				return "rgba(" + r + "," + g + "," + b + "," + a + ")";
			}
		}
		else
		{
			return x.toString();
		}
	}
	
	
	private static int to8bit(double x)
	{
		int v = (int)Math.round(255 * x);
		if(v < 0)
		{
			return 0;
		}
		else if(v > 255)
		{
			return 255;
		}
		return v;
	}
	
	
	public static String toValue(Object x)
	{
		if(x == null)
		{
			return "null";
		}
		else
		{
			return x.toString();
		}
	}
}
