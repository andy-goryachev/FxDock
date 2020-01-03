// Copyright © 2012-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.log;
import java.util.concurrent.LinkedBlockingQueue;


public class AsyncWriter
	implements LogWriterPrivate, Runnable
{
	private final LogWriter writer;
	private final LinkedBlockingQueue<LogEntry> queue = new LinkedBlockingQueue<>();
	private final Thread thread;
	private volatile boolean running = true;


	public AsyncWriter(LogWriter wr)
	{
		this.writer = wr;
		
		thread = new Thread(this, wr.toString());
		thread.setDaemon(true);
		thread.start();
	}
	
	
	public void run()
	{
		while(running || !queue.isEmpty())
		{
			try
			{
				LogEntry en = queue.take();
				writer.writePrivate(en);
			}
			catch(Exception e)
			{
				// FIX where?
				e.printStackTrace();
			}
		}
	}


	public void writePrivate(LogEntry en)
	{
		try
		{
			queue.add(en);
		}
		catch(Exception e)
		{
			// FIX where?
			e.printStackTrace();
		}
	}


	public void close()
	{
		running = false;
	}
}
