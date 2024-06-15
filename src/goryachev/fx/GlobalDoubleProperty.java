// Copyright © 2016-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.beans.property.DoublePropertyBase;
import javafx.util.StringConverter;


/**
 * Global Boolean Property.
 */
public class GlobalDoubleProperty
	extends DoublePropertyBase
	implements GlobalProperty<Number>
{
	private final String key;
	
	
	public GlobalDoubleProperty(String key, double defaultValue)
	{
		super(defaultValue);
		this.key = key;
		
		GlobalProperties.add(this);
	}
	
	
	public GlobalDoubleProperty(String key)
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
		return Converters.NUMBER_DOUBLE();
	}
}
