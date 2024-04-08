// Copyright © 2018-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.util.concurrent.ArrayBlockingQueue;


/**
 * Simple Synchronization Primitive.
 */
public class Mailbox<T>
{
	private final ArrayBlockingQueue<T> queue;
	
	
	public Mailbox()
	{
		queue = new ArrayBlockingQueue<T>(1);
	}
	
	
	public void put(T item)
	{
		queue.offer(item);
	}
	
	
	public T get()
	{
		try
		{
			return queue.take();
		}
		catch(Exception e)
		{
			return null;
		}
	}
}
