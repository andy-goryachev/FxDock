// Copyright © 2011-2025 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


public class Obj
{
	private final String name;
	
	
	public Obj(String name)
	{
		this.name = name;
	}
	
	
	@Override
	public String toString()
	{
		return name;
	}
	
	
	public String getName()
	{
		return name;
	}
}
