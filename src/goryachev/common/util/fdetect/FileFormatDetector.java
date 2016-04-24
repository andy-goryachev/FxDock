// Copyright (c) 2011-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.fdetect;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.Parsers;
import goryachev.common.util.fdetect.format.MatcherFORM;
import goryachev.common.util.fdetect.format.MatcherFTyp;
import goryachev.common.util.fdetect.format.MatcherMicrosoft;
import goryachev.common.util.fdetect.format.MatcherNetpbm;
import goryachev.common.util.fdetect.format.MatcherPICT;
import goryachev.common.util.fdetect.format.MatcherRAW;
import goryachev.common.util.fdetect.format.MatcherRIFF;
import goryachev.common.util.fdetect.format.MatcherTIFF;
import goryachev.common.util.fdetect.format.MatcherWM;
import goryachev.common.util.fdetect.format.MatcherZip;
import java.io.File;
import java.nio.charset.Charset;
import java.util.StringTokenizer;


// http://en.wikipedia.org/wiki/List_of_file_signatures
// http://www.garykessler.net/library/file_sigs.html
// http://www.filesignatures.net/index.php?page=all
// http://filext.com/file-extension/CRW
public class FileFormatDetector
{
	public static final Charset CHARSET_UTF16 = Charset.forName("UTF-16");
	private static final byte[] BOM_UTF8 = Parsers.parseByteArray("efbbbf");
	private static final byte[] BOM_UTF16LE = Parsers.parseByteArray("fffe");
	private static final byte[] BOM_UTF16BE = Parsers.parseByteArray("feff");
	private CList<FFMatcher> matchers = new CList();
	
	
	public FileFormatDetector()
	{
		// bmp
		add(FileType.BMP, "424D");
		// class
		add(FileType.CLASS, "CAFEBABE");
		// exe
		add(FileType.EXE, "4D5A");
		// flv
		add(FileType.FLV, "464C5601");
		// gif
		add(FileType.GIF, "474946383761");
		add(FileType.GIF, "474946383961");
		// gz, tgz, gzip
		add(FileType.GZ, "1F8B08");
		// icns
		add(FileType.ICNS, "69636e73");
		// ico
		add(FileType.ICO, "00000100");
		// jpg
		add(FileType.JPEG, "FFD8"); // FFD8FF
		// mov
		add(FileType.MOV, "6D6F6F76", 4);
		// mp3
		add(FileType.MP3, "494433");
		add(FileType.MP3, "fffa");
		add(FileType.MP3, "fffb");
		// otf
		add(FileType.OTF, "4f54544f");
		// pdf
		add(FileType.PDF, "25504446");
		// png
		add(FileType.PNG, "89504E470D0A1A0A");
		// ps
		add(FileType.PS, "25215053");
		// psd
		add(FileType.PSD, "38425053");
		// rtf
		add(FileType.RTF, "7B5C72746631");
		// ttf
		add(FileType.TTF, "0001000000");
		// crw canon raw
		add(FileType.RAW, "49491A0000004845415043434452020001");
		
		// ppt, xls, doc
		add(new MatcherMicrosoft());
		// 3gp, mp4, mov
		add(new MatcherFTyp());
		// zip, jar, ect.
		add(new MatcherZip());
		// avi, ...
		add(new MatcherRIFF());
		// avi, wma, wmv
		add(new MatcherWM());
		// iff
		add(new MatcherFORM());
		// pict, pic
		add(new MatcherPICT());
		// tiff, nef
		add(new MatcherTIFF());
		// various raw formats
		add(new MatcherRAW());
		// Netpbm
		add(new MatcherNetpbm());
	}
	
	
	private void add(final FileType t, String hex)
	{
		add(t, hex, 0);
	}
	
	
	private void add(final FileType t, String hex, final int offset)
	{
		final byte[] pattern = Parsers.parseByteArray(hex);
		
		add(new FFMatcher()
		{
			public FileType match(String filename, byte[] b)
			{
				if(match(b, pattern, offset))
				{
					return t;
				}
				return null;
			}
		});
	}


	private void add(FFMatcher m)
	{
		matchers.add(m);
	}


