// Copyright © 2012-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


/** String-based database identifier (key) */
public class SKey
	implements Cloneable, Comparable<SKey>
{
	public static interface Getter
	{
		public SKey getSKey();
	}
	
	//
	
	private String key;
	
	
	public SKey(String key)
	{
		this.key = key;
	}
	
	
	public static SKey format(String format, Object ... args)
	{
		String s = String.format(format, args);
		return new SKey(s);
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
		return FH.hash(SKey.class.hashCode(), key);
	}


	public boolean startsWith(String prefix)
	{
		return key.startsWith(prefix);
	}
	
	
	public boolean endsWith(String suffix)
	{
		return key.endsWith(suffix);
	}


	public int compareTo(SKey x)
	{
		return key.compareTo(x.key);
	}
}
