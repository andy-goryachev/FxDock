package demo;
import goryachev.fxdock.FxDockFramework;
import goryachev.fxdock.FxDockPane;
import goryachev.fxdock.FxDockWindow;


/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MyGenerator
	implements FxDockFramework.Generator
{
	@Override
	public FxDockWindow createWindow()
	{
		return new MyWindow();
	}


	@Override
	public FxDockPane createPane(String s)
	{
		return new MyPane();
	}
}

