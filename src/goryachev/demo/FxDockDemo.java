// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.demo;
import goryachev.common.util.DebugSettingsProvider;
import goryachev.common.util.GlobalSettings;
import goryachev.common.util.Log;
import goryachev.fxdock.FxDockFramework;
import javafx.application.Application;
import javafx.stage.Stage;


/**
 * FxDockDemo.
 */
public class FxDockDemo
	extends Application
{
	public static void main(String[] args)
	{
		// init logger
		Log.initConsole();
		Log.conf("DebugSettingsProvider", true);
		
		// init non-ui subsystems
		GlobalSettings.setProvider(new DebugSettingsProvider());
		
		// launch ui
		new FxDockDemo().launch(args);
	}
	

	public void start(Stage s) throws Exception
	{
		// load saved layout
		int ct = FxDockFramework.loadLayout(DemoWindow.class);
		if(ct == 0)
		{
			// no saved layout, open initial window
			DemoWindow.newWindow();
		}
	}
}
