// Copyright © 2016-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import goryachev.common.log.Log;
import java.io.File;
import java.io.FileNotFoundException;


/**
 * File-based Settings Provider.
 */
public class FileSettingsProvider
    extends SettingsProviderBase
{
	protected static final Log log = Log.get("FileSettingsProvider");
	private File file;
	
	
	public FileSettingsProvider(File f)
	{
		setFile(f);
	}
	
	
	public void setFile(File f)
	{
		file = f;
	}
	

	public void save()
	{
		try
		{
			String s = asString();
			CKit.write(file, s);
		}
		catch(Exception e)
		{
			log.error(e);
		}
	}
	
	
	public void load() throws Exception
	{
		try
		{
			String s = CKit.readString(file);
			loadFromString(s);
		}
		catch(FileNotFoundException ignore)
		{
		}
	}
	
	
	public void loadQuiet()
	{
		try
		{
			load();
		}
		catch(Exception e)
		{
			log.error(e);
		}
	}
	
	
	public void load(File f) throws Exception
	{
		setFile(f);
		load();
	}
	
	
	public void loadQuiet(File f)
	{
		try
		{
			load(f);
		}
		catch(Exception e)
		{
			log.error(e);
		}
	}
}
