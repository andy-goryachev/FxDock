// Copyright © 2020-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.common.log.internal;
import goryachev.common.log.ILogEventFormatter;
import goryachev.common.log.LogLevel;
import goryachev.common.log.LogUtil;
import goryachev.common.util.SB;


/**
 * Log Event Formatter.
 */
public class LogEventFormatter
	implements ILogEventFormatter
{
	private final FormatField[] fields;
	private final boolean needsCaller;
	
	
	public LogEventFormatter(FormatField[] fields)
	{
		this.fields = fields;
		this.needsCaller = LogUtil.needsCaller(fields);
	}


	@Override
	public String format(LogLevel level, long time, StackTraceElement caller, Throwable err, String msg)
	{
		SB sb = new SB(128);
		for(int i=0; i<fields.length; i++)
		{
			FormatField ff = fields[i];
			ff.format(sb, level, time, caller, err, msg);
		}
		return sb.toString();
	}


	@Override
	public boolean needsCaller()
	{
		return needsCaller;
	}
	
	
	public static LogEventFormatter simpleFormatter()
	{
		return new LogEventFormatter(new FormatField[]
		{
			FormatField.date("MM/dd HH:mm:ss.SSS"),
			FormatField.text(" "),
			FormatField.level(),
			FormatField.text(" "),
			FormatField.className(),
			FormatField.text("."),
			FormatField.method(),
			FormatField.text(":"),
			FormatField.line(),
			FormatField.text(" "),
			FormatField.error(),
			FormatField.text(" "),
			FormatField.message()
		});
	}
}
