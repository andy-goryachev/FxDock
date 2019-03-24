// Copyright © 2018-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.beans.property.SimpleBooleanProperty;


/**
 * Alias for SimpleBooleanProperty.
 */
public class FxBoolean
	extends SimpleBooleanProperty
{
	public FxBoolean(boolean initialValue)
	{
		super(initialValue);
	}
	
	
	public FxBoolean()
	{
	}
}
