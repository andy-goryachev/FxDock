// Copyright © 2011-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


public class Obj
{
	private String name;
	
	
	public Obj(String name)
	{
		this.name = name;
	}
	
	
	public String toString()
	{
		return getName();
	}
	
	
	public String getName()
	{
		return name;
	}
}
