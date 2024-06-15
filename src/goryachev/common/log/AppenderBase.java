// Copyright © 2017-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.common.log;
import goryachev.common.log.internal.LogEventFormatter;
import goryachev.common.util.CList;
import java.util.List;


/**
 * Log Appender base class.
 */
public abstract class AppenderBase
	implements IAppender
{
	public abstract void emit(String s);
	
	//
	
	private int threshold;
	private ILogEventFormatter formatter = LogEventFormatter.simpleFormatter();
	private final CList<String> channels = new CList();
	
	
	public AppenderBase(LogLevel threshold)
	{
		this.threshold = threshold.ordinal();
	}
	
	
	public AppenderBase()
	{
		this(LogLevel.ALL);
	}
	
	
	public void setFormatter(ILogEventFormatter f)
	{
		formatter = f;
	}
	
	
	@Override
	public int getThreshold()
	{
		return threshold;
	}
	
	
	@Override
	public boolean needsCaller()
	{
		return formatter.needsCaller();
	}
	
	
	public List<String> getChannels()
	{
		return channels;
	}
	
	
	@Override
	public void append(LogLevel level, long time, StackTraceElement caller, Throwable err, String msg)
	{
		if(level.ordinal() >= threshold)
		{
			String s = formatter.format(level, time, caller, err, msg);
			emit(s);
		}
	}
}
