// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package demo.dock;
import goryachev.fxdock.FxDockFramework;
import goryachev.fxdock.FxDockPane;
import goryachev.fxdock.FxDockWindow;


/**
 * Demo Generator creates custom window of type DemoWindow
 * and two kinds of panes:
 * - DemoBrowser, a simple web browser, with a type id BROWSER,
 * - DemoPane, an empty pane with a background color encoded in its type id.
 */
public class DemoGenerator
	implements FxDockFramework.Generator
{
	/** type id for a browser pane */
	public static final String BROWSER = "BROWSER";
	/** type id for a login pane */
	public static final String LOGIN = "LOGIN";

	/** creates custom window */
	public FxDockWindow createWindow()
	{
		return new DemoWindow();
	}


	/** creates custom pane using the type id */
	public FxDockPane createPane(String type)
	{
		switch(type)
		{
		case BROWSER:
			return new DemoBrowser();
		case LOGIN:
			return new DemoLoginPane();
		default:
			// type here codes for background color
			return new DemoPane(type);
		}
	}
}
