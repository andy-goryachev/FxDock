# FxDock

Docking Framework for JavaFX.

![screenshot](https://github.com/andy-goryachev/FxDock/blob/master/screenshots/2016-0521-125006-709.png)

## About

This library was created to provide JavaFX developers with a fully functional docking framework.

## License

This project and its source code is licensed under the [GNU General Public License version 3](https://www.gnu.org/licenses/gpl-3.0.en.html) and you should feel free to make adaptations of this work. Please see the included LICENSE file for further details.

Please contact me to acquire this library under a business-friendly license.

## Demo

Download and run [**FxDockDemo.jar**](https://github.com/andy-goryachev/FxDock/blob/master/demo/FxDockDemo.jar)

## Using the Library

- Register your provider for GlobalSettings.
- Set generator which will be used to create custom FxDockWindows and FxDockPanes.
- Load the layout.  Check the number of opened windows, and if it is 0, create the first window.   

Please refer to [**DockDemoApp.java**](https://github.com/andy-goryachev/FxDock/blob/master/src/demo/dock/DockDemoApp.java) for an example of how to integrate this library into your project.

