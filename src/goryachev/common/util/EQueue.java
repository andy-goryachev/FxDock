// Copyright © 2004-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


// A simple queue
public class EQueue<T>
{
	protected CList<T> queue;
	
	
	public EQueue(int size)
	{
		queue = new CList<T>(size);
	}
	
	
	public EQueue()
	{
		this(16);
	}


	public synchronized void put(T d)
	{
		queue.add(d);
		notifyAll();
	}
	
	
	public synchronized void putToFront(T d)
	{
		queue.add(0, d);
		notifyAll();
	}


	public synchronized T get()
	{
		while(queue.isEmpty())
		{
			try
			{
				wait();
			}
			catch(InterruptedException e)
			{ }
		}
		return queue.remove(0);
	}


	public synchronized boolean isEmpty()
	{
		return queue.isEmpty();
	}
	
	
	public synchronized void delete(T d)
	{
		queue.remove(d);
	}
	
	
	public synchronized void clear()
	{
		queue.clear();
		notify();
	}
}
