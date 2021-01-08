// Copyright © 2017-2021 Andy Goryachev <andy@goryachev.com>
package goryachev.common.log;
import goryachev.common.util.Keep;


/**
 * Log Level.
 */
@Keep
public enum LogLevel
{
	OFF,
	FATAL,
	ERROR,
	WARN,
	INFO,
	DEBUG,
	TRACE,
	ALL;
	

	public boolean isGreaterThanOrEqual(LogLevel level)
	{
		return ordinal() >= level.ordinal();
	}
}
