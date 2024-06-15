// Copyright © 2016-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;


/**
 * Css Pseudo class.
 */
public class CssPseudo
{
	private final String name;
	
	
	public CssPseudo(String name)
	{
		this.name = name;
	}
	
	
	public String getName()
	{
		return name;
	}
	
	
	@Override
	public String toString()
	{
		return getName();
	}
}
