// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.fdetect.format;
import goryachev.common.util.fdetect.FFMatcher;
import goryachev.common.util.fdetect.FileType;


/**
 * TIFF container detector.
 * 
 * http://www.filesignatures.net/index.php?page=search&search=TIFF&mode=EXT
 * http://filext.com/file-extension/NEF
 * http://filext.com/file-extension/CR2
 */
public class MatcherTIFF
	extends FFMatcher
{
	private byte[] cr2 = bytes("49492A00100000004352");
	private byte[] nef = bytes("4D4D002A0000000800");
	private byte[] tiff1 = bytes("492049");
	private byte[] tiff2 = bytes("49492A00");
	private byte[] tiff3 = bytes("4D4D002A");
	private byte[] tiff4 = bytes("4D4D002B");
	
	
	public FileType match(String filename, byte[] b)
	{
		if(match(b, cr2))
		{
			return FileType.RAW;
		}
		else if(match(b, nef))
		{
			if(ext(filename, ".nef"))
			{
				return FileType.RAW;
			}
			else if(ext(filename, ".mos"))
			{
				// Leaf raw .MOS http://filext.com/file-extension/MOS
				return FileType.RAW;
			}
			else if(ext(filename, ".erf"))
			{
				// epson raw image
				return FileType.RAW;
			}
			else if(ext(filename, ".kdc"))
			{
				// kodak
				return FileType.RAW;
			}
			else if(ext(filename, ".dcr"))
			{
				// kodak 
				return FileType.RAW;
			}
			else if(ext(filename, ".mef"))
			{
				// mamiya 
				return FileType.RAW;
			}
			else if(ext(filename, ".pef"))
			{
				// pentax 
				return FileType.RAW;
			}
			else if(ext(filename, ".dng"))
			{
				// pentax 
				return FileType.RAW;
			}
			else
			{
				return FileType.TIFF;
			}
		}
		else if(match(b, tiff1))
		{
			return FileType.TIFF;
		}
		else if(match(b, tiff2))
		{
			if(ext(filename, ".nef"))
			{
				// nikon
				return FileType.RAW;
			}
			if(ext(filename, ".nrw"))
			{
				// nikon
				return FileType.RAW;
			}
			if(ext(filename, ".3fr"))
			{
				// hasselblad raw image
				return FileType.RAW;
			}
			else if(ext(filename, ".kdc"))
			{
				// kodak
				return FileType.RAW;
			}
			else if(ext(filename, ".dng"))
			{
				// leica
				return FileType.RAW;
			}
			else if(ext(filename, ".arw"))
			{
				// sony .ARW
				return FileType.RAW;
			}
			else if(ext(filename, ".sr2"))
			{
				// sony .SR2
				return FileType.RAW;
			}
			else
			{
				return FileType.TIFF;
			}
		}
		else if(match(b, tiff3))
		{
			if(ext(filename, ".dcr"))
			{
				// kodak 
				return FileType.RAW;
			}
			if(ext(filename, ".dng"))
			{
				// ricoh 
				return FileType.RAW;
			}
			return FileType.TIFF;
		}
		else if(match(b, tiff4))
		{
			return FileType.TIFF;
		}
		return null;
	}
}
