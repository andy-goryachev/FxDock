// Copyright © 2012-2021 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


public class NamedObjects
	extends CMap<String,Object>
{
	public NamedObjects(int size)
	{
		super(size);	
	}
	
	
	public NamedObjects(NamedObjects c)
	{
		super(c);	
	}
	
	
	public NamedObjects()
	{
	}
}
