// Copyright © 2012-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.lang.ref.WeakReference;


/**
 * Unsynchronized List of WeakListeners.
 */
public class WeakList<T>
{
	private CList<WeakReference<T>> list;
	
	
	public WeakList()
	{
		this(8);
	}
	
	
	public WeakList(int size)
	{
		list = new CList<>(size);
	}
	
	
	public void gc()
	{
		int sz = list.size();
		for(int i=sz-1; i>=0; i--)
		{
			WeakReference<T> ref = list.get(i);
			T item = ref.get();
			if(item == null)
			{
				list.remove(i);
			}
		}
	}


	public CList<T> asList()
	{
		int sz = list.size();
		CList<T> rv = new CList<>(sz);
		for(int i=sz-1; i>=0; i--)
		{
			WeakReference<T> ref = list.get(i);
			T item = ref.get();
			if(item == null)
			{
				list.remove(i);
			}
			else
			{
				rv.add(item);
			}
		}
		return rv;
	}
	
	
	public T get(int ix)
	{
		return list.get(ix).get();
	}


	public void add(T item)
	{
		list.add(new WeakReference<>(item));
	}
	
	
	public void add(int index, T item)
	{
		list.add(index, new WeakReference<>(item));
	}
	
	
	public int size()
	{
		return list.size();
	}
	
	
	public void remove(T item)
	{
		int sz = list.size();
		for(int i=sz-1; i>=0; i--)
		{
			WeakReference<T> ref = list.get(i);
			T x = ref.get();
			if(x == null)
			{
				list.remove(i);
			}
			else if(item == x)
			{
				list.remove(i);
			}
		}
	}
	
	
	public void remove(int ix)
	{
		list.remove(ix);
	}
	
	
	public void clear()
	{
		list.clear();
	}
	
	
	public WeakReference<T> getRef(int ix)
	{
		return list.get(ix);
	}
}
