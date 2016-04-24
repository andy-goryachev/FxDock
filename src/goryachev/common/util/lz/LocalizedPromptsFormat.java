// Copyright (c) 2005-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.lz;
import goryachev.common.util.CKit;
import goryachev.common.util.SB;
import goryachev.common.util.TextTools;
import java.nio.charset.Charset;


/*
 * Class encapsulates localized prompts, loaded from .prompts file.
 * 
 * This human-readable text file format is similar to java .properties without cumbersome unicode escapes 
 * (since all modern editors can edit UTF files), and without complex rules for separators.
 * 
 * Encoding:
 *    UTF-8 with or without BOM (default)
 *    UTF-16 with BOM
 *    
 * Extension:
 *    .prompts
 *    
 * File Name Conventions:
 *    <NAME>.prompts         master file
 *    <NAME>_<LANG>.prompts  translation
 *    
 * Format
 *    # comment
 *    id<TAB>translation<NEWLINE>
 *    
 * Characters Escape Codes
 *    Unfortunately, a complete escape-less format will be binary.  The following special characters
 *    in the "id" and "translation" fields are escaped with the following sequences:
 *    \\ - backslash
 *    \t
 *    \n
 *    \r
 *    \f 
 */
public class LocalizedPromptsFormat
{
	public static final String EXTENSION = ".prompts";
	public static final String COMMENT_PREFIX = "#";
	public static final char SEPARATOR_CHAR = '\t';
	
	
	public static String detectEncodingAndExtractText(byte[] b)
	{
		int b0 = b[0] & 0xff;
		int b1 = b[1] & 0xff;
		
		if((b0 == 0xfe) && (b1 == 0xff))
		{
			// UTF-16BE
			return new String(b, 2, b.length - 2, Charset.forName("UTF-16BE"));
		}
		else if((b0 == 0xff) && (b1 == 0xfe))
		{
			// UTF-16LE
			return new String(b, 2, b.length - 2, Charset.forName("UTF-16LE"));
		}
		else if(b.length >= 3)
		{
			int b2 = b[2] & 0xff;
			if((b0 == 0xef) && (b1 == 0xbb) && (b2 == 0xbf))
			{
				// UTF-8
				return new String(b, 3, b.length - 3, CKit.CHARSET_UTF8);
			}
		}
		
		// treat everything else as UTF-8
		return new String(b, CKit.CHARSET_UTF8);
	}
	
	
	/** decode escaped characters */
	public static String decode(String s)
	{
		if(TextTools.contains(s, '\\'))
		{
			SB sb = new SB(s);
			sb.replace("\\n", "\n");
			sb.replace("\\r", "\r");
			sb.replace("\\t", "\t");
			sb.replace("\\f", "\f");
			sb.replace("\\\\", "\\");
			return sb.toString();
		}
		else
		{
			return s;
		}
	}
	
	
	/** properly encode special characters */
	public static String encode(String s)
	{
		SB sb = new SB(s);
		
		sb.replace("\\", "\\\\");
		sb.replace("\n", "\\n");
		sb.replace("\r", "\\r");
		sb.replace("\t", "\\t");
		sb.replace("\f", "\\f");
		
		return sb.toString();
	}
}
