// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock.internal;
import goryachev.fxdock.FxDockWindow;


/**
 * FxDockRootPane.
 */
public class FxDockRootPane
	extends FxDockBorderPane
{
	private final FxDockWindow window;
	
	
	public FxDockRootPane(FxDockWindow w)
	{
		this.window = w;
		setContent(new FxDockEmptyPane());
	}
}
