# How to Use FxDock Library in Your Project

## Overview

- Register your provider for GlobalSettings.
- Implement a generator which creates custom FxDockWindows and FxDockPanes.
- Load the layout by calling FxFramework.openLayout().

Please refer to [DockDemoApp.java](https://github.com/andy-goryachev/FxDock/blob/master/src/demo/dock/DockDemoApp.java) for an example of how to integrate this library into your project.

## Generator

The docking framework operates with two types of objects: 
[FxDockWindows](https://github.com/andy-goryachev/FxDock/blob/master/src/goryachev/fxdock/FxDockWindow.java)
and
[FxDockPanes](https://github.com/andy-goryachev/FxDock/blob/master/src/goryachev/fxdock/FxDockPane.java).

There could be only one type of application window (FX Stage), and it must extend FxDockWindow class.  Each window may contain one or more (or zero, if it is a last window) FX Panes, which must extend FxDockPane class.  You can have as many different types of FxDockPanes as you wish, each type identified by a String type ID.

Before the framework loads the layout, you must set a 
[FxDockFramework.Generator](https://github.com/andy-goryachev/FxDock/blob/master/src/goryachev/fxdock/FxDockFramework.java),
which creates windows and panes for your application.

Here is an example:
[DemoGenerator.java](https://github.com/andy-goryachev/FxDock/blob/master/src/demo/dock/DemoDockSchema.java)
```java
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
	

	public Stage createWindow(String name)
	{
		return new DemoWindow();
	}


	public Stage createDefaultWindow()
	{
		return DemoWindow.openBrowser("https://github.com/andy-goryachev/FxDock");
	}
}
```

## Global Settings

The docking framework uses an implementation of
[ASettingsStore](https://github.com/andy-goryachev/FxDock/blob/master/src/goryachev/fx/settings/ASettingsStore.java)
to store the layout.  This is a facade that can use your custom key-value database to store application-wide settings, or you
can simply use the file-based implementation provided by `GlobalSettings.instance()`.

## Local Settings

The framework also provides a mechanism to store JavaFX properties as a part of the layout.  All you need to do is to add a property to an instance of
[LocalSettings](https://github.com/andy-goryachev/FxDock/blob/master/src/goryachev/fx/settings/LocalSettings.java)
which can be obtained from either a `Node` or a `Window` with `LocalSettings.get()`.


## Logging

The framework uses custom logging façade
[Log](https://github.com/andy-goryachev/FxDock/blob/master/src/goryachev/common/log/Log.java)
.


## Startup

Here is an example of startup code (taken from 
[DockDemoApp](https://github.com/andy-goryachev/FxDock/blob/master/src/demo/dock/DockDemoApp.java)
.  The framework attempts to load an existing layout first, the proceeds to creating the default window if no prior layout exists:

```java
public class DockDemoApp
	extends Application
{
	public static final String COPYRIGHT = "copyright © 2016-2024 andy goryachev";
	public static final String TITLE = "FxDock Framework Demo";
	public static final String VERSION = "2024.0407.1750";


	public static void main(String[] args)
	{
		// init logger
		Log.initConsole(LogLevel.WARN);
		
		// init non-ui subsystems
		GlobalSettings.setFileProvider(new File("settings.conf"));
		
		// launch ui
		Application.launch(DockDemoApp.class, args);
	}
	

	public void start(Stage s) throws Exception
	{
		ASettingsStore store = GlobalSettings.instance();
		DemoDockSchema gen = new DemoDockSchema(store);
		FxFramework.openLayout(gen);
	}
}
```


## Bugs? Questions?

Please let me know by [creating an issue on Github](https://github.com/andy-goryachev/FxDock/issues/new).

