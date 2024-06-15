// Copyright © 2016-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.util.StringConverter;


/**
 * A StringConverter extension.
 */
public abstract class FxFormatter
	extends StringConverter<Object>
{
	@Override
	public abstract String toString(Object x);
	
	//
	
	public FxFormatter()
	{
	}
	
 
    @Override
	public Object fromString(String string)
    {
    	throw new Error("FxFormatter: fromString not supported");
    }
    
    
    public String format(Object x)
    {
    	return toString(x);
    }
}
