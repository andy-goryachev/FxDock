package demo;
import goryachev.fxdock.FxDockWindow;


/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MyWindow
	extends FxDockWindow
{
	public MyWindow()
	{
		MyPane pane1 = new MyPane();

		setContent(pane1);
	}
}

