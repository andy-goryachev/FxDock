// Copyright © 2022-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.util;
import goryachev.common.util.CKit;
import goryachev.fx.FX;
import java.util.List;
import javafx.stage.Stage;
import javafx.stage.Window;


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
	
	
	public static String describe(Window w)
	{
		if(w == null)
		{
			return "<null>";
		}
		
		if(w instanceof Stage s)
		{
			String title = s.getTitle();
			if(CKit.isNotBlank(title))
			{
				return title;
			}
		}
		return w.getClass().getSimpleName() + "(" + FX.getName(w) + ")";
	}
}
