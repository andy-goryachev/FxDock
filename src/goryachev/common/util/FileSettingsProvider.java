// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.io.File;


/**
 * File-based Settings Provider.
 */
public class FileSettingsProvider
    extends SettingsProviderBase
{
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
			log.err(e);
		}
	}
	
	
	public void load() throws Exception
	{
		String s = CKit.readString(file);
		loadFromString(s);
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
			log.err(e);
		}
	}
}
