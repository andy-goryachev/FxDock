// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;


/**
 * CMenu.
 */
public class CMenu
	extends Menu
{
	public CMenu(String text)
	{
		super(text);
	}
	
	
	public CMenu(String text, CAction a)
	{
		super(text);
		a.attach(this);
	}
	
	
	public SeparatorMenuItem separator()
	{
		SeparatorMenuItem m = new SeparatorMenuItem();
		getItems().add(m);
		return m;
	}
	
	
	public CMenuItem add(String text, CAction a)
	{
		CMenuItem m = new CMenuItem(text, a);
		getItems().add(m);
		return m;
	}
	
	
	public MenuItem add(MenuItem m)
	{
		getItems().add(m);
		return m;
	}
	
	
	/** adds a disabled menu item */
	public MenuItem add(String text)
	{
		CMenuItem m = new CMenuItem(text);
		m.setDisable(true);
		return add(m);
	}
	
	
	/** remove all menu items */
	public void clear()
	{
		getItems().clear();
	}
}
