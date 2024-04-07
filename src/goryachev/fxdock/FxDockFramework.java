// Copyright © 2016-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock;
import goryachev.common.util.CList;
import goryachev.fx.FxFramework;
import goryachev.fx.internal.WindowMonitor;
import java.util.List;
import javafx.stage.Window;


/**
 * Docking Framework for JavaFX.
 */
// TODO not sure if this is needed... remove?
public class FxDockFramework
{
	public static FxDockWindow createWindow()
	{
		return (FxDockWindow)FxFramework.createDefaultWindow();
	}
	
	
	/** returns a list of visible windows, topmost window first */
	public static List<FxDockWindow> getWindows()
	{
		List<Window> ws = WindowMonitor.getWindowStack();
		int sz = ws.size();
		CList<FxDockWindow> rv = new CList<>(sz);
		for(int i=sz-1; i>=0; i--)
		{
			Window w = ws.get(i);
			if(w instanceof FxDockWindow dw)
			{
				rv.add(dw);
			}
		}
		return rv;
	}
}
