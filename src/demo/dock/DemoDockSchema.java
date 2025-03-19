// Copyright © 2016-2025 Andy Goryachev <andy@goryachev.com>
package demo.dock;
import goryachev.common.util.ASettingsStore;
import goryachev.fxdock.FxDockPane;
import goryachev.fxdock.FxDockSchema;
import javafx.stage.Stage;


/**
 * Demo Schema creates custom dock windows and dock panes.
 */
public class DemoDockSchema
	extends FxDockSchema
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
	
	
	public DemoDockSchema(ASettingsStore store)
	{
		super(store);
	}
	

	/** creates custom pane using the type id */
	@Override
	public FxDockPane createPane(String id)
	{
		switch(id)
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
			// id determines the background color
			return new DemoPane(id);
		}
	}
	

	@Override
	public Stage createWindow(String name)
	{
		return new DemoWindow();
	}


	@Override
	public Stage createDefaultWindow()
	{
		return DemoWindow.openBrowser("https://github.com/andy-goryachev/FxDock");
	}
}
