// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.fdetect.format;
import goryachev.common.util.fdetect.FFMatcher;
import goryachev.common.util.fdetect.FileType;


/**
 * Matches Netpbm format.
 * http://en.wikipedia.org/wiki/Netpbm_format
 */ 
public class MatcherNetpbm
	extends FFMatcher
{
	public FileType match(String filename, byte[] b)
	{
		if(match(b, 0, 'P'))
		{
			int c = getByte(b, 1);
			switch(c)
			{
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
				break;
			default:
				return null;
			}
			
			c = getByte(b, 2);
			switch(c)
			{
			case ' ':
			case '\r':
			case '\n':
			case '\t':
			case 0x11:
			case '\f':
				return FileType.NETPBM;
			}
		}
		return null;
	}
}
