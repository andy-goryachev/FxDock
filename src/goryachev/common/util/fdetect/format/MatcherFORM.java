// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.fdetect.format;
import goryachev.common.util.fdetect.FFMatcher;
import goryachev.common.util.fdetect.FileType;


/**
 * Interchange File Format (IFF), is a generic container file format originally introduced by 
 * the Electronic Arts company in 1985 (in cooperation with Commodore/Amiga).
 * 
 * http://en.wikipedia.org/wiki/Interchange_File_Format
 * http://en.wikipedia.org/wiki/List_of_file_signatures
 */
public class MatcherFORM
	extends FFMatcher
{
	private byte[] prefix = bytes("46 4F 52 4D");
	private byte[] iff = bytes("49 4C 42 4D");
	
	
	public FileType match(String filename, byte[] b)
	{
		if(match(b, prefix))
		{
			if(match(b, iff, 8))
			{
				return FileType.IFF;
			}
		}
		return null;
	}
}
