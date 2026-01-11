// Copyright © 2017-2025 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import java.text.DecimalFormat;


/**
 * Standard Formatters.
 */
public class Formatters
{
	private static FxDecimalFormatter integerFormatter;
	private static FxDecimalFormatter decimal2DP;
	
	
	public static FxDecimalFormatter integerFormatter()
	{
		if(integerFormatter == null)
		{
			integerFormatter = new FxDecimalFormatter("#,##0");
		}
		return integerFormatter;
	}
	
	
	public static FxDecimalFormatter decimal2DP()
	{
		if(decimal2DP == null)
		{
			decimal2DP = new FxDecimalFormatter("#0.##");
		}
		return decimal2DP;
	}


	/**
	 * Formats double as a decimal value, omitting fractional values for exact integers.
	 */
	public static String formatDecimalOrInt(double v)
	{
		if(v == Math.rint(v))
		{
			return String.valueOf((long)v);
		}
		return String.valueOf(v);
	}
}
