// Copyright © 2007-2021 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.platform;
import goryachev.common.log.Log;
import goryachev.common.util.CKit;
import goryachev.common.util.CPlatform;
import java.io.File;
import java.nio.charset.Charset;


public class CPlatformWindows
	extends CPlatform
{
	private static final String REGQUERY_UTIL = "reg query ";
	private static final String REGSTR_TOKEN = "REG_SZ";
	private static final String DESKTOP_FOLDER_CMD = REGQUERY_UTIL + "\"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Shell Folders\" /v DESKTOP";
	protected static final Log log = Log.get("CPlatformWindows");


	public CPlatformWindows()
	{
	}


	// http://stackoverflow.com/questions/1080634/how-to-get-the-desktop-path-in-java
	public static File getCurrentUserDesktop()
	{
		try
		{
			Process process = Runtime.getRuntime().exec(DESKTOP_FOLDER_CMD);
			String s = CKit.readString(process.getInputStream(), Charset.defaultCharset());
			int ix = s.indexOf(REGSTR_TOKEN);
			if(ix >= 0)
			{
				return new File(s.substring(ix + REGSTR_TOKEN.length()).trim());
			}			
		}
		catch(Exception e)
		{
			log.error(e);
		}
		
		return null;
	}
	
	
	@Deprecated
	public File getDefaultSettingsFolder()
	{
		return new File(getUserHome(), SETTINGS_FOLDER);
	}


	protected File getSettingsFolderPrivate()
	{
		// TODO "Documents"?
		return new File(getUserHome(), SETTINGS_FOLDER);
	}
}
