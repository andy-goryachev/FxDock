// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
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
}
