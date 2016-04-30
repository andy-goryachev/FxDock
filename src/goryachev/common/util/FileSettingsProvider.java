// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.io.File;


/**
 * File-based Settings Provider.
 */
public class FileSettingsProvider
    extends SettingsProviderBase
{
	public FileSettingsProvider()
	{
	}
	

	public void save()
	{
		// TODO
	}
	
	
	public void load(File f) throws Exception
	{
		// TODO
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
