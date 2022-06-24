// Copyright © 2006-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.log.Log;
import goryachev.common.util.CKit;
import goryachev.common.util.Progress;
import javafx.application.Platform;


/**
 * A cancellable thread that runs some computation in background, 
 * then updates the UI in an FX thread.
 */
public abstract class FxThread
	extends Thread
{
	/** executed in a background thread */
	protected abstract void process() throws Throwable;
	
	/** Invoked in an FX thread when the background process ends successfully or with an exception. */
	protected void onProcessEnd() { }
	
	/** executed in an FX thread when process() returns normally */
	protected abstract void processSuccess();
	
	/** 
	 * executed in an FX thread when process() throws an Throwable.
	 * The default implementation simply logs the exception. 
	 */ 
	protected void processError(Throwable e) { log.error(e); }
	
	/** overwrite to enable time estimate and progress report */
	public Progress getProgress() { return null; }
	
	
	//
	
	
	protected static final Log log = Log.get("FxThread");
	private long startTime;
	
	
	public FxThread(String name, int priority)
	{
		setName(name);
		setPriority(priority);
	}
	

	public FxThread(String name)
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
			Platform.runLater(() -> 
			{
				try
				{
					onProcessEnd();
				}
				catch(Throwable e)
				{
					log.error(e);
				}
				
				processSuccess();	
			});
		}
		catch(Throwable err)
		{
			Platform.runLater(() -> 
			{
				try
				{
					onProcessEnd();
				}
				catch(Throwable e)
				{
					log.error(e);
				}
				
				processError(err);
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
