// Copyright © 2016-2026 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock;
import goryachev.fx.FX;
import goryachev.fxdock.internal.DockTools;
import java.util.List;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.stage.Window;


/**
 * WindowListMenuItem is a separator menu item which automatically inserts a list of window selection menu items after itself,
 * and updates this list dynamically.
 */
public class WindowListMenuItem
	extends SeparatorMenuItem
{
	private final Window owner;
	private final Menu menu;
	
	
	public WindowListMenuItem(Window owner, Menu m)
	{
		this.owner = owner;
		this.menu = m;
		m.addEventHandler(Menu.ON_SHOWING, (ev) -> updateMenu());
	}
	
	
	protected void updateMenu()
	{
		List<MenuItem> ms = menu.getItems();
		
		int index = -1;
		int i = 0;
		while(i < ms.size())
		{
			MenuItem x = ms.get(i); 
			if(x instanceof WindowMenuItem)
			{
				ms.remove(i);
			}
			else
			{
				if(x == this)
				{
					index = i + 1;
				}
				i++;
			}
		}
		
		if(index < 0)
		{
			index = ms.size() - 1;
		}
		
		int ct = 1;
		for(FxDockWindow w: DockTools.getWindows())
		{
			WindowMenuItem mi = new WindowMenuItem(ct + ": " + w.getTitle());
			mi.setOnAction((ev) -> FX.toFront(w));
			mi.setDisable(w == owner);
			
			ms.add(index, mi);
			ct++;
			index++;
		}
	}
	
	
	//
	
	
	public static class WindowMenuItem
		extends MenuItem
	{
		public WindowMenuItem(String text)
		{
			super(text);
		}
	}
}
