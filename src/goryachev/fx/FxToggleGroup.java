// Copyright © 2020-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;


/**
 * Slightly more convenient ToggleGroup, 
 * ensures that one button is always selected.
 * 
 * WARNING: selectedToggleProperty() change event sometimes comes BEFORE
 * a linked toggle changes it selected property!
 * It is a goo idea to invokeLater() when listening to selectedToggleProperty.
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
		
		selectedToggleProperty().addListener((s, p, c) ->
		{
			if(c == null)
			{
				p.setSelected(true);
			}
		});
		
		ensureSelected(buttons);
	}
	
	
	protected static void ensureSelected(ToggleButton[] buttons)
	{
		if(buttons.length > 0)
		{
			for(ToggleButton b: buttons)
			{
				if(b.isSelected())
				{
					return;
				}
			}
			
			buttons[0].setSelected(true);
		}
	}

	
	public FxToggleGroup()
	{
	}
	
	
	public void add(ToggleButton b)
	{
		b.setToggleGroup(this);
	}
}
