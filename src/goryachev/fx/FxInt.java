// Copyright © 2018-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;


/**
 * Alias for SimpleIntegerProperty.
 */
public class FxInt
	extends SimpleIntegerProperty
{
	public FxInt(int initialValue)
	{
		super(initialValue);
	}
	
	
	public FxInt()
	{
	}
	
	
	/** WARNING: potential loss of data */
	public void set(Number n)
	{
		if(n != null)
		{
			set(n.intValue());
		}
	}
}
