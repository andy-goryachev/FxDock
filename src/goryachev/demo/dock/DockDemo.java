// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.demo.dock;
import goryachev.common.util.FileSettingsProvider;
import goryachev.common.util.GlobalSettings;
import goryachev.common.util.Log;
import goryachev.fxdock.FxDockFramework;
import goryachev.fxdock.FxDockPane;
import goryachev.fxdock.FxDockWindow;
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
public class DockDemo
	extends Application
{
	public static void main(String[] args)
	{
		// init logger
		Log.initConsole();
		Log.conf("DebugSettingsProvider", true);
		
		// init non-ui subsystems
		File settingsFile = new File("settings.conf");
		FileSettingsProvider fs = new FileSettingsProvider(settingsFile);
		fs.loadQuiet();
		GlobalSettings.setProvider(fs);
		
		// launch ui
		new DockDemo().launch(args);
	}
	

	public void start(Stage s) throws Exception
	{
		// plug in custom windows and dockable panes. 
		FxDockFramework.setGenerator(new FxDockFramework.Generator()
		{
			public FxDockWindow createWindow()
			{
				return new DemoWindow();
			}
			
			public FxDockPane createPane(String type)
			{
				return new DemoPane(type);
			}
		});
		
		// load saved layout
		int ct = FxDockFramework.loadLayout();
		if(ct == 0)
		{
			// when no saved layout exists, open the first window
			DemoWindow.actionNewWindow().setTitle("Initial Window");
		}
	}
}
