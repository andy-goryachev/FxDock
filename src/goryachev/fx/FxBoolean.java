// Copyright © 2018-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.beans.property.ReadOnlyBooleanWrapper;


/**
 * Alias for SimpleBooleanProperty.
 */
public class FxBoolean
	extends ReadOnlyBooleanWrapper
{
	public FxBoolean(boolean initialValue)
	{
		super(initialValue);
	}
	
	
	public FxBoolean()
	{
	}
}
