// Copyright © 2012-2021 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


public class CancelledException
	extends RuntimeException
{
	public CancelledException()
	{
	}
	
	
	public CancelledException(Throwable cause)
	{
		super(cause);
	}
	
	
	public CancelledException(String message)
	{
		super(message);
	}
	
	
	public CancelledException(String message, Throwable cause)
	{
		super(message, cause);
	}


	public static boolean isNot(Throwable e)
	{
		return !is(e);
	}


	public static boolean is(Throwable e)
	{
		return (e instanceof CancelledException);
	}
}
