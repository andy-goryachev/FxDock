// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import goryachev.common.util.log.LogEntry;


/** 
 * An individual named logger channel.
 */
public class CLog
{
	private String name;
	private volatile boolean enabled = true;
	private volatile boolean printCallingMethod = true;
	// using unsynchronized list, which will be replaced with a new instance on any change
	private volatile CList<ILogWriter> writers = new CList(0);
	
	
	public CLog(String name)
	{
		this.name = name;
	}
	
	
	public String getName()
	{
		return name;
	}
	
	
	public void err(Throwable e)
	{
		if(enabled)
		{
			LogEntry en = new LogEntry(e, null, 0);
			if(printCallingMethod)
			{
				en.setCaller(e, 0);
			}
			add(en);
		}
	}
	
	
	public void err(String message)
	{
		err(message, 2);
	}
	
	
	protected void err(String message, int level)
	{
		if(enabled)
		{
			Throwable e = new Throwable(message);
			LogEntry en = new LogEntry(e, null, level);
			if(printCallingMethod)
			{
				en.setCaller(e, level);
			}
			add(en);
		}
	}
	
	
	public void print(Object a)
	{
		if(enabled)
		{
			SB sb = new SB();
			Log.append(sb, a);
			
			LogEntry en = new LogEntry(sb.toString());
			if(printCallingMethod)
			{
				en.setCaller(new Throwable(), 1);
			}
			add(en);
		}
	}
	
	
	public void print(Object a, Object b)
	{
		if(enabled)
		{
			SB sb = new SB();
			Log.append(sb, a);
			Log.append(sb, b);
			
			LogEntry en = new LogEntry(sb.toString());
			if(printCallingMethod)
			{
				en.setCaller(new Throwable(), 1);
			}
			add(en);
		}
	}
	
	
	public void print(Object a, Object b, Object c)
	{
		if(enabled)
		{
			SB sb = new SB();
			Log.append(sb, a);
			Log.append(sb, b);
			Log.append(sb, c);
			
			LogEntry en = new LogEntry(sb.toString());
			if(printCallingMethod)
			{
				en.setCaller(new Throwable(), 1);
			}
			add(en);
		}
	}
	
	
	public void print(Object a, Object b, Object c, Object d)
	{
		if(enabled)
		{
			SB sb = new SB();
			Log.append(sb, a);
			Log.append(sb, b);
			Log.append(sb, c);
			Log.append(sb, d);
			
			LogEntry en = new LogEntry(sb.toString());
			if(printCallingMethod)
			{
				en.setCaller(new Throwable(), 1);
			}
			add(en);
		}
	}
	
	
	public void print(Object a, Object b, Object c, Object d, Object e)
	{
		if(enabled)
		{
			SB sb = new SB();
			Log.append(sb, a);
			Log.append(sb, b);
			Log.append(sb, c);
			Log.append(sb, d);
			Log.append(sb, e);
			
			LogEntry en = new LogEntry(sb.toString());
			if(printCallingMethod)
			{
				en.setCaller(new Throwable(), 1);
			}
			add(en);
		}
	}
	
	
	protected void add(LogEntry en)
	{
		CList<ILogWriter> ws = writers;
		for(int i=ws.size()-1; i>=0; i--)
		{
			ws.get(i).write(en);
		}
	}
	
	
	public void setEnabled(boolean on)
	{
		enabled = on;
	}
	
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	
	public boolean getPrintCallingMethod()
	{
		return printCallingMethod;
	}
	
	
	public void setPrintCallingMethod(boolean on)
	{
		printCallingMethod = on;
	}


	public synchronized void addWriter(ILogWriter wr)
	{
		CList<ILogWriter> ws = new CList(writers);
		ws.add(wr);
		writers = ws;
	}
	
	
	public synchronized void removeWriter(ILogWriter wr)
	{
		CList<ILogWriter> ws = new CList(writers);
		ws.remove(wr);
		writers = ws;
	}
	
	
	protected void close()
	{
		// TODO write null entry?
	}
}
