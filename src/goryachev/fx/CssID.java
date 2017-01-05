// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;


/**
 * Css ID
 */
public class CssID
{
	private final String id;
	
	
	public CssID(String id)
	{
		this.id = id;
	}
	
	
	public String getID()
	{
		return id;
	}
	
	
	public String toString()
	{
		return getID();
	}
}
