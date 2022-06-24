// Copyright © 2019-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.beans.property.SimpleDoubleProperty;


/**
 * Alias for SimpleLongProperty.
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
