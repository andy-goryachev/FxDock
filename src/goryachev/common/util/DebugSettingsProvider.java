// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


/**
 * Debug Settings Provider.
 */
public class DebugSettingsProvider
    extends SettingsProviderBase
{
	public DebugSettingsProvider()
	{
	}
	

	public void save()
	{
		D.print("\n" + asString());
	}

	
	public synchronized String getString(String key)
	{
		String v = super.getString(key);
		D.print(key, v);
		return v;
	}


	public synchronized void setString(String key, String val)
	{
		super.setString(key, val);
		D.print(key, val);
	}
}
