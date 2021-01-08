// Copyright © 2009-2021 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.util.Arrays;


/**
 * Encapsulated a byte array for use as a hashtable key.
 * Assumes (but does not enforce) that the underlying byte array is immutable.
 */
public class ByteArray
{
	private int hash;
	private byte[] bytes;
	
	
	public ByteArray(byte[] b)
	{
		bytes = b;
	}
	
	
	public int size()
	{
		return bytes.length;
	}
	
	
	public int length()
	{
		return bytes.length;
	}
	
	
	public byte get(int n)
	{
		return bytes[n];
	}
	
	
	public byte[] getArray()
	{
		return bytes;
	}
	
	
	public int hashCode()
	{
		if(hash == 0)
		{
			hash = ByteArray.class.hashCode() ^ Arrays.hashCode(bytes);
		}
		return hash;
	}
	
	
	public boolean equals(Object x)
	{
		if(x == this)
		{
			return true;
		}
		else if(x instanceof ByteArray)
		{
			ByteArray z = (ByteArray)x;
			if(hashCode() != z.hashCode())
			{
				return false;
			}
			else
			{
				return Arrays.equals(bytes, z.bytes);
			}
		}
		else
		{
			return false;
		}
	}
}
