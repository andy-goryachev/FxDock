// Copyright © 2012-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import goryachev.common.log.Log;
import goryachev.common.util.platform.ApplicationSupport;
import java.util.Collection;
import java.util.concurrent.TimeUnit;


public abstract class CJob
	implements Runnable
{
	protected abstract void process() throws Exception;
	
	/** called in a background thread when the job has been completed or has thrown an exception */
	protected void onThisJobCompleted() { }
	
	protected void handleJobError(Throwable e) { log.error(e); }
	
	//
	
	protected static final Log log = Log.get("CJob");
	private String name;
	private volatile Object result;
	private CList<CJob> children;
	private volatile boolean cancelled;
	private static final Object NULL = new Object();
	private static final ParallelExecutor exec = createExecutor();
	private static final ThreadLocal<CJob> currentJob = new ThreadLocal<>();
	
	
	public CJob(String name)
	{
		this.name = name;
	}
	
	
	public CJob()
	{
		this("cjob." + CKit.id());
	}
	
	
	public CJob(CJob parent, String name)
	{
		this(name);
		
		if(parent != null)
		{
			parent.addChild(this);
		}
	}
	
	
	public CJob(String name, boolean childOfCurrent)
	{
		this(childOfCurrent ? getJob() : null, name);
	}
	
	
	private static ParallelExecutor createExecutor()
	{
		return new ParallelExecutor("cjob", 10);
	}
	
	
	public static void setKeepAliveTime(long timeSeconds)
	{
		exec.setKeepAliveTime(timeSeconds, TimeUnit.SECONDS);
	}
	
	
	/** 
	 * Signal to finish all currently running jobs and then shitdown the executor.
	 * An exception will be thrown by submit() if a job is submitted after this method is called.
	 */
	public static void shutdown()
	{
		exec.shutdown();
	}
	
	
//	public static boolean shutdown(int waitTimeSeconds) throws Exception
//	{
//		return exec.shutdown(waitTimeSeconds);
//	}
	
	
	public String getName()
	{
		return name;
	}
	
	
	public String toString()
	{
		return getName(); 
	}
	
	
	protected synchronized void addChild(CJob ch)
	{
		if(children == null)
		{
			children = new CList<>();
		}
		
		children.add(ch);
	}
	
	
	public void run()
	{
		if(isCancelled())
		{
			return;
		}
		
		Thread t = Thread.currentThread();
		String oldName = t.getName();
		t.setName(getName());
		
		currentJob.set(this);
		
		try
		{
			process();
			setResult(NULL);
		}
		catch(Throwable e)
		{
			setResult(e);
			handleJobError(e);
		}
		
		try
		{
			onThisJobCompleted();
		}
		catch(Throwable e)
		{
			log.error(e);
		}
		
		currentJob.set(null);
		t.setName(oldName);
	}
	
	
	public static CJob getJob()
	{
		return currentJob.get();
	}
	
	
	protected synchronized void setResult(Object x)
	{
		result = x;
		notifyAll();
	}
	
	
	protected boolean hasResult()
	{
		return (result != null);
	}
	
	
	protected synchronized Object getResult()
	{
		if(result == NULL)
		{
			return null;
		}
		return result;
	}
	
	
	public synchronized Throwable getJobError()
	{
		if(result instanceof Throwable)
		{
			return (Throwable)result;
		}
		return null;
	}
	
	
	// FIX add the job to the child and release the thread
	public void waitForCompletion() throws Exception
	{
		while(!hasResult())
		{
			if(isCancelled())
			{
				break;
			}
			
			synchronized(this)
			{
				try
				{
					// FIX something is wrong here, should not need the timeout parameter
					wait(100);
				}
				catch(Exception e)
				{
					log.error(e);
				}
			}
		}
		
		Object rv = getResult();
		if(rv instanceof Exception)
		{
			throw (Exception)rv;
		}
		else if(rv instanceof Throwable)
		{
			throw new Exception((Throwable)rv);
		}
		
		waitForChildren();
	}
	
	
	public void waitForChildren()
	{
		// wait for children AFTER the main task 
		// because the latter might have spawned more children 
		CList<CJob> cs = getChildrenPrivate();
		waitForAll(cs);
	}
	
	
	public CList<CJob> getChildren()
	{
		CList<CJob> cs = getChildrenPrivate();
		return cs == null ? new CList<>() : cs;
	}
	
	
	protected synchronized CList<CJob> getChildrenPrivate()
	{
		if(children == null)
		{
			return null;
		}
		else
		{
			return new CList<>(children);
		}
	}
	
	
	public void submit()
	{
		submit(this);
	}
	
	
	public static void submit(Runnable r)
	{
		ApplicationSupport.shutdownCJobExecutor = true;
		exec.submit(r);
	}
	
	
	public static void waitForCompletionQuiet(CJob j)
	{
		try
		{
			j.waitForCompletion();
		}
		catch(Exception e)
		{ 
			log.error(e);
		}
	}
	
	
	public static void waitForAll(Collection<CJob> jobs)
	{
		if(jobs != null)
		{
			for(CJob ch: jobs)
			{
				waitForCompletionQuiet(ch);
			}
		}
	}
	
	
	public static void waitForAll(CJob ... jobs)
	{
		for(CJob ch: jobs)
		{
			if(ch != null)
			{
				waitForCompletionQuiet(ch);
			}
		}
	}
	
	
	public static void waitForAll(CJob j1, CJob j2)
	{
		waitForCompletionQuiet(j1);
		waitForCompletionQuiet(j2);
	}
	
	
	public void cancel()
	{
		cancelled = true;
		// TODO interrupt thread
	}
	
	
	public boolean isCancelled()
	{
		return cancelled;
	}
}