// Copyright © 2016-2023 Andy Goryachev <andy@goryachev.com>
package demo.dock;
import goryachev.common.log.Log;
import goryachev.common.log.SimpleLogConfig;
import goryachev.common.util.GlobalSettings;
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
	public static final String COPYRIGHT = "copyright © 2016-2021 andy goryachev";
	public static final String TITLE = "FxDock Docking Framework Demo";


	public static void main(String[] args)
	{
		// init logger
		SimpleLogConfig cf = new SimpleLogConfig();
		cf.addConsole();
		cf.all("DebugSettingsProvider"); 
		cf.all("DemoBrowser"); 
		
		Log.setConfig(cf);
		
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
