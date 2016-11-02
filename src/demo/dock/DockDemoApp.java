// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package demo.dock;
import goryachev.common.util.GlobalSettings;
import goryachev.common.util.Log;
import goryachev.fxdock.FxDockFramework;
import java.io.File;
import javafx.application.Application;
import javafx.stage.Stage;


/**
 * Demo for FxDock docking framework.
 * 
 * This class provides an example of an FX application that uses the docking framework.
 * The framework depends on GlobalSettings, Log, and goryachev.common package.
 * A custom Generator allows for creation of application-specific windows and dockable panes. 
 */
public class DockDemoApp
	extends Application
{
	public static void main(String[] args)
	{
		// init logger
		Log.initConsole();
		Log.conf("DebugSettingsProvider", true);
		
		// init non-ui subsystems
		GlobalSettings.setFileProvider(new File("settings.conf"));
		
		// launch ui
		Application.launch(DockDemoApp.class, args);
	}
	

	public void start(Stage s) throws Exception
	{
		// plug in custom windows and dockable panes. 
		FxDockFramework.setGenerator(new DemoGenerator());
		
		// load saved layout
		int ct = FxDockFramework.loadLayout();
		if(ct == 0)
		{
			// when no saved layout exists, open the first window
			DemoWindow.openBrowser("https://github.com/andy-goryachev/FxDock");
		}
	}
}
