// Copyright © 2019 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.beans.property.Property;
import javafx.scene.control.ToggleButton;


/**
 * Fx ToggleButton.
 */
public class FxToggleButton
	extends ToggleButton
{
	public FxToggleButton(String text)
	{
		super(text);
	}
	
	
	public FxToggleButton(String text, Property<Boolean> prop)
	{
		super(text);
		selectedProperty().bindBidirectional(prop);
	}
}
