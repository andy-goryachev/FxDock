// Copyright © 2016-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.io.File;
import java.util.Collection;
import java.util.List;


/**
 * Application-wide settings.
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
	
	
	public static File getFile(String key)
	{
		String s = getString(key);
		if(s != null)
		{
			return new File(s);
		}
		return null;
	}
	
	
	public static void setFile(String key, File f)
	{
		String s = (f == null ? null : f.getAbsolutePath());
		setString(key, s);
	}
	
	
	public static List<File> getFiles(String key)
	{
		SStream ss = GlobalSettings.getStream(key);
		int sz = ss.size();
		CList<File> list = new CList<>(sz);
		for(int i=0; i<sz; i++)
		{
			String s = ss.nextString();
			list.add(s == null ? null : new File(s));
		}
		return list;
	}
	
	
	public static void setFiles(String key, Collection<File> files)
	{
		SStream ss = new SStream();
		if(files != null)
		{
			for(File f: files)
			{
				ss.add(f);
			}
		}
		setStream(key, ss);
	}
}
