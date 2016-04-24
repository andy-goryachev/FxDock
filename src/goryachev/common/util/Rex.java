// Copyright (c) 2015-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


/** "RuntimeException" is too long */
public class Rex
	extends RuntimeException
{
	public Rex()
	{
	}
	
	
	public Rex(Object message)
	{
		super(String.valueOf(message));
	}
	
	
	public Rex(Object message, Throwable cause)
	{
		super(String.valueOf(message), cause);
	}
	
	
	public Rex(Throwable cause)
	{
		super(cause);
	}
}
