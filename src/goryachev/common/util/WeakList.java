// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.lang.ref.WeakReference;


public class WeakList<T>
{
	private CList<WeakReference<T>> list;
	
	
	public WeakList()
	{
		this(8);
	}
	
	
	public WeakList(int size)
	{
		list = new CList(size);
	}


	public synchronized CList<T> asList()
	{
		int sz = list.size();
		CList<T> rv = new CList(sz);
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


	public synchronized void add(T item)
	{
		list.add(new WeakReference(item));
	}
	
	
	public synchronized int size()
	{
		return list.size();
	}
	
	
	public synchronized void remove(T item)
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
}
