// Copyright © 2013-2021 Andy Goryachev <andy@goryachev.com>
package goryachev.common.io;
import goryachev.common.util.CList;
import goryachev.common.util.CMap;


public class PersisterStreamCache
{
	private final CMap<Object,Integer> cache;
	private final CList<Object> read;
	private int index;

	
	public PersisterStreamCache(int size)
	{
		cache = new CMap(size);
		read = new CList(size);
	}
	
	
	public PersisterStreamCache()
	{
		this(64);
	}
	
	
	/** pass data through this method when reading from a stream.  make sure to call it for EACH object */
	public Object read(Object x)
	{
		if(x instanceof PersisterStreamRef)
		{
			x = read.get(((PersisterStreamRef)x).getIndex());
		}
		
		read.add(x);
		return x;
	}
	
	
	/** use this method when writing to a stream.  make sure to call it for EACH object */
	public Object write(Object x)
	{
		if(isAcceptable(x))
		{
			Integer n = cache.get(x);
			if(n != null)
			{
				x = new PersisterStreamRef(n);
			}
			else
			{
				cache.put(x, index);
			}
		}
		index++;
		return x;
	}
	
	
	protected boolean isAcceptable(Object x)
	{
		if(x != null)
		{
			Class c = x.getClass();
			if(c == Long.class) return true;
			else if(c == Double.class) return true;
			else if(c == String.class) return true;
		}
		return false;
	}
}
