// Copyright © 2020-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.scene.Node;


/**
 * FxToggleButton that behaves like RadioButton.
 */
public class FxRadioToggleButton
	extends FxToggleButton
{
	public FxRadioToggleButton(String text, String tooltip)
	{
		super(text, tooltip);
	}
	
	
	public FxRadioToggleButton(String text, Node graphic)
	{
		super(text, graphic);
	}


	public void fire()
	{
		// behave like RadioButton
		if((getToggleGroup() == null) || !isSelected())
		{
			super.fire();
		}
	}
}
