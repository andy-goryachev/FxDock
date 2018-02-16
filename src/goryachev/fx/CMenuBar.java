// Copyright © 2016-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;


/**
 * Convenient MenuBar.
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
	
	
	public void addFill()
	{
		// TODO
	}
	
	
	public void add(Node n)
	{
		Menu m = new Menu();
//		m.setDisable(true);
		m.setGraphic(n);
		getMenus().add(m);
	}
}
