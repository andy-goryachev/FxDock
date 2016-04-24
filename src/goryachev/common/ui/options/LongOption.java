// Copyright (c) 2005-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.options;
import goryachev.common.util.CSettings;
import java.util.Collection;


public class LongOption
	extends COption<Long>
{
	private Long defaultValue;


	public LongOption(String key, CSettings options, Collection<COption<?>> list)
	{
		super(key, options, list);
	}


	public LongOption(String key, long defaultValue)
	{
		super(key);
		this.defaultValue = defaultValue;
	}


	public Long defaultValue()
	{
		return defaultValue;
	}


	public Long parseProperty(String s)
	{
		try
		{
			return Long.parseLong(s);
		}
		catch(Exception e)
		{ }

		return defaultValue;
	}


	public String toProperty(Long val)
	{
		return String.valueOf(val);
	}
}
