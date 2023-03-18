// Copyright © 2017-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;


/**
 * Common Date Format which operates with long, Dates, and new LocalDates types.
 */
public class CDateFormat
	implements IFormat
{
	private final String pattern;
	private SimpleDateFormat simple;
	private DateTimeFormatter dt;
	
	
	public CDateFormat(String pattern)
	{
		this.pattern = pattern;
	}
	
	
	public String getPattern()
	{
		return pattern;
	}
	
	
	protected SimpleDateFormat simple()
	{
		if(simple == null)
		{
			simple = new SimpleDateFormat(pattern);
		}
		return simple;
	}
	
	
	protected DateTimeFormatter dt()
	{
		if(dt == null)
		{
			dt = DateTimeFormatter.ofPattern(pattern);
		}
		return dt;
	}
	
	
	public String format(Object x)
	{
		if(x != null)
		{
			if(x instanceof Long)
			{
				return simple().format(x);
			}
			else if(x instanceof Date)
			{
				return simple().format(x);
			}
			else if(x instanceof TemporalAccessor)
			{
				return dt().format((TemporalAccessor)x);
			}
		}
		return null;
	}
}
