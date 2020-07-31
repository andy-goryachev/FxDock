// Copyright © 2017-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import goryachev.common.log.Log;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;


/**
 * Common Task eliminates the need to explicitly manage Threads.
 * Tasks are run by the default ParallelExecutor.
 */
public class CTask<T>
	implements Runnable
{
	protected static final Log log = Log.get("CTask");
	protected ValueGenerator<? extends T> generator;
	protected Consumer<T> onSuccess;
	protected Consumer<Throwable> onError;
	protected Runnable onFinish;
	protected static ParallelExecutor exec = initExecutor();
	
	
	public CTask()
	{
	}
	
	
	public CTask<T> producer(ValueGenerator<? extends T> generator)
	{
		this.generator = generator;
		return this;
	}
	

	public CTask<T> onError(Consumer<Throwable> onError)
	{
		this.onError = onError;
		return this;
	}
	
	
	public CTask<T> onSuccess(Consumer<T> onSuccess)
	{
		this.onSuccess = onSuccess;
		return this;
	}
	
	
	public CTask<T> onFinish(Runnable onFinish)
	{
		this.onFinish = onFinish;
		return this;
	}
	
	
	/** submits the task to be executed by the default executor */
	public void submit()
	{
		submit(this);
	}
	
	
	/** submits the task to be executed by the specified executor */
	public void submit(ExecutorService ex)
	{
		ex.submit(this);
	}
	
	
	/** submits a Runnable to be executed by the default executor */
	public static void submit(Runnable r)
	{
		exec.submit(r);
	}
	
	
	public void run()
	{
		try
		{
			T result = generator.generate();
			
			try
			{
				handleSuccess(result);
			}
			catch(Throwable e)
			{
				log.error(e);
			}
		}
		catch(Throwable e)
		{
			try
			{
				handleError(e);
			}
			catch(Throwable err)
			{
				log.error(e);
			}
		}
		
		try
		{
			handleFinish();
		}
		catch(Throwable e)
		{
			log.error(e);
		}
	}
	
	
	private static ParallelExecutor initExecutor()
	{
		ParallelExecutor ex = new ParallelExecutor("CTask.Executor", 5);
		return ex;
	}

	
	protected void handleSuccess(T result)
	{
		Consumer<T> c = onSuccess;
		if(c != null)
		{
			c.accept(result);
		}
	}
	
	
	protected void handleError(Throwable e)
	{
		Consumer<Throwable> c = onError;
		if(c != null)
		{
			c.accept(e);
		}
	}
	
	
	protected void handleFinish()
	{
		Runnable r = onFinish;
		if(r != null)
		{
			r.run();
		}
	}
}
