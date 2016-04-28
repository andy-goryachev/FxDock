// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock;
import java.util.List;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;


/**
 * WindowListMenuItem.
 */
public class WindowListMenuItem
	extends SeparatorMenuItem
{
	private final Menu menu;
	
	
	public WindowListMenuItem(Menu m)
	{
		this.menu = m;
		m.addEventHandler(Menu.ON_SHOWING, (ev) -> updateMenu());
	}
	
	
	protected void updateMenu()
	{
		List<MenuItem> ms = menu.getItems();
		
		int ix = -1;
		int sz = ms.size();
		for(int i=0; i<sz; )
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
					ix = i + 1;
				}
				i++;
			}
		}
		
		if(ix < 0)
		{
			ix = ms.size() - 1;
		}
		
		int ct = 1;
		for(FxDockWindow w: FxDockFramework.getWindows())
		{
			WindowMenuItem mi = new WindowMenuItem(ct + ": " + w.getTitle());
			mi.setOnAction((ev) -> w.toFront());
			
			ms.add(ix, mi);
			ct++;
			ix++;
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
