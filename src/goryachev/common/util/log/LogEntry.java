// Copyright © 2006-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.log;
import goryachev.common.util.CKit;


public class LogEntry
{
	private final long timestamp = System.currentTimeMillis();
	private Throwable exception;
	private String text;
	private String caller;
	private int stackTraceLevel;

	
	public LogEntry(Throwable e, String text, int stackTraceLevel)
	{
		this.exception = e;
		this.text = text;
		this.stackTraceLevel = stackTraceLevel;
	}
	
	
	public LogEntry(String text)
	{
		this.text = text;
	}
	
	
	public long getTimestamp()
	{
		return timestamp;
	}
	
	
	public String getText()
	{
		return text;
	}
	
	
	public Throwable getException()
	{
		return exception;
	}
	
	
	public boolean isException()
	{
		return (exception != null);
	}
	
	
	public String getTitle()
	{
		if(isException())
		{
			return exception.getClass().getName();
		}
		else
		{
			return text;
		}
	}


	public String getCaller()
	{
		return caller;
	}
	
	
	public void setCaller(Throwable ex, int level)
	{
		try
		{
			StackTraceElement[] ss = ex.getStackTrace();
			if(ss != null)
			{
				if(ss.length > level)
				{
					StackTraceElement t = ss[level];
					String className = t.getClassName();
			
					int ix = className.lastIndexOf('.');
					if(ix > 0)
					{
						className = className.substring(ix + 1);
					}
			
					caller =  className + "." + t.getMethodName();
				}
			}
		}
		catch(Exception e)
		{
			// getting AIOOBE
			// problem with stack trace array?
			e.printStackTrace();
		}
	}
	
	
	public String getMessage()
	{
		if(text == null)
		{
			if(exception != null)
			{
				return CKit.stackTrace(exception, stackTraceLevel);
			}
		}
		
		return text;
	}
}