	public FileType detect(File f) throws Exception
	{
		int sz = (int)Math.min(8192, f.length());
		byte[] b = CKit.readBytes(f, sz);
		return detect(f.getName(), b);
	}
	
	
	public FileType detect(String filename, byte[] b)
	{
		if(b != null)
		{
			FileType t;
			
			for(FFMatcher m: matchers)
			{
				t = m.match(filename, b);
				if(t != null)
				{
					return t;
				}
			}
			
			t = detectText(b);
			if(t != null)
			{
				return t;
			}
		}
		
		return FileType.UNKNOWN;
	}
	
	
	public FileType getType(File f)
	{
		try
		{
			return detect(f);
		}
		catch(Exception e)
		{ }
		
		return FileType.UNKNOWN;
	}
	
	
	public static Charset detectUtfBOM(byte[] bytes)
	{
		if(FFMatcher.match(bytes, BOM_UTF8))
		{
			return CKit.CHARSET_UTF8;
		}
		else if(FFMatcher.match(bytes, BOM_UTF16BE))
		{
			// big endian
			return CHARSET_UTF16;
		}
		else if(FFMatcher.match(bytes, BOM_UTF16LE))
		{
			// little endian
			return CHARSET_UTF16;
		}
		else
		{
			return null;
		}
	}

	
	protected String toString(byte[] bytes, Charset enc)
	{
		if(enc != null)
		{
			try
			{
				return new String(bytes, enc);
			}
			catch(Exception e)
			{ }
		}
		
		try
		{
			return new String(bytes, CKit.CHARSET_UTF8);
		}
		catch(Exception e)
		{ }
		
		try
		{
			return new String(bytes, CKit.CHARSET_ASCII);
		}
		catch(Exception e)
		{ }
		
		return "";
	}


	protected FileType detectText(byte[] bytes)
	{
		// do we have a utf file?
		Charset enc = detectUtfBOM(bytes);
		
		String text = null;
		if(enc != null)
		{
			try
			{
				text = new String(bytes, enc);
			}
			catch(Exception e)
			{
				// not a utf encoded text, must be a binary
				return null;
			}
		}
		
		int eof = 0;
		int nonascii = 0;
		
		if(enc == null)
		{
			for(byte b: bytes)
			{
				if(b < 0)
				{
					nonascii++;
				}
				else if(b < ' ')
				{
					switch(b)
					{
					case '\t':
					case '\n':
					case '\r':
						break;
					case 0x1a:
						eof++;
						break;
					default:
						// must be a binary
						return null;
					}
				}
			}
			
			if(eof > 1)
			{
				// unlikely to have two EOF in a text file (unless weird encoding)
				return null;
			}
		}
		
		// let's analyze text
		if(text == null)
		{
			try
			{
				text = new String(bytes, CKit.CHARSET_UTF8);
			}
			catch(Exception e)
			{
				// not a utf-8, let's try ascii
				// TODO could be a national encoding
				try
				{
					text = new String(bytes, CKit.CHARSET_ASCII);
				}
				catch(Exception e2)
				{
					// may not arrive here because ascii always creates a string
					// TODO also nonascii
					return null;
				}
			}
		}
		
		text = text.trim().toLowerCase();
		
		boolean xml = false;
		boolean bracket = false;
		boolean doctype = false;
		StringTokenizer tok = new StringTokenizer(text, "<> :\t\r\n", true);
		while(tok.hasMoreTokens())
		{
			String s = tok.nextToken();
			
			if("?xml".equals(s))
			{
				if(bracket)
				{
					xml = true;
				}
			}
			else if("svg".equals(s))
			{
				if(bracket)
				{
					return FileType.SVG;
				}
			}
			else if("html".equals(s))
			{
				if(doctype || bracket)
				{
					if(xml)
					{
						// xhtml
						return FileType.XML;
					}
					else
					{
						return FileType.HTML;
					}
				}
			}
			else if("xsl".equals(s))
			{
				if(bracket)
				{
					// it's an XSL actually
					return FileType.XML;
				}
			}
			
			doctype = bracket && "!doctype".equals(s);
			bracket = "<".equals(s);
		}
		
		if(xml)
		{
			return FileType.XML;
		}
		else		
		{
			return FileType.TEXT;
		}
	}
}
