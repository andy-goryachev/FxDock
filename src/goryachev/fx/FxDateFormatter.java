// Copyright © 2016-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Fx DateFormatter.
 */
public class FxDateFormatter
	extends FxFormatter
{
	private final SimpleDateFormat format;
	
	
	public FxDateFormatter(String pattern)
	{
		format = new SimpleDateFormat(pattern);
	}
	
	
    public String format(long t)
    {
    	return format.format(t);
    }

	
	@Override
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
			Long v = (Long)x;
			if(v.longValue() <= 0)
			{
				return null;
			}
			return format.format(x);
		}
		else
		{
			return null;
		}
	}
}
