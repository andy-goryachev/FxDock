// Copyright © 2020 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;


/**
 * FxToggleGroup.
 */
public class FxToggleGroup
	extends ToggleGroup
{
	public FxToggleGroup(ToggleButton ... buttons)
	{
		for(ToggleButton b: buttons)
		{
			b.setToggleGroup(this);
		}
	}

	
	public FxToggleGroup()
	{
	}
}
