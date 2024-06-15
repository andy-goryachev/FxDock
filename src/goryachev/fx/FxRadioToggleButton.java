// Copyright © 2020-2024 Andy Goryachev <andy@goryachev.com>
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


	@Override
	public void fire()
	{
		// behave like RadioButton
		if((getToggleGroup() == null) || !isSelected())
		{
			super.fire();
		}
	}
}
