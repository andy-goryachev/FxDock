// Copyright © 2020-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.common.log;


/**
 * Log Event Formatter.
 */
public interface ILogEventFormatter
{
	public String format(LogLevel level, long time, StackTraceElement caller, Throwable err, String msg);
	
	
	public boolean needsCaller();
}
