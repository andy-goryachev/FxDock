// Copyright © 2018-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.beans.property.SimpleLongProperty;


/**
 * Alias for SimpleLongProperty.
 */
public class FxLong
	extends SimpleLongProperty
{
	public FxLong(long initialValue)
	{
		super(initialValue);
	}
	
	
	public FxLong()
	{
	}
}
