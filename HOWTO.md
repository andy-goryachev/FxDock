# How to Use FxDock Library in Your Project

## Overwiev

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

## Global Settings

- explain bindings


## Local Settings


## Logger


## Startup
