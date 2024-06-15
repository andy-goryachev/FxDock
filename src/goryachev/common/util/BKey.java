// Copyright © 2011-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.util.Arrays;


/** Byte array based key */
public class BKey
{
	private byte[] key;
	private int hash;
	
	
	public BKey(byte[] k)
	{
		this.key = k.clone();
	}
	
	
	public BKey(BKey other)
	{
		this.key = other.key;
		this.hash = other.hash;
	}
	
	
	@Override
	public String toString()
	{
		return toHexString();
	}


	public String toHexString()
	{
		return Hex.toHexString(key);
	}
	
	
	public byte[] toByteArray()
	{
		return key.clone();
	}
	
	
	public int sizeInBytes()
	{
		return key.length;
	}


	public static BKey parse(Object x) throws Exception
	{
		if(x instanceof BKey)
		{
			return (BKey)x;
		}
		else if(x instanceof String)
		{
			String s = (String)x;
			byte[] b = Parsers.parseByteArray(s);
			return new BKey(b);
		}
		else if(x instanceof byte[])
		{
			return new BKey((byte[])x);
		}

		throw new Exception();
	}
	
	
	public static BKey parseQuiet(Object x)
	{
		try
		{
			return parse(x);
		}
		catch(Exception e)
		{ }
		
		return null;
	}
	
	
	public String getShortString()
	{
		SB sb = new SB();
		Hex.appendHexString(sb, key, 0, Math.min(5,  sizeInBytes()));
		return sb.toString();
	}

	
	@Override
	public boolean equals(Object x)
	{
		if(x == this)
		{
			return true;
		}
		else if(x instanceof BKey)
		{
			BKey other = (BKey)x;
			if(hashCode() == other.hashCode())
			{
				return Arrays.equals(key, other.key);
			}
		}
		
		return false;
	}
	
	
	@Override
	public int hashCode()
	{
		if(hash == 0)
		{
			int h = FH.hash(BKey.class);
			h = FH.hash(h, key);
			hash = h;
		}
		return hash;
	}
}
