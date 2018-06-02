// Copyright © 2016-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import java.util.List;
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
	
	
	public CMenu menu(String name)
	{
		return addMenu(name);
	}
	
	
	public void separator()
	{
		lastMenu().separator();
	}
	
	
	protected CMenu lastMenu()
	{
		List<Menu> ms = getMenus();
		return (CMenu)ms.get(ms.size() - 1);
	}
	
	
	public CMenuItem item(String name)
	{
		CMenuItem m = new CMenuItem(name);
		m.setDisable(true);
		lastMenu().add(m);
		return m;
	}
	
	
	public CMenuItem item(String name, FxAction a)
	{
		CMenuItem m = new CMenuItem(name, a);
		lastMenu().add(m);
		return m;
	}
}
