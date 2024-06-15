// Copyright © 2016-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import java.text.DecimalFormat;


/**
 * Fx Decimal Number Formatter.
 */
public class FxDecimalFormatter
	extends FxFormatter
{
	private final DecimalFormat format;
	
	
	public FxDecimalFormatter(String pattern)
	{
		format = new DecimalFormat(pattern);
	}

	
	@Override
	public String toString(Object x)
	{
		if(x == null)
		{
			return null;
		}
		else if(x instanceof Number)
		{
			return format.format(x);
		}
		else
		{
			return null;
		}
	}
}
