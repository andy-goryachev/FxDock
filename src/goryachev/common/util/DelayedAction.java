// Copyright © 2019-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import goryachev.common.log.Log;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;


/**
 * Delayed Action.
 * 
 * A special TimerTask equivalent that uses a single shared daemon Timer to avoid
 * creating multiple threads.  This class eats all exceptions to avoid killing 
 * the timer thread.
 * 
 * Unlike TimerTask, this class can be reused by calling schedule() multiple
 * times.
 */
public class DelayedAction
{
	protected static final Log log = Log.get("DelayedAction");
	private static final int WARN_THRESHOLD = 500;
	private final String name;
	private final Runnable action;
	private TimerTask task;
	private Consumer<Throwable> errorHandler;
	private static final Timer timer = new Timer("DelayedAction.timer", true);
	
	
	public DelayedAction(String name, Runnable action)
	{
		if(action == null)
		{
			throw new NullPointerException("action");
		}
		this.name = name;
		this.action = action;
	}
	
	
	public void setOnError(Consumer<Throwable> handler)
	{
		errorHandler = handler;
	}
	
	
	public void schedule(long delay)
	{
		timer.schedule(task(), delay);
	}
	
	
	public void schedule(long delay, long period)
	{
		timer.schedule(task(), delay, period);
	}
	
	
	protected synchronized TimerTask task()
	{
		if(task != null)
		{
			task.cancel();
		}
		
		task = new TimerTask()
		{
			public void run()
			{
				processTask();
			}
			
			
			public String toString()
			{
				return getName();
			}
		};
		
		return task;
	}
	
	
	public String toString()
	{
		return getName();
	}
	
	
	public String getName()
	{
		return name;
	}


	protected final void processTask()
	{
		long start = System.currentTimeMillis();
		
		try
		{
			action.run();
		}
		catch(Throwable e)
		{
			try
			{
				Consumer<Throwable> eh = errorHandler;
				if(eh == null)
				{
					log.error(e);
				}
				else
				{
					eh.accept(e);
				}
			}
			catch(Throwable err)
			{
				log.error(err);
			}
		}
		finally
		{
			long elapsed = System.currentTimeMillis() - start;
			if(elapsed > WARN_THRESHOLD)
			{
				log.error("taking too long to run: " + name);
			}
		}
	}
}
