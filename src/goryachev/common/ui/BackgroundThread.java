// Copyright (c) 2006-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.util.CKit;
import goryachev.common.util.CancellableThread;
import goryachev.common.util.Progress;
import java.awt.EventQueue;


// Simple class that provides background thread functionality and allows to manipulate
// swing components in EDT
// Simpler and lighter than Sun's SwingWorker
//
// process() - in a separate background thread, returns result of processing
// success() - in EDT thread
// error()   - in EDT thread
//
public abstract class BackgroundThread
	extends CancellableThread
{
	/** executed in the background thread */
	public abstract void process() throws Throwable;
	
	/** executed in EDT in case of success - when process() returns normally */
	public abstract void success();
	
	/** executed in EDT when process() throws an exception */ 
	public abstract void onError(Throwable e);
	
	/** overwrite to enable time estimate and progress report */
	public Progress getProgress() { return null; }
	
	
	//
	
	
	private long startTime;
	
	
	public BackgroundThread(String name, int priority)
	{
		setName(name);
		setPriority(priority);
	}
	

	public BackgroundThread(String name)
	{
		// truly background thread
		this(name, NORM_PRIORITY - 3);
	}
	
	
	public final void run()
	{
		try
		{
			synchronized(this)
			{
				startTime = System.currentTimeMillis();
			}
			
			// force a context switch
			CKit.sleep(10);

			// background thread body
			process();
			
			// update ui with the result
			EventQueue.invokeLater(new Runnable()
			{
				public void run()
				{
					success();
				}
			});
		}
		catch(final Throwable e)
		{
			EventQueue.invokeLater(new Runnable()
			{
				public void run()
				{
					onError(e);
				}
			});
		}
	}
	
	
	public synchronized long getStartTime()
	{
		return startTime;
	}
	
	
	/** sleeps, if necessary, to insure minimum delay from start */
	public void comfortSleep(int minimumTimeMilliseconds)
	{
		CKit.comfortSleep(startTime, minimumTimeMilliseconds);
	}
	
	
	/** sleeps, if necessary, to insure minimum delay from start */
	public void comfortSleep()
	{
		comfortSleep(400);
	}
}
