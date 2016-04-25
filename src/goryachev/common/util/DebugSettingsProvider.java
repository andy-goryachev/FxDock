// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


/**
 * DebugSettingsProvider.
 */
public class DebugSettingsProvider
    implements GlobalSettings.Provider
{
	protected static final CLog log = Log.get("DebugSettingsProvider");
	private CMap<String,String> data = new CMap();
	
	
	public synchronized String getString(String key)
	{
		String v = data.get(key);
		log.print(key, v);
		D.print(key, v);
		return v;
	}


	public synchronized void setString(String key, String val)
	{
		data.put(key, val);
		log.print(key, val);
		D.print(key, val);
	}


	public void save()
	{
		log.print();
		D.print();
	}
}
