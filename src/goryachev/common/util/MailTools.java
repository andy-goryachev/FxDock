// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.awt.Desktop;
import java.net.URI;
import java.text.Normalizer;


public class MailTools
{
	public static void mail(String to, String subject, String body) throws Exception
	{
		SB sb = new SB();
		
		sb.a("mailto:").a(to);
		sb.a("?subject=").a(encode(subject)); 
		sb.a("&body=").a(encode(body));
			
		Desktop.getDesktop().mail(new URI(sb.toString()));
	}


	public static String encode(String s) throws Exception
	{
		if(s == null)
		{
			return "";
		}

		int n = s.length();
		if(n == 0)
		{
			return s;
		}

		String ns = Normalizer.normalize(s, Normalizer.Form.NFC);
		byte[] bb = ns.getBytes("UTF-8");

		SB sb = new SB();
		for(int b: bb)
		{
			b &= 0xff;
			if(b >= 0x80)
			{
				appendEscape(sb, (byte) b);
			}
			else
			{
				switch(b)
				{
				case ' ':
				case '\n':
				case '=':
				case '+':
				case '?':
				case '&':
				case '\\':
				case '\r':
				case '>':
				case '<':
					appendEscape(sb, (byte) b);
					break;
				default:
					sb.append((char) b);
				}
			}
		}
		return sb.toString();
	}


	private static void appendEscape(SB sb, byte b)
	{
		sb.append('%');
		Hex.hexByte(sb, b);
	}
	
	
	/** 
	 * Simple check verifies that the passed string is in format A@B.C
	 */
	public static boolean isValidEmail(String s)
	{
		if(s != null)
		{
			int at = -1;
			int dot = -1;
			
			int sz = s.length();
			for(int i=0; i<sz; i++)
			{
				char c = s.charAt(i);
				
				switch(c)
				{
				case '.':
					dot = i;
					break;
					
				case '@':
					if(at >= 0)
					{
						// two @
						return false;
					}
					else
					{
						at = i;
					}
					break;
				}
			}
			
			if(at <= 0)
			{
				// can't start with @
				return false;
			}
			
			if(dot >= (s.length() - 1))
			{
				// can't end with .
				return false;
			}
			
			if(dot <= (at + 1))
			{
				// needs domain name
				return false;
			}

			return true;
		}
		return false;
	}
}
