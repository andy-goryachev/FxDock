// Copyright © 2018-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.beans.property.SimpleObjectProperty;


/**
 * Alias for SimpleLongProperty.
 */
public class FxObject<T>
	extends SimpleObjectProperty<T>
{
	public FxObject(T initialValue)
	{
		super(initialValue);
	}
	
	
	public FxObject()
	{
	}
}
