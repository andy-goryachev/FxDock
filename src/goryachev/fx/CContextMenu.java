// Copyright © 2016-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;


/**
 * ContextMenu.
 */
public class CContextMenu
	extends ContextMenu
{
	public CContextMenu()
	{
		setHideOnEscape(true);
		setAutoHide(true);
		setAutoFix(true);
	}
	
	
	public void add(MenuItem m)
	{
		getItems().add(m);
	}
	
	
	public void add(String text, CAction a)
	{
		CMenuItem m = new CMenuItem(text, a);
		add(m);
	}
	
	
	public void add(String text)
	{
		CMenuItem m = new CMenuItem(text);
		m.setDisable(true);
		add(m);
	}
	
	
	public void separator()
	{
		add(new SeparatorMenuItem());
	}
}
