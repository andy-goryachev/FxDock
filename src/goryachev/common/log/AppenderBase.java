// Copyright © 2017-2021 Andy Goryachev <andy@goryachev.com>
package goryachev.common.log;
import goryachev.common.log.internal.LogEventFormatter;
import goryachev.common.util.CList;
import java.util.List;


/**
 * Log Appender base class.
 */
public abstract class AppenderBase
{
	public abstract void emit(String s);
	
	//
	
	private ILogEventFormatter formatter = LogEventFormatter.simpleFormatter();
	private final CList<String> channels = new CList();
	
	
	public AppenderBase()
	{
	}
	
	
	public boolean needsCaller()
	{
		return formatter.needsCaller();
	}
	
	
	public List<String> getChannels()
	{
		return channels;
	}
	
	
	public void append(LogLevel level, long time, StackTraceElement caller, Throwable err, String msg)
	{
		String s = formatter.format(level, time, caller, err, msg);
		emit(s);
	}
}
