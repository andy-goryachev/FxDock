// Copyright (c) 2005-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.options;
import goryachev.common.util.CSettings;
import java.util.Collection;


public class StringOption
	extends COption<String>
{
	private String defaultValue;


	public StringOption(String key, CSettings options, Collection<COption<?>> list)
	{
		super(key, options, list);
	}


	public StringOption(String key)
	{
		super(key);
	}


	public StringOption(String key, String defaultValue)
	{
		super(key);
		this.defaultValue = defaultValue;
	}


	public String defaultValue()
	{
		return defaultValue;
	}


	public String parseProperty(String s)
	{
		return s;
	}


	public String toProperty(String value)
	{
		return value;
	}
}
