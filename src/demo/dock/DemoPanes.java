// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package demo.dock;
import goryachev.fxdock.FxDockFramework;
import goryachev.fxdock.FxDockPane;
import goryachev.fxdock.FxDockWindow;


/**
 * Demo Panes.
 */
public class DemoPanes
	implements FxDockFramework.Generator
{
	public static final String BROWSER = "BROWSER";


	public FxDockWindow createWindow()
	{
		return new DemoWindow();
	}


	public FxDockPane createPane(String type)
	{
		switch(type)
		{
		case BROWSER:
			return new DemoBrowser();
		default:
			// type here codes for background color
			return new DemoPane(type);
		}
	}
}
