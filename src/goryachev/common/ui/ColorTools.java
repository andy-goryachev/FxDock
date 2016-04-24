// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import java.awt.Color;


// formerly Colors
public class ColorTools
{
	public static Color darker(Color c, float factor)
	{
		if(c == null)
		{
			return null;
		}
		
		return new Color
		(
			Math.max((int)(c.getRed()   * factor), 0), 
			Math.max((int)(c.getGreen() * factor), 0), 
			Math.max((int)(c.getBlue()  * factor), 0)
		);
	}


	public static Color brighter(Color c, float factor)
	{
		if(c == null)
		{
			return null;
		}

		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();

		int i = (int)(1.0f / (1.0f - factor));
		if(r == 0 && g == 0 && b == 0)
		{
			return new Color(i, i, i);
		}
		
		if(r > 0 && r < i)
		{
			r = i;
		}
		if(g > 0 && g < i)
		{
			g = i;
		}
		if(b > 0 && b < i)
		{
			b = i;
		}

		return new Color
		(
			Math.min((int)(r / factor), 255), 
			Math.min((int)(g / factor), 255), 
			Math.min((int)(b / factor), 255)
		);
	}


	public static Gray gray(int x)
	{
		return new Gray(x);
	}


	public static Gray gray(int x, int alpha)
	{
		return new Gray(x, alpha);
	}


	// calculate gradient color between  a(d=1.0) and b (d=0.0)
	public static Color mix(float d, Color a, Color b)
	{
		if(d < 0)
		{
			d = 0;
		}
		else if(d > 1)
		{
			d = 1;
		}
		
		return new Color
		(
			gradient(d, a.getRed(),   b.getRed()),
			gradient(d, a.getGreen(), b.getGreen()),
			gradient(d, a.getBlue(),  b.getBlue())
		);
	}
	
	
	// calculate gradient color between a(d=1.0) and b(d=0.0)
	public static int gradient(float d, int a, int b)
	{
		float c = (a - b) * d + b;
		if(c < 0.0)
		{
			c = 0;
		}
		else if(c > 255)
		{
			c = 255;
		}
		return Math.round(c);
	}
	
	
	public static Color alpha(Color c, int alpha)
	{
		return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
	}
	
	
	public static Color alpha(Color c, double alpha)
	{
		int a = toInt255(alpha);
		return new Color(c.getRed(), c.getGreen(), c.getBlue(), a);
	}
	

	/** 
	 * converts a double value in the range 0 .. 1.0 to an integer in the range 0..255,
	 * while clipping the output if the input value falls outside of the specified range 
	 */
	public static int toInt255(double x)
	{
		long v = Math.round(255 * x);
		if(v < 0)
		{
			return 0;
		}
		else if(v > 255)
		{
			return 255;
		}
		else
		{
			return (int)v;
		}
	}
}
