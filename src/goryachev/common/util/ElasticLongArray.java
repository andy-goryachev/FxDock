// Copyright © 2005-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


/** elastic long array buffer */ 
public class ElasticLongArray
{
	protected int size;
	protected long[] array;
	
	
	public ElasticLongArray()
	{
		this(128);
	}
	
	
	public ElasticLongArray(int capacity)
	{
		size = 0;
		array = new long[capacity];
	}
	
	
	public int size()
	{
		return size;
	}
	
	
	public long get(int index)
	{
//		try
//		{
			return array[index];
//		}
//		catch(ArrayIndexOutOfBoundsException e)
//		{
//			return 0;
//		}
	}
	
	
	public void add(long value)
	{
		prepareFor(size);
		array[size++] = value;
	}
	
	
	public void addAll(ElasticLongArray a)
	{
		if(a != null)
		{
			int sz = a.size();
			if(sz > 0)
			{
				prepareFor(size + sz);
				System.arraycopy(a.array, 0, array, size, sz);
				size += sz;
			}
		}
	}
	
	
	public void set(int index, long value)
	{
		prepareFor(index);
		array[index] = value;
		if(index >= size)
		{
			size = index + 1;
		}
	}
	
	
	public int indexOf(long value)
	{
		return indexOf(value, 0);
	}
	
	
	public int indexOf(long value, int fromIndex)
	{
		for(int i=fromIndex; i<size; i++)
		{
			if(array[i] == value)
			{
				return i;
			}
		}
		return -1;
	}


	public int lastIndexOf(long value)
	{
		return lastIndexOf(value, size - 1);
	}
	
	
	public int lastIndexOf(long value, int fromIndex)
	{
		for(int i=Math.min(size, fromIndex); i>=0; i--)
		{
			if(array[i] == value)
			{
				return i;
			}
		}
		return -1;
	}


	public void set(long[] d)
	{
		array = d.clone();
		size = d.length;
	}
	
	
	public void clear()
	{
		size = 0;
	}
	
	
	protected void prepareFor(int sz)
	{
		if(sz >= size)
		{
			if(sz >= array.length)
			{
				long[] a = new long[sz + Math.max(16, sz / 2)];
				System.arraycopy(array, 0, a, 0, size);
				array = a;
			}
			else
			{
				// array unchanged, clear possible garbage [size...n-1]
				for(int i=size; i<sz; i++)
				{
					array[i] = 0;
				}
			}
		}
	}
	

	public void trim(int sz)
	{
		if(sz < size)
		{
			size = sz;
		}
	}


	public void insert(int index, long value)
	{
		prepareFor(size);
		if(size > index)
		{
			System.arraycopy(array, index, array, index + 1, size - index);
		}
		else
		{
			size = index + 1;
		}
		array[index] = value;
	}


	public long[] toArray()
	{
		long[] rv = new long[size];
		System.arraycopy(array, 0, rv, 0, size);
		return rv;
	}
}
