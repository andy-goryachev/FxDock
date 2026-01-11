// Copyright © 2019-2025 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.beans.property.SimpleDoubleProperty;


/**
 * Alias for SimpleDoubleProperty.
 */
public class FxDouble
	extends SimpleDoubleProperty
{
	public FxDouble(double initialValue)
	{
		super(initialValue);
	}
	
	
	public FxDouble()
	{
	}
}
