// Copyright © 2018-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.beans.property.ReadOnlyObjectWrapper;


/**
 * Alias for SimpleLongProperty.
 */
public class FxObject<T>
	extends ReadOnlyObjectWrapper<T>
{
	public FxObject(T initialValue)
	{
		super(initialValue);
	}
	
	
	public FxObject()
	{
	}
}
