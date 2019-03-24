// Copyright © 2006-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;


public class NetTools
{
	/** read string content from an http link */
	public static String readString(String url) throws Exception
	{
		byte[] content;

		HttpURLConnection c = (HttpURLConnection)new URL(url).openConnection();
		//c.setRequestProperty("User-Agent", getUserAgent());
		//c.setConnectTimeout(connectTimeout);
		//c.setReadTimeout(readTimeout);
		c.setRequestProperty("Connection", "close");
		c.setInstanceFollowRedirects(true);
		
		int code = c.getResponseCode();
		if(code != 200)
		{
			throw new Exception("HTTP " + code + " reading " + url);
		}

		InputStream in = getInputStream(c);
		try
		{
			ByteArrayOutputStream ba = new ByteArrayOutputStream();
			CKit.copy(in, ba);
			content = ba.toByteArray();
		}
		finally
		{
			CKit.close(in);
		}
		
		// identify encoding
		String s = new String(content, CKit.CHARSET_UTF8);
		Charset cs = extractCharset(s);
		if(!CKit.CHARSET_UTF8.equals(cs))
		{
			s = new String(content, cs);
		}
		
		return s;
	}
	
	
	private static InputStream getInputStream(HttpURLConnection c) throws Exception
	{
		//Object x = c.getContent();
		
		InputStream in = c.getInputStream();
		String s = c.getRequestProperty("Content-Encoding");
		if(s != null)
		{
			if(s.contains("zip"))
			{
				return new GZIPInputStream(in);
			}
		}
		return in;
	}
	
	
	private static Charset extractCharset(String s)
	{
		try
		{
			int ix = s.indexOf('>');
			if(ix > 0)
			{
				s = s.substring(0, ix);
				ix = TextTools.indexOfIgnoreCase(s, "encoding");
				if(ix >= 0)
				{
					ix += "encoding".length();
					
					// trim leading whitespace, equals, and quotes
					for(; ix<s.length(); ix++)
					{
						char c = s.charAt(ix);
						if(CKit.isBlank(c))
						{
							continue;
						}
						else
						{
							switch(c)
							{
							case '=':
							case '\'':
							case '"':
								continue;
							}
						}
	
						// start of encoding
						break;
					}
					
					int end;
					for(end=ix; end<s.length(); end++)
					{
						char c = s.charAt(end);
						switch(c)
						{
						case '"':
						case '\'':
						case '?':
						case '>':
							break;
						default:
							continue;
						}
						
						// found
						break;
					}
					
					String enc = s.substring(ix, end);
					return Charset.forName(enc);
				}
			}
		}
		catch(Exception e)
		{
			Log.ex(e);
		}
		
		return CKit.CHARSET_UTF8;
	}
	
	
	public static URI parseURI(String uri) throws Exception
	{
		return new URI(parseUrlString(uri));
	}
	
	
	public static URL parseURL(String url) throws Exception
	{
		return new URL(parseUrlString(url));
	}

	
	/** replaces non-ASCII symbols with their UTF-8 byte representation and spaces with %20 */
	public static String parseUrlString(String url)
	{
		byte[] bytes = url.getBytes(CKit.CHARSET_UTF8);
		SB sb = new SB(bytes.length * 2);
		
		for(byte b: bytes)
		{
			if(b < 0)
			{
				sb.a('%');
				sb.a(Hex.toHexByte(b & 0xff));
			}
			else if(b == ' ')
			{
				sb.a("%20");
			}
			else
			{
				sb.a((char)b);
			}
		}
		
		return sb.toString();
	}
}
