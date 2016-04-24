// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.fdetect.format;
import goryachev.common.util.fdetect.FFMatcher;
import goryachev.common.util.fdetect.FileType;


public class MatcherWM
	extends FFMatcher
{
	private byte[] prefix = bytes("3026B2758E66CF11A6D900AA0062CE6C");
	
	
	public FileType match(String filename, byte[] b)
	{
		if(match(b, prefix))
		{
			String name = filename.toLowerCase();
			if(name.endsWith(".asf"))
			{
				return FileType.ASF;
			}
			else if(name.endsWith(".wma"))
			{
				return FileType.WMA;
			}
			else if(name.endsWith(".wmv"))
			{
				return FileType.WMV;
			}
		}
		return null;
	}
}
