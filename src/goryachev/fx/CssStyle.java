// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;


/**
 * CssStyle.
 */
public class CssStyle
{
	private final String name;
	
	
	public CssStyle(String name)
	{
		this.name = name;
	}
	
	
	public String getName()
	{
		return name;
	}
	
	
	public String toString()
	{
		return getName();
	}
}
