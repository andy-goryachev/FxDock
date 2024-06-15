// Copyright © 2005-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.nio.charset.Charset;


/** elastic byte array buffer */ 
public class ElasticByteArray
{
	protected int size;
	protected byte[] array;
	
	
	public ElasticByteArray()
	{
		this(128);
	}
	
	
	public ElasticByteArray(int capacity)
	{
		size = 0;
		array = new byte[capacity];
	}
	
	
	public int size()
	{
		return size;
	}
	
	
	public int get(int index)
	{
		try
		{
			return array[index];
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			return 0;
		}
	}
	
	
	public void add(int value)
	{
		prepareFor(size);
		array[size++] = (byte)value;
	}
	
	
	public void set(int index, int value)
	{
		prepareFor(index);
		array[index] = (byte)value;
		if(index >= size)
		{
			size = index + 1;
		}
	}
	
	
	public int indexOf(int value)
	{
		return indexOf(value, 0);
	}
	
	
	public int indexOf(int value, int fromIndex)
	{
		byte v = (byte)value;
		for(int i=fromIndex; i<size; i++)
		{
			if(array[i] == v)
			{
				return i;
			}
		}
		return -1;
	}


	public int lastIndexOf(int value)
	{
		return lastIndexOf(value, size - 1);
	}
	
	
	public int lastIndexOf(int value, int fromIndex)
	{
		byte v = (byte)value;
		for(int i=Math.min(size, fromIndex); i>=0; i--)
		{
			if(array[i] == v)
			{
				return i;
			}
		}
		return -1;
	}

	
	
	public void set(byte[] d)
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
				byte[] a = new byte[sz + Math.max(16, sz / 2)];
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


	public void insert(int index, int value)
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
		array[index] = (byte)value;
	}


	public byte[] toArray()
	{
		byte[] rv = new byte[size];
		System.arraycopy(array, 0, rv, 0, size);
		return rv;
	}
	
	
	@Override
	public String toString()
	{
		return super.toString();
	}
	
	
	public String toString(Charset cs)
	{
		return new String(array, 0, size, cs);
	}
	
	
	public String toString(int start, int len, Charset cs)
	{
		return new String(array, start, len, cs);
	}
}
