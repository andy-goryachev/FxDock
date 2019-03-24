// Copyright © 2012-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


public class CancellableThread
	extends Thread
	implements Cancellable
{
	private volatile boolean cancelled;

	
	public CancellableThread()
	{
	}
	
	
	public CancellableThread(String name)
	{
		super(name);
	}
	
	
	public CancellableThread(Runnable target)
	{
		super(target);
	}
	
	
	public CancellableThread(Runnable target, String name)
	{
		super(target, name);
	}
	
	
	public synchronized void cancel()
	{
		cancelled = true;
		interrupt();
	}
	
	
	public synchronized boolean isCancelled()
	{
		return cancelled;
	}
	
	
	public boolean isRunning()
	{
		return !cancelled;
	}
	
	
	public void checkCancelled() throws CancelledException
	{
		if(cancelled)
		{
			throw new CancelledException();
		}
	}
	
	
	public static boolean isCurrentThreadCancelled()
	{
		return ((CancellableThread)Thread.currentThread()).isCancelled();
	}
}
