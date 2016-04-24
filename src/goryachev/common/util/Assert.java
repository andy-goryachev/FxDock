// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


/** Assert */
public class Assert
{
	public static <T> T notNull(T x)
	{
		if(x == null)
		{
			throw new IllegalArgumentException("must not be null");
		}
		return x;
	}
	
	
	public static <T> T notNull(T x, String name)
	{
		if(x == null)
		{
			throw new IllegalArgumentException(name + " must not be null");
		}
		return x;
	}


	public static void greaterThanZero(int x, String name)
	{
		if(x <= 0)
		{
			throw new IllegalArgumentException(name + " must be greater than 0 (" + x + ")");
		}
	}
	
	
	public static String notBlank(String x)
	{
		if(CKit.isBlank(x))
		{
			throw new IllegalArgumentException("must not be blank");
		}
		return x;
	}
	
	
	public static String notBlank(String x, String name)
	{
		if(CKit.isBlank(x))
		{
			throw new IllegalArgumentException(name + " must not be blank");
		}
		return x;
	}
}
