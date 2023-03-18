// Copyright © 2011-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


public class UserException
	extends RuntimeException
{
	private String title;
	
	
	public UserException(String title, String message, Throwable cause)
	{
		super(message, cause);
		this.title = title;
	}
	
	
	public UserException(String title, String message)
	{
		super(message);
		this.title = title;
	}
	
	
	public UserException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	
	public UserException(String message)
	{
		super(message);
	}

	
	public String getTitle()
	{
		return title;
	}
}
