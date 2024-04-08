// Copyright © 2017-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;


/**
 * Standard Formatters.
 */
public class Formatters
{
	// TODO should these be settable somehow?
	private static FxDecimalFormatter integerFormatter;
	
	
	public static FxDecimalFormatter integerFormatter()
	{
		if(integerFormatter == null)
		{
			integerFormatter = new FxDecimalFormatter("#,##0");
		}
		return integerFormatter;
	}
}
