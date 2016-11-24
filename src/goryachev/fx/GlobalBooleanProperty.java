// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.beans.property.BooleanPropertyBase;
import javafx.util.StringConverter;


/**
 * Global Boolean Property.
 */
public class GlobalBooleanProperty
	extends BooleanPropertyBase
	implements GlobalProperty<Boolean>
{
	private final String key;
	
	
	public GlobalBooleanProperty(String key, boolean defaultValue)
	{
		super(defaultValue);
		this.key = key;
		
		GlobalProperties.add(this);
	}


	/** who knows what this is for */
	public Object getBean()
	{
		return null;
	}


	public String getName()
	{
		return key;
	}
	
	
	public StringConverter<Boolean> getConverter()
	{
		return Converters.BOOLEAN();
	}
}
