// Copyright © 2020-2021 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;


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


	public void fire()
	{
		// behave like RadioButton
		if((getToggleGroup() == null) || !isSelected())
		{
			super.fire();
		}
	}
}
