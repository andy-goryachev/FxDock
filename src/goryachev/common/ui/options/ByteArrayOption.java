// Copyright (c) 2005-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.options;
import goryachev.common.util.CSettings;
import goryachev.common.util.Hex;
import goryachev.common.util.Parsers;
import java.util.Collection;


public class ByteArrayOption
	extends COption<byte[]>
{
	private byte[] defaultValue;


	public ByteArrayOption(String key, CSettings options, Collection<COption<?>> list)
	{
		super(key, options, list);
	}


	public ByteArrayOption(String key)
	{
		super(key);
	}


	public ByteArrayOption(String key, byte[] defaultValue)
	{
		super(key);
		this.defaultValue = defaultValue;
	}


	public byte[] defaultValue()
	{
		return defaultValue;
	}


	public byte[] parseProperty(String s)
	{
		return Parsers.parseByteArray(s);
	}


	public String toProperty(byte[] value)
	{
		return Hex.toHexString(value);
	}
}
