// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;


/**
 * Width x Height
 */
public class FxSize
{
	private double width;
	private double height;
	
	
	public FxSize(double width, double height)
	{
		this.width = width;
		this.height = height;
	}
	
	
	public FxSize()
	{
	}
	
	
	public void setWidth(double w)
	{
		width = w;
	}
	
	
	public void setHeight(double h)
	{
		height = h;
	}
	
	
	public double getWidth()
	{
		return width;
	}
	
	
	public double getHeight()
	{
		return height;
	}
	
	
	public boolean equals(Object x)
	{
		if(x == this)
		{
			return true;
		}
		else if(x instanceof FxSize)
		{
			FxSize s = (FxSize)x;
			return ((width == s.width) && (height == s.height));
		}
		else
		{
			return false;
		}
	}
	
	
	public int hashCode()
	{
		return FxSize.class.hashCode() ^ Double.hashCode(width) ^ Double.hashCode(height);
	}
}
