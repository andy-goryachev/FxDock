// Copyright © 2011-2024 Andy Goryachev <andy@goryachev.com>
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
	
	
	@Override
	public String getName()
	{
		return name;
	}
	
	
	@Override
	public String getStringValue()
	{
		return value;
	}
}
