// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock.dnd;
import goryachev.common.util.CKit;
import goryachev.fxdock.internal.FxDockTabPane;


/**
 * Drop zone position in an FxDockTabPane.
 */
public class WhereTab
{
	public final FxDockTabPane tabPane;
	public final int index;

	
	public WhereTab(FxDockTabPane tabPane, int index)
	{
		this.tabPane = tabPane;
		this.index = index;
	}
	
	
	public boolean equals(Object x)
	{
		if(x == this)
		{
			return true;
		}
		else if(x instanceof WhereTab)
		{
			WhereTab w = (WhereTab)x;
			return 
				(tabPane == w.tabPane) &&
				(index == w.index);
		}
		else
		{
			return false;
		}
	}
	
	
	public int hashCode()
	{
		return CKit.hashCode(WhereTab.class, tabPane, index);
	}
}
