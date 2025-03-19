// Copyright © 2018-2025 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.beans.property.ReadOnlyObjectWrapper;


/**
 * Alias for ReadOnlyObjectWrapper.
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
