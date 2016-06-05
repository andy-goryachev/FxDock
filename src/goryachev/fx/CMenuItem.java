// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.scene.control.MenuItem;


/**
 * CMenuItem.
 */
public class CMenuItem
	extends MenuItem
{
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
