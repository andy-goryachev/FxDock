// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import java.awt.Color;


// TODO Theme.isDark()
public class Gray
	extends Color
{
	public Gray(int color)
	{
		super(color, color, color);
	}
	
	
	public Gray(int color, int alpha)
	{
		super(color, color, color, alpha);
	}
	
	
	public Gray(Gray c)
	{
		super(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
	}
	

	/** returns 5:6:5-safe gray color */
	public static Gray safeGray(int n)
	{
		return new Gray(n &= 0x00f8);
	}
}
