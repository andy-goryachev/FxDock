// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
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
	/** type id for a CPane demo */
	public static final String CPANE = "CPANE";
	/** type id for a HPane demo */
	public static final String HPANE = "HPANE";
	/** type id for a login pane */
	public static final String LOGIN = "LOGIN";
	/** type id for a VPane demo */
	public static final String VPANE = "VPANE";
	

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
		case CPANE:
			return new DemoCPane();
		case HPANE:
			return new DemoHPane();
		case LOGIN:
			return new DemoLoginPane();
		default:
			// type here codes for background color
			return new DemoPane(type);
		}
	}
}
