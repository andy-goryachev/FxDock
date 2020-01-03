// Copyright © 2016-2020 Andy Goryachev <andy@goryachev.com>
package demo.dock;
import goryachev.common.util.CKit;
import goryachev.fx.HPane;
import java.text.DecimalFormat;


/**
 * Demo Tools.
 */
public class DemoTools
{
	private static DecimalFormat format = new DecimalFormat("#0.##");
	
	
	/** formats double value */
	public static String f(double x)
	{
		int n = CKit.round(x);
		if(x == n)
		{
			return String.valueOf(n);
		}
		else
		{
			return format.format(x);
		}
	}
	
	
	/** spec description */
	public static String spec(double x)
	{
		if(x == HPane.FILL)
		{
			return "FILL";
		}
		else if(x == HPane.PREF)
		{
			return "PREF";
		}
		else
		{
			return f(x);
		}
	}
}
