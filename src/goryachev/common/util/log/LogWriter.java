// Copyright © 2009-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.log;
import goryachev.common.util.ILogWriter;
import goryachev.common.util.SB;
import java.io.Closeable;
import java.text.SimpleDateFormat;


// TODO async
// TODO set format (method, timestamp, format)
public abstract class LogWriter
	implements ILogWriter, LogWriterPrivate, Closeable
{
	public abstract void print(String s);
	
	//
	
	private String name;
	private volatile LogWriterPrivate actualWriter = this;
	private SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MMdd-HH:mm:ss.SSS");
	
	
	public LogWriter(String name)
	{
		this.name = name;
	}
	
	
	public String getName()
	{
		return name;
	}
	
	
	public String toString()
	{
		return "LogWriter " + getName();
	}
	
	
	public void write(LogEntry en)
	{
		// FIX perhaps use class hierarchy instead: there are async file loggers, etc.
		// prepare (stack trace, etc)
		actualWriter.writePrivate(en);
	}
	
	
	public void writePrivate(LogEntry en)
	{
		String s = format(en);
		print(s);
	}
	
	
	public void setAsync(boolean on)
	{
		if(on != isAsync())
		{
			if(on)
			{
				actualWriter = createAsyncWriter();
			}
			else
			{
				if(actualWriter instanceof AsyncWriter)
				{
					((AsyncWriter)actualWriter).close();
				}
				
				actualWriter = this;
			}
		}
	}
	
	
	public boolean isAsync()
	{
		return actualWriter != this;
	}
	
	
	protected LogWriterPrivate createAsyncWriter()
	{
		return new AsyncWriter(this);
	}
	
	
	protected String format(LogEntry en)
	{
		SB sb = new SB();
		
		synchronized(timeFormat)
		{
			sb.a(timeFormat.format(en.getTimestamp()));
		}
		sb.a(' ');
		
		String caller = en.getCaller();
		if(caller != null)
		{
			sb.a(caller);
			sb.a(' ');
		}
		
		sb.a(en.getMessage());
		
		return sb.toString();
	}
}
