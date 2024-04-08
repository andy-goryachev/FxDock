// Copyright © 2018-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.util.Collection;


/**
 * Simple Circular Buffer.
 * - addition overwrites the oldest element when the buffer is full
 * - allows for insertion of an item before the first element, but only when the buffer is not full
 */
public class CircularBuffer<T>
{
	protected final Object[] items;
	private int size;
	private int first;
		
		
	public CircularBuffer(int capacity)
	{
		this.items = new Object[capacity];
	}
	
	
	protected int index(int ix)
	{
		return ix % items.length;
	}
	
	
	public int size()
	{
		return size;
	}
	
	
	public int getCapacity()
	{
		return items.length;
	}
	
	
	public void add(T item)
	{
		int ix = index(first + size);
		items[ix] = item;

		if(size < items.length)
		{
			size++;
		}
		else
		{
			first++;
		}
	}
	
	
	public void addAll(T ... adding)
	{
		for(int i=0; i<adding.length; i++)
		{
			add(adding[i]);
		}
	}
	
	
	public void addAll(Collection<T> adding)
	{
		for(T item: adding)
		{
			add(item);
		}
	}
	
	
	/** 
	 * inserts an item before the first item in the buffer.
	 * @throws ArrayIndexOutOfBoundsException when the buffer is full
	 */
	public void insertFirst(T item)
	{
		int capacity = getCapacity();
		if(size >= capacity)
		{
			throw new ArrayIndexOutOfBoundsException("buffer is full");
		}
		
		first = index(first + capacity - 1);
		items[first] = item;
		size++;
	}
	
	
	public T get(int index)
	{
		if(index < 0)
		{
			throw new ArrayIndexOutOfBoundsException(index);
		}
		else if(index >= size)
		{
			throw new ArrayIndexOutOfBoundsException(index);
		}
		
		int ix = index(first + index);
		return (T)items[ix];
	}
	
	
	public T removeFirst()
	{
		if(size == 0)
		{
			throw new ArrayIndexOutOfBoundsException(0);
		}
		
		T item = (T)items[first];
		first = index(first + 1);
		size--;
		
		return item;
	}
	
	
	public T removeLast()
	{
		if(size == 0)
		{
			throw new ArrayIndexOutOfBoundsException(0);
		}
		
		int ix = index(first + size - 1);
		T item = (T)items[ix];
		size--;
		
		return item;
	}
}
