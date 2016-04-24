// Copyright (c) 2005-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.options;
import goryachev.common.util.CSettings;
import java.util.Collection;


public class CharArrayOption
	extends COption<char[]>
{
	private char[] defaultValue;


	public CharArrayOption(String key, CSettings options, Collection<COption<?>> list)
	{
		super(key, options, list);
	}


	public CharArrayOption(String key)
	{
		super(key);
	}


	public CharArrayOption(String key, char[] defaultValue)
	{
		super(key);
		this.defaultValue = defaultValue;
	}


	public char[] defaultValue()
	{
		return defaultValue;
	}


	public char[] parseProperty(String s)
	{
		if(s == null)
		{
			return null;
		}
		else
		{
			return s.toCharArray();
		}
	}


	public String toProperty(char[] value)
	{
		return value == null ? null : new String(value);
	}
}
