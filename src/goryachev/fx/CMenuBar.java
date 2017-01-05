// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.scene.control.MenuBar;


/**
 * CMenuBar.
 */
public class CMenuBar
	extends MenuBar
{
	public CMenuBar()
	{
	}
	
	
	public void add(CMenu m)
	{
		getMenus().add(m);
	}
	
	
	public CMenu addMenu(String text)
	{
		CMenu m = new CMenu(text);
		getMenus().add(m);
		return m;
	}
}
