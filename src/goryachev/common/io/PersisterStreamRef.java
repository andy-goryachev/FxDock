// Copyright © 2013-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.common.io;


/** 
 * Internal reference class used in serialization of lists, maps and tables
 * to deduplicate strings and other large values.
 * Should not be seen outside of this package.
 */
public class PersisterStreamRef
{
	private final int index;

	
	public PersisterStreamRef(int index)
	{
		this.index = index;
	}
	
	
	public int getIndex()
	{
		return index;
	}
}
