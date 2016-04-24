// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;


/**
 * CssProperty.
 */
public class CssProperty
{
	private final String name;
	private final Object value;
	
	
	public CssProperty(String name, Object val)
	{
		this.name = name;
		this.value = val;
	}
	
	
	public String getName()
	{
		return name;
	}
	
	
	public Object getValue()
	{
		return value;
	}
	
	
	public String toString()
	{
		return getName() + ": " + getValue() + ";";
	}
}
