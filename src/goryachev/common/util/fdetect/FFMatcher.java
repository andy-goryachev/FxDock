// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.fdetect;
import goryachev.common.util.CKit;
import goryachev.common.util.Parsers;
import goryachev.common.util.Rex;


public abstract class FFMatcher
{
	public abstract FileType match(String filename, byte[] b);
	
	
	//
	
	
	public static byte[] bytes(String hex)
	{
		byte[] b = Parsers.parseByteArray(hex);
		if(b == null)
		{
			throw new Rex("unable to parse: " + hex);
		}
		return b;
	}
	
	
	public static boolean match(byte[] b, String pattern)
	{
		return match(b, bytes(pattern));
	}
	
	
	public static boolean match(byte[] b, byte[] pattern)
	{
		return match(b, pattern, 0);
	}
	
	
	public static boolean match(byte[] bytes, byte[] pattern, int offset)
	{
		if(bytes.length < (pattern.length + offset))
		{
			return false;
		}
		
		for(int i=0; i<pattern.length; i++)
		{
			if(bytes[i + offset] != pattern[i])
			{
				return false;
			}
		}
		
		return true;
	}
	
	
	public static boolean ext(String filename, String ext)
	{
		return CKit.endsWithIgnoreCase(filename, ext);
	}


	public static boolean match(byte[] bytes, int index, int value)
	{
		int v = getByte(bytes, index);
		return v == value;
	}


	public static int getByte(byte[] bytes, int index)
	{
		if(index < bytes.length)
		{
			return (bytes[index] & 0xff);
		}
		return -1;
	}
}