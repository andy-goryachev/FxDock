// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.util.List;


/**
 * Debug Settings Provider.
 */
public class DebugSettingsProvider
    extends SettingsProviderBase
{
	protected static final CLog log = Log.get("DebugSettingsProvider");
	private CMap<String,String> data = new CMap();
	
	
	public DebugSettingsProvider()
	{
	}
	

	public void save()
	{
		D.print("\n" + asString());
	}

	
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


	public SStream getStream(String key)
	{
		String s = getString(key);
		return parseStream(s);
	}


	public void setStream(String key, SStream s)
	{
		setString(key, asString(s));
	}
	
	
	public List<String> getKeys()
	{
		return data.keys();
	}
	
	
	public synchronized String asString()
	{
		List<String> keys = getKeys();
		CSorter.sort(keys);
		
		SB sb = new SB(keys.size() * 128);
		for(String k: keys)
		{
			String v = data.get(k);
			sb.a(k);
			sb.a('=');
			sb.a(v);
			sb.nl();
		}
		return sb.toString();
	}
}
