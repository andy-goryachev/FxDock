// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import goryachev.common.io.BitStream;


/** String-based database identifier (key) */
public class SKey
{
	private String key;
	
	
	public SKey(String key)
	{
		this.key = key;
	}
	
	
	public String toString()
	{
		return key;
	}
	
	
	/** accepts either an SKey or a hex representation obtained via asString() */
	public static SKey parse(Object x) throws Exception
	{
		if(x instanceof SKey)
		{
			return (SKey)x;
		}
		else if(x instanceof String)
		{
			return new SKey((String)x);
		}

		throw new Exception();
	}
	
	
	public static SKey parseQuiet(Object x)
	{
		try
		{
			return parse(x);
		}
		catch(Exception e)
		{ }
		
		return null;
	}
	
	
	public BitStream getBitStream()
	{
		CDigest md = new CDigest.SHA512();
		byte[] b = md.digest(key);
		return new BitStream(b);
	}
	
	
	public boolean equals(Object x)
	{
		if(x == this)
		{
			return true;
		}
		else if(x instanceof SKey)
		{
			return key.equals(((SKey)x).key);
		}
		else
		{
			return false;
		}
	}
	
	
	public int hashCode()
	{
		return SKey.class.hashCode() ^ key.hashCode();
	}


	public boolean startsWith(String prefix)
	{
		return key.startsWith(prefix);
	}
	
	
	public boolean endsWith(String suffix)
	{
		return key.endsWith(suffix);
	}
}
