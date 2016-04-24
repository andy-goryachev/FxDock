// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.fdetect.format;
import goryachev.common.util.fdetect.FFMatcher;
import goryachev.common.util.fdetect.FileType;


public class MatcherZip
	extends FFMatcher
{
	private byte[] prefix = bytes("504B");
	
	
	public FileType match(String filename, byte[] b)
	{
		if(match(b, prefix))
		{
			String name = filename.toLowerCase();
			if(name.endsWith(".jar"))
			{
				return FileType.JAR;
			}
			else if(name.endsWith(".docx"))
			{
				return FileType.DOCX;
			}
			else if(name.endsWith(".xlsx"))
			{
				return FileType.XLSX;
			}
			else if(name.endsWith(".pptx"))
			{
				return FileType.PPTX;
			}
			else if(name.endsWith(".odt"))
			{
				return FileType.ODT;
			}
			else if(name.endsWith(".odp"))
			{
				return FileType.ODP;
			}
			else if(name.endsWith(".ods"))
			{
				return FileType.ODS;
			}
			else if(name.endsWith(".ott"))
			{
				return FileType.OTT;
			}
			else if(name.endsWith(".sxc"))
			{
				return FileType.SXC;
			}
			else if(name.endsWith(".sxd"))
			{
				return FileType.SXD;
			}
			else if(name.endsWith(".sxi"))
			{
				return FileType.SXI;
			}
			else if(name.endsWith(".sxw"))
			{
				return FileType.SXW;
			}
			else
			{
				return FileType.ZIP;
			}
		}
		return null;
	}
}
