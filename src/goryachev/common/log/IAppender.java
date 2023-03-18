// Copyright © 2021-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.common.log;


/**
 * Log Appender Interface.
 */
public interface IAppender
{
	public void append(LogLevel level, long time, StackTraceElement caller, Throwable err, String msg);
	
	
	public int getThreshold();
	
	
	public boolean needsCaller();
}
