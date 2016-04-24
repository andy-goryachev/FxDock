// Copyright (c) 2014-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.options;
import goryachev.common.util.CKit;
import goryachev.common.util.CSettings;
import goryachev.common.util.Hex;
import goryachev.common.util.HiddenData;
import goryachev.common.util.Log;
import java.util.Collection;


/** an option to store obfuscated char[] string */
public class HiddenCharArrayOption
	extends COption<char[]>
{
	public HiddenCharArrayOption(String key, CSettings options, Collection<COption<?>> list)
	{
		super(key, options, list);
	}


	public HiddenCharArrayOption(String key)
	{
		super(key);
	}


	public char[] defaultValue()
	{
		return null;
	}


	public char[] parseProperty(String s)
	{
		if(s == null)
		{
			return null;
		}
		else
		{
			return decrypt(s);
		}
	}


	public String toProperty(char[] value)
	{
		return value == null ? null : encrypt(value);
	}
	
	
	protected char[] decrypt(String s)
	{
		try
		{
			byte[] b = Hex.parseByteArray(s);
			byte[] dec = HiddenData.decode(b);
			return new String(dec, CKit.CHARSET_UTF8).toCharArray();
		}
		catch(Exception e)
		{
			Log.err(e);
			return null;
		}
	}
	
	
	protected String encrypt(char[] cs)
	{
		try
		{
			String s = new String(cs);
			byte[] b = s.getBytes(CKit.CHARSET_UTF8);
			byte[] enc = HiddenData.encode(b);
			return Hex.toHexString(enc);
		}
		catch(Exception e)
		{
			Log.err(e);
			return null;
		}
	}
}
