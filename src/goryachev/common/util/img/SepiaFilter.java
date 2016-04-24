// Copyright (c) 2015-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.img;
import goryachev.common.util.img.jhlabs.PointFilter;


// http://www.coderanch.com/t/616259/GUI/java/Sepia-Filter-built-JHLabs-library
public class SepiaFilter
    extends PointFilter
{
	private int sepiaIntensity;


	public SepiaFilter()
	{
		this(20);
	}


	public SepiaFilter(int sepiaIntensity)
	{
		this.sepiaIntensity = sepiaIntensity;
	}


	public void setSepiaIntensity(int sepiaIntensity)
	{
		this.sepiaIntensity = sepiaIntensity;
	}


	public int getSepiaIntensity()
	{
		return sepiaIntensity;
	}


	public int filterRGB(int x, int y, int rgb)
	{
		int sepiaDepth = 20;
		
		int r = (rgb >> 16) & 0xff;
		int g = (rgb >>  8) & 0xff;
		int b = (rgb & 0xff);

		int gry = (r + g + b) / 3;
		r = g = b = gry;
		r = r + (sepiaDepth * 2);
		g = g + sepiaDepth;

		if(r > 255)
		{
			r = 255;
		}
		
		if(g > 255)
		{
			g = 255;
		}
		
		if(b > 255)
		{
			b = 255;
		}

		// darken blue color to increase sepia effect
		// or multiply?
		b -= sepiaIntensity;

		// normalize if out of bounds
		if(b < 0)
		{
			b = 0;
		}
		
		if(b > 255)
		{
			b = 255;
		}
		
		return (r << 16) | (g << 8) | b;
	}
}