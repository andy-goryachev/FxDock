// Copyright (c) 2011-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


public interface CSettingsProvider
{
	public String getProperty(String key);


	public void setProperty(String key, String value);


	public CList<String> getPropertyNames();


	public void save() throws Exception;
}
