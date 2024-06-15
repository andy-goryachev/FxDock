// Copyright © 2016-2024 Andy Goryachev <andy@goryachev.com>
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
	
	
	public GlobalBooleanProperty(String key)
	{
		this(key, false);
	}


	/** who knows what this is for */
	@Override
	public Object getBean()
	{
		return null;
	}


	@Override
	public String getName()
	{
		return key;
	}
	
	
	@Override
	public StringConverter<Boolean> getConverter()
	{
		return Converters.BOOLEAN();
	}
}
