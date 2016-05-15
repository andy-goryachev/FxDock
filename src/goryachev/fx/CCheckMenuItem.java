// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.scene.control.CheckMenuItem;


/**
 * CheckMenuItem with action.
 */
public class CCheckMenuItem
	extends CheckMenuItem
{
	public CCheckMenuItem(String text)
	{
		super(text);
	}
	
	
	public CCheckMenuItem(String text, CAction a)
	{
		super(text);
		a.attach(this);
	}
}
