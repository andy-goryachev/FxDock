// Copyright © 2016-2018 Andy Goryachev <andy@goryachev.com>
package demo.dock;
import goryachev.fx.FX;
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
		int n = FX.round(x);
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
