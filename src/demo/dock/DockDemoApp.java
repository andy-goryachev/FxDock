// Copyright © 2016-2024 Andy Goryachev <andy@goryachev.com>
package demo.dock;
import goryachev.common.log.Log;
import goryachev.common.log.LogLevel;
import goryachev.common.util.ASettingsStore;
import goryachev.common.util.GlobalSettings;
import goryachev.fx.FxFramework;
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
	public static final String COPYRIGHT = "copyright © 2016-2024 andy goryachev";
	public static final String TITLE = "FxDock Framework Demo";


	public static void main(String[] args)
	{
		// init logger
		Log.initConsole(LogLevel.WARN);
		Log.setLevel
		(
			LogLevel.ALL,
//			"DemoBrowser",
//			"SettingsProviderBase.reads",
//			"SettingsProviderBase.writes",
			"WindowMonitor",
			"$"
		);
		
		// init non-ui subsystems
		GlobalSettings.setFileProvider(new File("settings.conf"));
		
		// launch ui
		Application.launch(DockDemoApp.class, args);
	}
	

	@Override
	public void start(Stage s) throws Exception
	{
		ASettingsStore store = GlobalSettings.instance();
		DemoDockSchema gen = new DemoDockSchema(store);
		FxFramework.openLayout(gen);
	}
}
