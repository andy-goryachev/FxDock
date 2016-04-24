// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.fdetect.format;
import goryachev.common.util.fdetect.FFMatcher;
import goryachev.common.util.fdetect.FileType;


/**
 * Matches exotic RAW formats.
 */ 
public class MatcherRAW
	extends FFMatcher
{
	private byte[] raf = bytes("46554A4946494C4D4343442D52415720");
	private byte[] mrw = bytes("004D524D");
	private byte[] orf = bytes("494952");
	private byte[] mmor = bytes("4D4D4F52");
	private byte[] rw2 = bytes("49495500");
	private byte[] sigma = bytes("464F5662");
	private byte[] sony = bytes("050000004157312E");
	
	
	public FileType match(String filename, byte[] b)
	{
		if(match(b, sony))
		{
			// sony .ARW http://filext.com/file-extension/ARW
			return FileType.RAW;
		}
		else if(match(b, raf))
		{
			// fuji .RAF http://filext.com/file-extension/RAF
			return FileType.RAW;
		}
		else if(match(b, mrw))
		{
			// minolta .MRW http://filext.com/file-extension/MRW
			return FileType.RAW;
		}
		else if(match(b, orf))
		{
			// olympus .ORF http://filext.com/file-extension/ORF
			return FileType.RAW;
		}
		else if(match(b, mmor))
		{
			// olympus
			return FileType.RAW;
		}
		else if(match(b, rw2))
		{
			// panasonic .RW2 http://fileformats.archiveteam.org/wiki/Panasonic_RAW/RW2
			return FileType.RAW;
		}
		else if(match(b, sigma))
		{
			// sigma .X3F http://filext.com/file-extension/X3F
			return FileType.RAW;
		}
		
		// by extension
		
		if(ext(filename, ".crw"))
		{
			// canon raw .CRW http://filext.com/file-extension/CRW
			return FileType.RAW;
		}
		else if(ext(filename, ".raw"))
		{
			// kodak raw
			return FileType.RAW;
		}
		return null;
	}
}
