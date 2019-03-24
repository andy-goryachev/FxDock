// Copyright © 2016-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.util.FH;


/**
 * CSS Style.
 */
public class CssStyle
{
	private final String name;
	
	
	public CssStyle(String name)
	{
		this.name = name;
	}
	
	
	public boolean equals(Object x)
	{
		if(x == this)
		{
			return true;
		}
		else if(x instanceof CssStyle)
		{
			CssStyle z = (CssStyle)x;
			return name.equals(z.name);
		}
		else
		{
			return false;
		}
	}
	
	
	public int hashCode()
	{
		int h = FH.hash(CssStyle.class);
		h = FH.hash(h, name);
		return h;
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
