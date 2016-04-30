// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.demo;
import goryachev.common.util.DebugSettingsProvider;
import goryachev.common.util.GlobalSettings;
import goryachev.common.util.Log;
import goryachev.fxdock.FxDockFramework;
import goryachev.fxdock.FxDockPane;
import goryachev.fxdock.FxDockWindow;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.Window;


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
		// init docking framework
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
			// no saved layout, open initial window
			DemoWindow.newWindow();
			move(DemoWindow.newWindow(), 200);
			move(DemoWindow.newWindow(), 400);
		}
	}


	protected void move(Window w, int d)
	{
		w.setX(w.getX() + d);
		w.setY(w.getY() + d);
	}
}
