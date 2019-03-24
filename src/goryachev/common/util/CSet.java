// Copyright © 2015-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.util.Collection;
import java.util.HashSet;


/** HashSet with additional methods */
public class CSet<T>
	extends HashSet<T>
{
	private static final int DEFAULT_SIZE = 64;

	
	public CSet(int size)
	{
		super(size);
	}
	
	
	public CSet()
	{
		super(DEFAULT_SIZE);
	}
	
	
	public CSet(Collection<T> items)
	{
		super(items);
	}
	
	
	public CSet(T[] items)
	{
		addAll(items);
	}
	
	
	public void addAll(T[] items)
	{
		if(items != null)
		{
			int sz = items.length;
			for(int i=0; i<sz; i++)
			{
				add(items[i]);
			}
		}
	}
	
	
	public void removeAll(T[] items)
	{
		if(items != null)
		{
			int sz = items.length;
			for(int i=0; i<sz; i++)
			{
				remove(items[i]);
			}
		}
	}


	public boolean addAll(Collection<? extends T> items)
	{
		if(items != null)
		{
			return super.addAll(items);
		}
		return false;
	}


	public CList<T> asList()
	{
		return new CList<>(this);
	}
}
