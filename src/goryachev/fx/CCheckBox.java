// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.scene.control.CheckBox;


/**
 * CCheckBox.
 */
public class CCheckBox
	extends CheckBox
{
	public CCheckBox(String text, boolean selected)
	{
		super(text);
		setSelected(selected);
	}
	
	
	public CCheckBox(boolean selected)
	{
		setSelected(selected);
	}

	
	public CCheckBox(String text)
	{
		super(text);
	}
	
	
	public CCheckBox()
	{
	}
}
