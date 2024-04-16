// Copyright © 2024 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


/**
 * Abstract Settings Store.
 */
public abstract class ASettingsStore
{
	public abstract void setStream(String key, SStream stream);

	
	public abstract SStream getStream(String key);
	
	
	public abstract void setString(String key, String val);
	
	
	public abstract String getString(String key);
	

	/** triggers saving.  this may not be necessary if saving is triggered automatically (after a short delay) */
	public abstract void save();
	
	
	//
	
	
	public ASettingsStore()
	{
	}
	
	
	public int getInt(String key, int defaultValue)
	{
		return Parsers.parseInt(getString(key), defaultValue);
	}
	
	
	public void setInt(String key, int val)
	{
		setString(key, String.valueOf(val));
	}
	
	
	public void setBoolean(String key, boolean value)
	{
		setString(key, String.valueOf(value));
	}


	public Boolean getBoolean(String key)
	{
		String v = getString(key);
		if(v != null)
		{
			if("true".equals(v))
			{
				return Boolean.TRUE;
			}
			else if("false".equals(v))
			{
				return Boolean.FALSE;
			}
		}
		return null;
	}
	
	
	public Boolean getBoolean(String key, boolean defaultValue)
	{
		 Boolean b = getBoolean(key);
		 if(b == null)
		 {
			 return defaultValue;
		 }
		 return b.booleanValue();
	}
}
