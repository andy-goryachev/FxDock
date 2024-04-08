// Copyright © 2024 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.util.List;


/**
 * Global Settings Provider.
 */
public interface GlobalSettingsProvider
{
	public String getString(String key);
	
	public void setString(String key, String val);
	
	public SStream getStream(String key);
	
	public void setStream(String key, SStream s);
	
	public List<String> getKeys();
	
	public void save();
}