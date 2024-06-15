// Copyright © 2007-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


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
	
	
	public CList(Iterator<? extends T> it)
	{
		if(it != null)
		{
			while(it.hasNext())
			{
				add(it.next());
			}
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
	@Override
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
	
	
	@Override
	public Object clone()
	{
		return copyCList();
	}
	
	
	public CList<T> copyCList()
	{
		return new CList<T>(this);
	}
	
	
	@Override
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
	
	
	@Override
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
	
	
	/** remove elements from 'fromInclusive' onwards, silently ignoring out-of-bounds argument */
	public void prune(int fromInclusive)
	{
		if(fromInclusive >= 0)
		{
			int ct = size() - fromInclusive;
			if(ct > 0)
			{
				removeRange(fromInclusive, size());
			}
		}
	}
	
	
	/** 
	 * returns last element of null if the list is empty.  
	 * keep in mind this method does not distingush between two scenarios:
	 * when the list is empty and when the last element is null.
	 */
	@Override
	public T getLast()
	{
		if(size() > 0)
		{
			return get(size() - 1);
		}
		return null;
	}
	
	
	/** removes last element */
	@Override
	public T removeLast()
	{
		int ix = size() - 1;
		if(ix >= 0)
		{
			return remove(ix);
		}
		return null;
	}
	
	
	public static CList parse(Object x)
	{
		if(x instanceof CList)
		{
			return (CList)x;
		}
		return null;
	}


	public boolean isValidIndex(int ix)
	{
		return (ix >= 0) && (ix < size());
	}
	
	
	public void prepareFor(int ix)
	{
		int sz = ix + 1;
		while(size() < sz)
		{
			super.add(null);
		}
	}
	
	
	/** 
	 * safely inserts an item into the list, adding an item to the end of the list when index is >= size, 
	 * or inserting at the position 0 when index is <= 0
	 */
	public void insert(int index, T item)
	{
		if(index < 0)
		{
			index = 0;
		}
		
		if(index >= size())
		{
			add(item);
		}
		else
		{
			add(index, item);
		}
	}


	public static <V> CList<V> of(V ... a)
	{
		return new CList(a);
	}
	
	
	public static <V> CList<V> copy(Collection<V> items)
	{
		if(items == null)
		{
			return null;
		}
		
		int sz = items.size();
		CList<V> rv = new CList(sz);
		rv.addAll(items);
		return rv;
	}
}
