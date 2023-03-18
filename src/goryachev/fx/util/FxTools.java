// Copyright © 2022-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.util;
import java.util.List;


/**
 * FxTools.
 */
public class FxTools
{
	/** finds the maximum value in the list of Integers */
	public static int getMaximumValue(List<Integer> items)
	{
		int rv = Integer.MIN_VALUE;
		
		for(int v: items)
		{
			if(rv < v)
			{
				rv = v;
			}
		}
		return rv;
	}
}
