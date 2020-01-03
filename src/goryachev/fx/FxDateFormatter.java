// Copyright © 2016-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * FxDateFormatter.
 */
public class FxDateFormatter
	extends FxFormatter
{
	private final SimpleDateFormat format;
	
	
	public FxDateFormatter(String pattern)
	{
		format = new SimpleDateFormat(pattern);
	}

	
	public String toString(Object x)
	{
		if(x == null)
		{
			return null;
		}
		else if(x instanceof Date)
		{
			return format.format(x);
		}
		else if(x instanceof Long)
		{
			return format.format(x);
		}
		else
		{
			return null;
		}
	}
}
