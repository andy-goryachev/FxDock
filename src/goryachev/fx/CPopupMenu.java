// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;


/**
 * Dynamic popup menu.
 */
public abstract class CPopupMenu
	extends ContextMenu
{
	/**
	 * Implement this method to populate the menu dynamically.
	 * The menu is always cleared before invoking this method. 
	 * Use add* methods to build the content.
	 */
	protected abstract void populateMenu();
	
	//
	
	public CPopupMenu()
	{
	}
	
	
	public void clear()
	{
		getItems().clear();
	}


	public void show(Node anchor, double screenX, double screenY)
	{
		clear();
		populateMenu();
		super.show(anchor, screenX, screenY);
	}


	public void show(Node anchor, Side side, double dx, double dy)
	{
		clear();
		populateMenu();
		super.show(anchor, side, dx, dy);
	}


	public void add(String text, CAction a)
	{
		MenuItem m = new MenuItem(text);
		a.attach(m);
		add(m);
	}
	
	
	public void add(MenuItem m)
	{
		getItems().add(m);
	}
}
