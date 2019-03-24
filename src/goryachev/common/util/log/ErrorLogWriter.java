// Copyright © 2015-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.log;
import goryachev.common.util.CList;
import goryachev.common.util.ILogWriter;


public class ErrorLogWriter
	implements ILogWriter
{
	public interface Monitor
	{
		// should be mapped easily to table model events 
		public void handleMonitorEvent(int removed, int added);
	}
	
	//
	
	public static final ErrorLogWriter instance = new ErrorLogWriter();
	private int maxSize = 128;
	private CList<LogEntry> errors = new CList<>();
	private CList<Monitor> listeners;
	
	
	private ErrorLogWriter()
	{
	}


	public void write(LogEntry en)
	{
		int removed = 0;
		Object[] ls = null;
		
		synchronized(this)
		{
			while(errors.size() >= maxSize)
			{
				errors.remove(0);
				removed++;
			}
			
			errors.add(en);
		
			if(listeners != null)
			{
				ls = listeners.toArray();
			}
		}
		
		if(ls != null)
		{
			// fire events outside of the synchronized block
			for(Object m: ls)
			{
				((Monitor)m).handleMonitorEvent(removed, 1);
			}
		}
	}
	
	
	public synchronized void addMonitor(Monitor m)
	{
		if(listeners == null)
		{
			listeners = new CList<>();
		}
		
		listeners.add(m);
	}
	
	
	public synchronized void removeMonitor(Monitor m)
	{
		if(listeners != null)
		{
			listeners.remove(m);
		}
	}
}
