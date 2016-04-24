// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


public class Debug
{
	public static final String PROPERTY_NAME = "goryachev.debug";
	private static Boolean enabled;
	
	
	public static synchronized boolean is()
	{
		if(enabled == null)
		{
			enabled = (System.getProperty(PROPERTY_NAME) != null);
		}
		return enabled;
	}
}
