// Copyright © 2008-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


public abstract class BackgroundProcess
	extends CancellableThread
{
	public abstract void run();
	
	//	
	
	public BackgroundProcess(String name)
	{
		this(name, -3, false);
	}
	
	
	public BackgroundProcess(String name, int priorityDelta, boolean daemon)
	{
		super(name);
		
		int pri = NORM_PRIORITY + priorityDelta;
		if(pri < MIN_PRIORITY)
		{
			pri = MIN_PRIORITY;
		}
		else if(pri > MAX_PRIORITY)
		{
			pri = MAX_PRIORITY;
		}
		setPriority(pri);
		setDaemon(daemon);
	}
}
