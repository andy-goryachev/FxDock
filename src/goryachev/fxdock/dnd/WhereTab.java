// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock.dnd;
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
}
