// Copyright © 2011-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


public class NamedEntry
	implements HasName, HasStringValue
{
	private String name;
	private String value;
	
	
	public NamedEntry(String name, String value)
	{
		this.name = name;
		this.value = value;
	}
	
	
	public String getName()
	{
		return name;
	}
	
	
	public String getStringValue()
	{
		return value;
	}
}
