// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.scene.control.Menu;
import javafx.scene.control.Separator;


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
	
	
	public Separator addSeparator()
	{
		Separator s = new Separator();
		// FIX getItems().add(s);
		return s;
	}
	
	
	public CMenuItem add(String text, CAction a)
	{
		CMenuItem m = new CMenuItem(text, a);
		getItems().add(m);
		return m;
	}
}
