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


	@Override
	public synchronized CList<T> asList()
	{
		return super.asList();
	}
	
	
	@Override
	public synchronized T get(int ix)
	{
		return super.get(ix);
	}


	@Override
	public synchronized void add(T item)
	{
		super.add(item);
	}
	
	
	@Override
	public synchronized void add(int index, T item)
	{
		super.add(index, item);
	}
	
	
	@Override
	public synchronized int size()
	{
		return super.size();
	}
	
	
	@Override
	public synchronized void remove(T item)
	{
		super.remove(item);
	}
	
	
	@Override
	public synchronized T remove(int ix)
	{
		return super.remove(ix);
	}
	
	
	@Override
	public synchronized int indexOf(T item)
	{
		return super.indexOf(item);
	}
	
	
	@Override
	public synchronized void forEach(Consumer<T> client)
	{
		super.forEach(client);
	}
}
