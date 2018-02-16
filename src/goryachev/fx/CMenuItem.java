// Copyright © 2016-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;


/**
 * CMenuItem.
 */
public class CMenuItem
	extends MenuItem
{
	public CMenuItem(String text, Node icon, CAction a)
	{
		super(text);
		a.attach(this);
		setGraphic(icon);
	}
	
	
	public CMenuItem(Node icon, CAction a)
	{
		a.attach(this);
		setGraphic(icon);
	}
	
	
	public CMenuItem(String text, CAction a)
	{
		super(text);
		a.attach(this);
	}
	
	
	public CMenuItem(String text)
	{
		super(text);
	}
}
