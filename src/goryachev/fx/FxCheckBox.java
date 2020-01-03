// Copyright © 2016-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.scene.control.CheckBox;


/**
 * CCheckBox.
 */
public class FxCheckBox
	extends CheckBox
{
	public FxCheckBox(String text, boolean selected)
	{
		super(text);
		setSelected(selected);
	}
	
	
	public FxCheckBox(boolean selected)
	{
		setSelected(selected);
	}

	
	public FxCheckBox(String text)
	{
		super(text);
	}
	
	
	public FxCheckBox()
	{
	}
}
