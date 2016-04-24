// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.fdetect.format;
import goryachev.common.util.fdetect.FFMatcher;
import goryachev.common.util.fdetect.FileType;


/**
 * CFB format always begins with this byte pattern, “D0 CF 11 E0 A1 B1 1A E1”.
 * 
 * http://msdn.microsoft.com/en-us/library/cc313105.aspx
 * http://en.wikipedia.org/wiki/List_of_file_signatures
 * http://social.msdn.microsoft.com/Forums/en-US/343d09e3-5fdf-4b4a-9fa6-8ccb37a35930/developing-a-tool-to-recognise-ms-office-file-types-doc-xls-mdb-ppt-?forum=os_binaryfile
 */
public class MatcherMicrosoft
	extends FFMatcher
{
	private byte[] cfb = bytes("D0CF11E0A1B11AE1");
	
	
	public FileType match(String filename, byte[] b)
	{
		if(match(b, cfb))
		{
			if(ext(filename, ".doc"))
			{
				return FileType.DOC;
			}
			else if(ext(filename, ".xls"))
			{
				return FileType.XLS;
			}
			else if(ext(filename, ".ppt"))
			{
				return FileType.PPT;
			}	
			else if(ext(filename, ".vsd"))
			{
				return FileType.VSD;
			}
		}
		return null;
	}
}
