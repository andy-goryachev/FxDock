// Copyright © 2011-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


public class ElasticArray<T>
{
	private CList<T> list = new CList();
	private int size;
	
	
	public ElasticArray()
	{
	}
	
	
	public void add(T item)
	{
		add(size, item);
	}
	
	
	public void add(int index, T item)
	{
		while(list.size() <= index)
		{
			list.add(null);
		}
		
		list.add(index, item);
		size++;
	}
	
	
	public void set(int index, T item)
	{
		while(list.size() <= index)
		{
			list.add(null);
		}
		
		list.set(index, item);
		if(size <= index)
		{
			size = index + 1;
		}
	}
	
	
	public T get(int index)
	{
		if(index >= 0)
		{
			if(index < list.size())
			{
				return list.get(index);
			}
		}
		return null;
	}
	
	
	public int size()
	{
		return size;
	}
}
