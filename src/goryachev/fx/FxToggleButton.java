// Copyright © 2019-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;


/**
 * Slightly more convenient ToggleButton.
 * 
 * When you need a toggle button that behave like RadioButtons in a group
 * (i.e. to keep one always selected), use FxRadioToggleButton. 
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
	
	
	public FxToggleButton(String text, FxAction a)
	{
		super(text);
		a.attach(this);
	}
	
	
	public FxToggleButton(String text, String tooltip, Property<Boolean> prop)
	{
		super(text);
		selectedProperty().bindBidirectional(prop);
		FX.setTooltip(this, tooltip);
	}
	
	
	public FxToggleButton(String text, String tooltip)
	{
		super(text);
		FX.setTooltip(this, tooltip);
	}
	
	
	public FxToggleButton(String text, String tooltip, FxAction a)
	{
		super(text);
		a.attach(this);
		FX.setTooltip(this, tooltip);
	}
	
	
	public FxToggleButton(String text, Node graphic)
	{
		super(text, graphic);
	}
}
