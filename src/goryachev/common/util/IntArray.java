// Copyright (c) 2005-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;


public class IntArray
	implements Externalizable
{
	private int size;
	private int[] array;
	private static final long serialVersionUID = 2L;
	
	
	
	public IntArray()
	{
		this(128);
	}
	
	
	public IntArray(int capacity)
	{
		size = 0;
		array = new int[capacity];
	}
	
	
	public int size()
	{
		return size;
	}
	
	
	public int get(int n)
	{
		try
		{
			return array[n];
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			return 0;
		}
	}
	
	
	public void add(int d)
	{
		prepareFor(size);
		array[size++] = d;
	}
	
	
	public void set(int n, int d)
	{
		prepareFor(n);
		array[n] = d;
		if(n >= size)
		{
			size = n + 1;
		}
	}
	
	
	public int indexOf(int n)
	{
		for(int i=0; i<size; i++)
		{
			if(array[i] == n)
			{
				return i;
			}
		}
		return -1;
	}
	
	
	public void set(int[] d)
	{
		array = d;
		size = d.length;
	}
	
	
	public void clear()
	{
		size = 0;
	}
	
	
	protected void prepareFor(int n)
	{
		if(n >= size)
		{
			if(n >= array.length)
			{
				int[] a = new int[n + Math.max(16,n/2)];
				System.arraycopy(array,0,a,0,size);
				array = a;				
			}
			else
			{
				// array unchanged, clear possible garbage [size...n-1]
				for(int i=size; i<n; i++)
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


	public void insert(int n, int d)
	{
		prepareFor(size);
		if(size > n)
		{
			System.arraycopy(array,n,array,n+1,size-n);
		}
		else
		{
			size = n + 1;
		}
		array[n] = d;
	}


	public void remove(int n)
	{
		int tomove = size - n - 1;
		if(tomove > 0)
		{
			System.arraycopy(array,n+1,array,n,tomove);
		}
		array[--size] = 0;
	}
	
	
	public int[] toArray()
	{
		int[] rv = new int[size];
		System.arraycopy(array,0,rv,0,size);
		return rv;
	}
	
	
	
	public void readExternal(ObjectInput in) throws ClassNotFoundException, IOException
	{
		size = in.readInt();
		array = new int[size];
		for(int i=0; i<size; i++)
		{
			array[i] = in.readInt();
		}
	}
	
	
	public void writeExternal(ObjectOutput out) throws IOException
	{
		out.writeInt(size);
		for(int i=0; i<size; i++)
		{
			out.writeInt(array[i]);
		}
	}
}
