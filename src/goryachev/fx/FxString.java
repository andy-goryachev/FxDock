// Copyright © 2018-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.beans.property.SimpleStringProperty;


/**
 * Alias for SimpleStringProperty.
 */
public class FxString
	extends SimpleStringProperty
{
	public FxString(String initialValue)
	{
		super(initialValue);
	}
	
	
	public FxString()
	{
	}
}
