// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.io.WebReader;
import goryachev.common.util.DotSeparatedVersion;
import goryachev.common.util.Log;
import goryachev.common.util.TXT;
import goryachev.common.util.UserException;
import java.io.FileNotFoundException;
import java.net.URL;


/**
 * Retrieves the content with the specified URL as a UTF-8 string
 */ 
public class CheckForUpdate
{
	private final String url;
	private final WebReader reader;
	private volatile String siteVersion;
	
	
	public CheckForUpdate(String url)
	{
		this.url = url;
		this.reader = new WebReader();
	}
	
	
	public void readWeb() throws Exception
	{
		try
		{
			siteVersion = reader.readString(url);
		}
		catch(FileNotFoundException e)
		{
			Log.err(e);
			URL u = new URL(url);
			throw new UserException(TXT.get("CheckForUpdate.err.server problem", "Sorry, there seems to be a problem contacting server [{0}]", u.getHost()));
		}
	}
	
	
	public String getLatestVersion()
	{
		return siteVersion;
	}
	

	public boolean isUpdateAvailable(String currentVersion)
	{
		try
		{
			if(DotSeparatedVersion.compare(siteVersion, currentVersion) > 0)
			{
				return true;
			}
		}
		catch(Exception e)
		{ }
		
		return false;
	}
}
