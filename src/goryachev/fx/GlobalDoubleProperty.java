// Copyright © 2016-2021 Andy Goryachev <andy@goryachev.com>
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
	
	
	public GlobalDoubleProperty(String key, int defaultValue)
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
	public Object getBean()
	{
		return null;
	}


	public String getName()
	{
		return key;
	}
	
	
	public StringConverter<Number> getConverter()
	{
		return Converters.NUMBER_DOUBLE();
	}
}
