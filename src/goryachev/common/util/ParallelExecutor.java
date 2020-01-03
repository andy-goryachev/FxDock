// Copyright © 2012-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class ParallelExecutor
	implements ThreadFactory
{
	private String name;
	private AtomicInteger number = new AtomicInteger();
	private ThreadPoolExecutor exec;
	private boolean closed;
	
	
	public ParallelExecutor(String name)
	{
		this(name, 60);
	}
	
	
	public ParallelExecutor(String name, int keepAliveTimeSeconds)
	{
		this.name = name;
		
		exec = new ThreadPoolExecutor(0, Integer.MAX_VALUE, keepAliveTimeSeconds, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), this);
		exec.allowCoreThreadTimeOut(true);
	}
	
	
	public Thread newThread(Runnable r)
	{
		Thread t = new Thread(r, name + "." + number.getAndIncrement());
		t.setDaemon(true);
		return t;
	}
	
	
	public void setKeepAliveTime(long time, TimeUnit unit)
	{
		exec.setKeepAliveTime(time, unit);
	}
	
	
	public synchronized void shutdown()
	{
		if(!closed)
		{
			exec.shutdown();
			
			try
			{
				// why wait?
				exec.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
			}
			catch(Exception e)
			{
				Log.ex(e);
			}
			closed = true;
		}
	}
	
	
	protected synchronized boolean isClosed()
	{
		// why is this needed?
		return closed;
	}
	
	
	public void submit(Runnable r)
	{
		exec.execute(r);
	}
}
