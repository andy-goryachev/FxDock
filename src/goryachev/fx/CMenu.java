// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
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
	
	
	public SeparatorMenuItem addSeparator()
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
}
