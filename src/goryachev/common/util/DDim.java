// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.awt.geom.Dimension2D;


public class DDim
	extends Dimension2D
{
	private double width;
	private double height;
	
	
	public DDim()
	{
	}
	
	
	public DDim(double w, double h)
	{
		setSize(w, h);
	}
	
	
	public double getWidth()
	{
		return width;
	}


	public double getHeight()
	{
		return height;
	}


	public void setSize(double w, double h)
	{
		this.width = w;
		this.height = h;
	}
}
