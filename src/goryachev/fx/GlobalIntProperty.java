// Copyright © 2016-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.beans.property.IntegerPropertyBase;
import javafx.util.StringConverter;


/**
 * Global Boolean Property.
 */
public class GlobalIntProperty
	extends IntegerPropertyBase
	implements GlobalProperty<Number>
{
	private final String key;
	
	
	public GlobalIntProperty(String key, int defaultValue)
	{
		super(defaultValue);
		this.key = key;
		
		GlobalProperties.add(this);
	}
	
	
	public GlobalIntProperty(String key)
	{
		this(key, 0);
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
	public StringConverter<Number> getConverter()
	{
		return Converters.NUMBER_INT();
	}
}
