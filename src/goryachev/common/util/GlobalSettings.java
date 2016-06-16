// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.io.File;
import java.util.List;


/**
 * Application-wide Settings.
 */
public class GlobalSettings
{
	/** settings storage interface */
	public interface Provider
	{
		public String getString(String key);
		
		public void setString(String key, String val);
		
		public SStream getStream(String key);
		
		public void setStream(String key, SStream s);
		
		public List<String> getKeys();
		
		public void save();
	}
	
	//
	
	private static Provider provider;
	
	
	public static void setProvider(Provider p)
	{
		provider = p;
	}
	
	
	/** a convenience shortcut to set file-based provider and load the settings */
	public static void setFileProvider(File f)
	{
		FileSettingsProvider p = new FileSettingsProvider(f);
		p.loadQuiet();
		setProvider(p);
	}
	
	
	private static Provider provider()
	{
		if(provider == null)
		{
			throw new NullPointerException("GlobalSettings.setProvider()");
		}
		return provider;
	}
	
	
	public static void save()
	{
		provider().save();
	}
	
	
	public static List<String> getKeys()
	{
		return provider().getKeys();
	}
	
	
	public static String getString(String key)
	{
		return provider().getString(key);
	}
	
	
	public static void setString(String key, String val)
	{
		set(key, val);
	}
	
	
	private static void set(String key, Object val)
	{
		String s = val == null ? null : val.toString();
		provider().setString(key, s);
	}
	
	
	public static int getInt(String key, int defaultValue)
	{
		return Parsers.parseInt(getString(key), defaultValue);
	}
	
	
	public static void setInt(String key, int val)
	{
		set(key, val);
	}
	
	
	public static SStream getStream(String key)
	{
		return provider().getStream(key);
	}
	
	
	public static void setStream(String key, SStream s)
	{
		provider().setStream(key, s);
	}
}
