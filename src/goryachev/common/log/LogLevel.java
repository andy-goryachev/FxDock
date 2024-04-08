// Copyright © 2017-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.common.log;
import goryachev.common.util.Keep;


/**
 * Log Level.
 * 
 * Do not re-arrange, order is important.
 */
@Keep
public enum LogLevel
{
	ALL,
	TRACE,
	DEBUG,
	INFO,
	WARN,
	ERROR,
	FATAL,
	OFF
}
