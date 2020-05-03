// Copyright © 2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.log.internal;
import goryachev.common.log.LogLevel;
import goryachev.common.util.CKit;
import goryachev.common.util.SB;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


/**
 * Format Field.
 */
public abstract class FormatField
{
	public abstract void format(SB sb, LogLevel level, long time, StackTraceElement caller, Throwable err, String msg);
	
	public boolean needsCaller() { return false; }
	
	
	// TODO
	// level, fqn, date, filename, location, line, msg, method, newline, priority ms since start, thread name, text
	
	
	/** field emits the caller class name */
	public static FormatField className()
	{
		return new FormatField()
		{
			public void format(SB sb, LogLevel level, long time, StackTraceElement caller, Throwable err, String msg)
			{
				// TODO need to propagate needsCaller flag!
				if(caller == null)
				{
					sb.append("NO_CALLER_ERRROR"); // FIX
				}
				else
				{
					String name = caller.getClassName();
					// TODO options
					int ix = name.lastIndexOf('.');
					if(ix >= 0)
					{
						name = name.substring(ix + 1);
					}
					sb.append(name);
				}
			}


			public boolean needsCaller()
			{
				return true;
			}
		};
	}
	
	
	/** the field formats date/time of the logged event */
	public static FormatField date(String spec)
	{
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern(spec);
		ZoneId tz = ZoneId.systemDefault();

		return new FormatField()
		{
			public void format(SB sb, LogLevel level, long time, StackTraceElement caller, Throwable err, String msg)
			{
				LocalDateTime t = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), tz);
				fmt.formatTo(t, sb);
			}
		};
	}
	
	
	/** the field emits the original exception */
	public static FormatField error()
	{
		// TODO is this needed?
		return new FormatField()
		{
			public void format(SB sb, LogLevel level, long time, StackTraceElement caller, Throwable err, String msg)
			{
				if(err != null)
				{
					// TODO reimplement here
					sb.append(CKit.stackTrace(err));
				}
			}
		};
	}
	
	
	/** field emits the log level */
	public static FormatField level()
	{
		return new FormatField()
		{
			public void format(SB sb, LogLevel level, long time, StackTraceElement caller, Throwable err, String msg)
			{
				sb.append(level);
			}
		};
	}
	
	
	/** field emits the source file line number */
	public static FormatField line()
	{
		return new FormatField()
		{
			public void format(SB sb, LogLevel level, long time, StackTraceElement caller, Throwable err, String msg)
			{
				if(caller == null)
				{
					sb.append("NO_CALLER_ERRROR"); // FIX
				}
				else
				{
					int line = caller.getLineNumber();
					sb.append(line);
				}
			}
			
			
			public boolean needsCaller()
			{
				return true;
			}
		};
	}
	
	
	/** field emits the caller method name */
	public static FormatField method()
	{
		return new FormatField()
		{
			public void format(SB sb, LogLevel level, long time, StackTraceElement caller, Throwable err, String msg)
			{
				if(caller == null)
				{
					sb.append("NO_CALLER_ERRROR"); // FIX
				}
				else
				{
					String method = caller.getMethodName();
					sb.append(method);
				}
			}


			public boolean needsCaller()
			{
				return true;
			}
		};
	}
	
	
	/** field emits the original logged message */
	public static FormatField message()
	{
		return new FormatField()
		{
			public void format(SB sb, LogLevel level, long time, StackTraceElement caller, Throwable err, String msg)
			{
				sb.append(msg);
			}
		};
	}
	
	
	public static FormatField text(String text)
	{
		return new FormatField()
		{
			public void format(SB sb, LogLevel level, long time, StackTraceElement caller, Throwable err, String msg)
			{
				sb.append(text);
			}
		};
	}
}
