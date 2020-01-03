// Copyright © 2005-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;


public class LongArray
	implements Externalizable
{
	private int size;
	private long[] array;
	private static final long serialVersionUID = 1L;
	
	
	
	public LongArray()
	{
		this(128);
	}
	
	
	public LongArray(int capacity)
	{
		size = 0;
		array = new long[capacity];
	}
	
	
	public LongArray(long[] a)
	{
		size = a.length;
		array = a;
	}


	public int size()
	{
		return size;
	}
	
	
	public long get(int n)
	{
		if(n >= size)
		{
			throw new ArrayIndexOutOfBoundsException(n);
		}
		return array[n];
	}
	
	
	public void add(long d)
	{
		prepareFor(size);
		array[size++] = d;
	}
	
	
	public void set(int n, long d)
	{
		prepareFor(n);
		array[n] = d;
		if(n >= size)
		{
			size = n + 1;
		}
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
				long[] a = new long[n + Math.max(16,n/2)];
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
	
	
	public void readExternal(ObjectInput in) throws ClassNotFoundException, IOException
	{
		size = in.readInt();
		array = (long[])in.readObject();
	}
	
	
	public void writeExternal(ObjectOutput out) throws IOException
	{
		out.writeInt(size);
		out.writeObject(array);
	}


	public void trim(int sz)
	{
		if(sz < size)
		{
			size = sz;
		}
	}


	public void insert(int n, long d)
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
	
	
	public long[] toArray()
	{
		long[] rv = new long[size];
		System.arraycopy(array,0,rv,0,size);
		return rv;
	}


	public long[] getArray()
	{
		return array;
	}
}
