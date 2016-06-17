// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;


/**
 * Fx Icon Builder.
 */
public class FxIconBuilder
{
	private final double width;
	private final double height;
	private double xcenter;
	private double ycenter;
	
	
	public FxIconBuilder(double width, double height, double xcenter, double ycenter)
	{
		this(width, height);
		setCenter(xcenter, ycenter);
	}


	public FxIconBuilder(double width, double height)
	{
		this.width = width;
		this.height = height;
	}
	
	
	public FxIconBuilder(double size)
	{
		this(size, size);
	}
	
	
	private void setCenter(double xcenter, double ycenter)
	{
	}
}
