// Copyright © 2016-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.beans.property.StringPropertyBase;
import javafx.util.StringConverter;


/**
 * Global String Property.
 */
public class GlobalStringProperty
	extends StringPropertyBase
	implements GlobalProperty<String>
{
	private final String key;
	
	
	public GlobalStringProperty(String key, String defaultValue)
	{
		super(defaultValue);
		this.key = key;
		
		GlobalProperties.add(this);
	}
	
	
	public GlobalStringProperty(String key)
	{
		this(key, null);
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
	public StringConverter<String> getConverter()
	{
		return Converters.STRING();
	}
}
