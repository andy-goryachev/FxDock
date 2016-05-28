# How to Use FxDock Library in Your Project

## Overview

- Register your provider for GlobalSettings.
- Implement a generator which will be used to create custom FxDockWindows and FxDockPanes.
- Load the layout.  Check the number of opened windows, and if it is 0, create the first window.   

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
[DemoGenerator.java](https://github.com/andy-goryachev/FxDock/blob/master/src/demo/dock/DemoGenerator.java)
```java
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
		default:
			// type here codes for background color
			return new DemoPane(type);
		}
	}
}
```

## Global Settings

The docking framework uses 
[GlobalSettings](https://github.com/andy-goryachev/FxDock/blob/master/src/goryachev/common/util/GlobalSettings.java)
facility to store the layout.  This is a facade that can use your custom key-value database to store application-wide settings, or you
can simply use the file-based properties-like implementation provided by default.

## Local Settings

The framework also provides a mechanism to store JavaFX properties as a part of the layout.  All you need to do is to call one of the several
bind() methods available in FxDockWindow and FxDockPane classes.  (Note: currently, support for only a few property types is implemented, as it is still work in progress).  

Here is an example of binding a boolean "selected" property to a subkey for a check box:

```java
	bind("CHECKBOX_MENU", windowCheckAction.selectedProperty());
```

## Logging

The framework uses custom logging facade
[Log](https://github.com/andy-goryachev/FxDock/blob/master/src/goryachev/common/util/Log.java)
which, alas, is still a work in progress.


## Startup

Here is an example of startup code.  The framework attempts to load an existing layout first, the proceeds to creating the initial window if no prior layout exists:
```java
	// load saved layout
	int ct = FxDockFramework.loadLayout();
	if(ct == 0)
	{
		// when no saved layout exists, open the first window
		DemoWindow.openBrowser("https://github.com/andy-goryachev/FxDock");
	}
```


## Did I Miss Anything?

Please let me know by [creating an issue on Github](https://github.com/andy-goryachev/FxDock/issues/new).

