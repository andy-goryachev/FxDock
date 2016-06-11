// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.geometry.Side;
import javafx.scene.Node;


/**
 * Dynamic popup menu.
 */
public abstract class CPopupMenu
	extends CContextMenu
{
	/**
	 * Implement this method to populate the menu dynamically.
	 * The menu is always cleared before invoking this method. 
	 * Use add* methods to build the content.
	 */
	protected abstract void createPopupMenu();
	
	//
	
	public CPopupMenu()
	{
	}
	
	
	public CPopupMenu(Node n)
	{
		attach(n);
	}
	
	
	public void attach(Node n)
	{
		n.setOnContextMenuRequested((ev) ->
		{
			// later or not?  it stills throws an exception in a tableview
			FX.later(() ->
			{
				clear();
				createPopupMenu();
				
				show(n, ev.getScreenX(), ev.getScreenY());
			});
			ev.consume();
		});
	}
	
	
	public void clear()
	{
		getItems().clear();
	}


	public void show(Node anchor, double screenX, double screenY)
	{
		clear();
		createPopupMenu();
		super.show(anchor, screenX, screenY);
	}


	public void show(Node anchor, Side side, double dx, double dy)
	{
		clear();
		createPopupMenu();
		super.show(anchor, side, dx, dy);
	}
}
