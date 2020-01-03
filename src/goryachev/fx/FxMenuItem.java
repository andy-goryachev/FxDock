// Copyright © 2016-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;


/**
 * CMenuItem.
 */
public class FxMenuItem
	extends MenuItem
{
	public FxMenuItem(String text, Node icon, FxAction a)
	{
		super(text);
		a.attach(this);
		setGraphic(icon);
	}
	
	
	public FxMenuItem(Node icon, FxAction a)
	{
		a.attach(this);
		setGraphic(icon);
	}
	
	
	public FxMenuItem(String text, FxAction a)
	{
		super(text);
		a.attach(this);
	}
	
	
	public FxMenuItem(String text, Runnable r)
	{
		super(text);
		new FxAction(r).attach(this);
	}
	
	
	public FxMenuItem(String text)
	{
		super(text);
		setDisable(true);
	}
}
