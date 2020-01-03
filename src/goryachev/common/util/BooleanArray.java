// Copyright © 2006-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;


public class BooleanArray
	implements Externalizable
{
	private int size;
	private boolean[] array;
	private static final long serialVersionUID = 1L;
	
	
	public BooleanArray()
	{
		this(32);
	}
	
	
	public BooleanArray(int capacity)
	{
		size = 0;
		array = new boolean[capacity];
	}
	
	
	public int size()
	{
		return size;
	}
	
	
	public boolean get(int n)
	{
		try
		{
			return array[n];
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			return false;
		}
	}
	
	
	public void add(boolean d)
	{
		prepareFor(size);
		array[size++] = d;
	}
	
	
	public void set(int n, boolean d)
	{
		prepareFor(n);
		array[n] = d;
		if(n >= size)
		{
			size = n + 1;
		}
	}
	
	
	public void set(boolean[] d)
	{
		array = d;
		size = d.length;
	}
	
	
	public void clear()
	{
		size = 0;
	}

	
	public void removeAll()
	{
		size = 0;
	}
	
	
	protected void prepareFor(int n)
	{
		if(n >= size)
		{
			if(n >= array.length)
			{
				boolean[] a = new boolean[n + Math.max(16,n/2)];
				System.arraycopy(array,0,a,0,size);
				array = a;				
			}
			else
			{
				// array unchanged, clear possible garbage [size...n-1]
				for(int i=size; i<n; i++)
				{
					array[i] = false;
				}
			}
		}
	}
	
	
	public void readExternal(ObjectInput in) throws ClassNotFoundException, IOException
	{
		size = in.readInt();
		array = (boolean[])in.readObject();
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


	public void insert(int n, boolean d)
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
		array[--size] = false;
	}
	
	
	public boolean[] toArray()
	{
		boolean[] rv = new boolean[size];
		System.arraycopy(array,0,rv,0,size);
		return rv;
	}


	// delete range (start...end-1) inclusive
	public void removeRange(int start, int end)
	{
		if(start < end)
		{
			System.arraycopy(array,end,array,start,size-end);
			size -= (end-start);
		}
	}
}
