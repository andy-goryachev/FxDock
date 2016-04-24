// Copyright (c) 2007-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.util.ArrayList;
import java.util.Collection;


public class CList<T>
	extends ArrayList<T>
{
	public CList(int initialCapacity)
	{
		super(initialCapacity);
	}
	
	
	public CList()
	{
	}
	
	
	public CList(Collection<? extends T> c)
	{
		super(c == null ? 0 : c.size());
		if(c != null)
		{
			addAll(c);
		}
	}
	
	
	public CList(T[] a)
	{
		super(a == null ? 0 : a.length);
		if(a != null)
		{
			addAll(a);
		}
	}
	
	
	public CList(T item)
	{
		if(item != null)
		{
			add(item);
		}
	}
	
	
	// why ArrayList declares this method as protected is unclear
	public void removeRange(int fromInclusive, int toExclusive)
	{
		super.removeRange(fromInclusive,toExclusive);
	}
	
	
	public void addNonNull(T t)
	{
		if(t != null)
		{
			add(t);
		}
	}
	
	
	public Object clone()
	{
		return copyCList();
	}
	
	
	public CList<T> copyCList()
	{
		return new CList<T>(this);
	}
	
	
	public boolean equals(Object x)
	{
		if(x == this)
		{
			return true;
		}
		else if(x instanceof CList)
		{
			CList o = (CList)x;
			// adding check for size makes it slightly faster than the superclass implementation
			// in case of different lists
			if(size() == o.size())
			{
				int sz = size();
				for(int i=0; i<sz; i++)
				{
					if(CKit.notEquals(get(i), o.get(i)))
					{
						return false;
					}
				}
				return true;
			}
		}

		return false;
	}
	
	
	public int hashCode()
	{
		return CList.class.hashCode() ^ super.hashCode();
	}
	
	
	public void setAll(T[] items)
	{
		clear();
		
		if(items != null)
		{
			addAll(items);
		}
	}
	
	
	public void setAll(Collection<T> items)
	{
		clear();
		
		if(items != null)
		{
			addAll(items);
		}
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
	
	
	public void addAll(int startIndex, T[] items)
	{
		if(items != null)
		{
			int sz = items.length;
			for(int i=0; i<sz; i++)
			{
				add(startIndex, items[i]);
				startIndex++;
			}
		}
	}
	
	
	public static CList parse(Object x)
	{
		if(x instanceof CList)
		{
			return (CList)x;
		}
		return null;
	}
}
