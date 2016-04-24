// Copyright (c) 2006-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


public class Dim
{
	public int width;
	public int height;
	
	
	public Dim(int w, int h)
	{
		width = w;
		height = h;
	}
	
	
	public int getWidth()
	{
		return width;
	}
	
	
	public int getHeight()
	{
		return height;
	}
	
	
	public String toString()
	{
		return "[" + width + "x" + height + "]";
	}
}
