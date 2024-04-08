// Copyright © 2012-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.util.function.Consumer;


/**
 * Synchronized List of WeakListeners.
 */
public class WeakVector<T>
	extends WeakList<T>
{
	public WeakVector()
	{
	}
	
	
	public WeakVector(int size)
	{
		super(size);
	}


	public synchronized CList<T> asList()
	{
		return super.asList();
	}
	
	
	public synchronized T get(int ix)
	{
		return super.get(ix);
	}


	public synchronized void add(T item)
	{
		super.add(item);
	}
	
	
	public synchronized void add(int index, T item)
	{
		super.add(index, item);
	}
	
	
	public synchronized int size()
	{
		return super.size();
	}
	
	
	public synchronized void remove(T item)
	{
		super.remove(item);
	}
	
	
	public synchronized T remove(int ix)
	{
		return super.remove(ix);
	}
	
	
	public synchronized int indexOf(T item)
	{
		return super.indexOf(item);
	}
	
	
	public synchronized void forEach(Consumer<T> client)
	{
		super.forEach(client);
	}
}
