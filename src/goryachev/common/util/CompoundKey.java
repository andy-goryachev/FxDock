// Copyright © 2014-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


public class CompoundKey
{
	private final Object[] keys;
	private int hash;
	
	
	public CompoundKey(Object a, Object b)
	{
		this(new Object[] { a, b });
	}
	
	
	public CompoundKey(Object a, Object b, Object c)
	{
		this(new Object[] { a, b, c });
	}
	
	
	public CompoundKey(Object a, Object b, Object c, Object d)
	{
		this(new Object[] { a, b, c, d });
	}
	
	
	public CompoundKey(Object a, Object b, Object c, Object d, Object e)
	{
		this(new Object[] { a, b, c, d, e });
	}
	
	
	public CompoundKey(Object[] keys)
	{
		this.keys = keys;
	}
	
	
	public boolean equals(Object x)
	{
		if(x == this)
		{
			return true;
		}
		else if(x instanceof CompoundKey)
		{
			return CKit.equals(keys, ((CompoundKey)x).keys);
		}
		else
		{
			return false;
		}
	}


	public int hashCode()
	{
		if(hash == 0)
		{
			hash = FH.hash(keys);
		}
		return hash;
	}
}
