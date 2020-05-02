// Copyright © 2017-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.log;
import goryachev.common.log.internal.ConsoleAppender;
import goryachev.common.log.internal.LogConfig;
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
	
	protected static final String STDOUT = "stdout";
	protected static final String STDERR = "stderr";
	
	private ILogEventFormatter formatter = LogEventFormatter.simpleFormatter();
	private final CList<String> channels = new CList();
	
	
	public AppenderBase(String[] channels)
	{
		if(channels != null)
		{
			for(String ch: channels)
			{
				this.channels.add(ch);
			}
		}
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


	public static AppenderBase create(LogConfig.AppenderInfo inf) throws Exception
	{
		if(inf.type == null)
		{
			throw new Exception("undefined appender type (null)");
		}
		
		switch(inf.type)
		{
		case STDOUT:
			return ConsoleAppender.create(inf, System.out);
		case STDERR:
			return ConsoleAppender.create(inf, System.err);
		default:
			throw new Exception("unknown appender type: " + inf.type);
		}
	}
}
