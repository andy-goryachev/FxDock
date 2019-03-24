// Copyright © 2005-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.io.File;
import java.util.Hashtable;


/** utility class helps storing/restoring various binary values */
public class CSettings
{
	public interface Provider
	{
		public String getProperty(String key);

		public void setProperty(String key, String value);

		public CList<String> getPropertyNames();

		public void save() throws Exception;
	}
	
	//
	
	public static final Provider NONE = new Provider()
	{
		public String getProperty(String key) { return null; }
		public void setProperty(String key, String value) { }
		public CList<String> getPropertyNames() { return new CList(0); }
		public void save() throws Exception { }
	};
	private Provider provider = NONE;
	
	/** in-memory settings based on hashtable*/
	public static class CMAP extends CSettings
	{		
		public CMAP()
		{
			super(new Provider()
			{
				protected final Hashtable<String,Object> settings = new Hashtable();

				public String getProperty(String key) { return Parsers.parseString(settings.get(key)); }
				public void setProperty(String key, String value) { settings.put(key, value); }
				public CList<String> getPropertyNames() { return new CList(settings.keySet()); }
				public void save() throws Exception { }
			});
		};
	}
	
	
	//
	
	
	public CSettings()
	{
	}
	
	
	public CSettings(Provider p)
	{
		setProvider(p);
	}
	
	
	public void setProvider(Provider p)
	{
		this.provider = p;
	}
	
	
	public CSettings.Provider getProvider()
	{
		return provider;
	}
	
	
	public String getProperty(String key)
	{
		return provider.getProperty(key);
	}
	
	
	public void setProperty(String key, String value)
	{
		provider.setProperty(key, value);
	}
	
	
	public void save() throws Exception
	{
		provider.save();
	}
	
	
	public void set(String key, Object value)
	{
		setProperty(key, value == null ? null : value.toString());
	}
	
	
	public void setBoolean(String key, boolean val)
	{
		set(key, val);
	}
	
	
	public void setBool(String key, boolean val)
	{
		set(key, val);
	}
	
	
	public void setInt(String key, int val)
	{
		set(key, val);
	}
	
	
	public void setLong(String key, long val)
	{
		set(key, val);
	}
	
	
	public Boolean getBoolean(String key)
	{
		String s = getProperty(key);
		if(s == null)
		{
			return null;
		}
		else
		{
			return Parsers.parseBoolean(s);
		}
	}
	
	
	public boolean getBool(String key, boolean defaultValue)
	{
		Boolean b = getBoolean(key);
		if(b == null)
		{
			return defaultValue;
		}
		else
		{
			return b.booleanValue();
		}
	}
	
	
	public boolean getBool(String key)
	{
		return Boolean.TRUE.equals(getBoolean(key));
	}
	
	
	public int getInt(String key, int defaultValue)
	{
		String s = getProperty(key);
		if(s != null)
		{
			try
			{
				return Integer.parseInt(s);
			}
			catch(Exception e)
			{ }
		}
		return defaultValue;
	}
	
	
	public long getLong(String key, long defaultValue)
	{
		String s = getProperty(key);
		if(s != null)
		{
			try
			{
				return Long.parseLong(s);
			}
			catch(Exception e)
			{ }
		}
		return defaultValue;
	}
	
	
	public String getProperty(String key, String defaultValue)
	{
		String s = getProperty(key);
		if(s == null)
		{
			return defaultValue;
		}
		else
		{
			return s;
		}
	}
	
	
	public String[] getList(String key)
	{
		String s = getProperty(key);
		if(s == null)
		{
			return CKit.emptyStringArray;
		}
		else
		{
			// TODO should support embedded commas
			return CKit.split(s, ",");
		}
	}
	

	public static CSettings loadFromFile(String filename)
	{
		return loadFromFile(new File(filename));
	}
	
	
	public static CSettings loadFromFile(File f)
	{
		return new CSettings(CFileSettings.loadQuiet(f));
	}
	
	
	public File getFile(String key)
	{
		return Parsers.parseFile(getProperty(key));
	}
	
	
	public File getFile(String key, File defaultValue)
	{
		String s = getProperty(key);
		if(s == null)
		{
			return defaultValue;
		}
		return Parsers.parseFile(s);
	}
	
	
	public void setFile(String key, File f)
	{
		setProperty(key, f == null ? null : f.getAbsolutePath());
	}
	
	
	public <T extends Enum> T getEnum(String key, Class<T> type)
	{
		return getEnum(key, type, null);
	}
	
	
	public <T extends Enum> T getEnum(String key, Class<T> type, T defaultValue)
	{
		String s = getProperty(key);
		if(s != null)
		{
			try
			{
				return (T)Enum.valueOf(type, s);
			}
			catch(Exception ignore)
			{
			}
		}
		return defaultValue;
	}
	
	
	public <T extends Enum> void setEnum(String key, T value)
	{
		setProperty(key, value == null ? null : value.toString());
	}
}
