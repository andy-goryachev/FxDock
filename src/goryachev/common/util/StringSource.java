// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


/** helpful class to use while parsing strings (including null strings) */
public class StringSource
{
	private final String text;
	
	
	public StringSource(String s)
	{
		this.text = s;
	}
	
	
	public int size()
	{
		return text == null ? 0 : text.length();
	}
	
	
	public int length()
	{
		return size();
	}
	
	
	/** returns a char at specified index or -1 if index goes beyond the string boundary */
	public int charAt(int ix)
	{
		if(text == null)
		{
			return -1;
		}
		else if(ix < 0)
		{
			return -1;
		}
		else if(ix >= text.length())
		{
			return -1;
		}
		else
		{
			return text.charAt(ix);
		}
	}
	
	
	/** returns a substring or null if any indexes go beyond the original string */
	public String substring(int start, int len)
	{
		if(text == null)
		{
			return null;
		}
		else if(start < 0)
		{
			return null;
		}
		else if((start + len) > text.length())
		{
			return null;
		}
		else
		{
			return text.substring(start, start + len);
		}
	}
	
	
	public String toString()
	{
		return text;
	}
}
