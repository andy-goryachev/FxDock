// Copyright © 2008-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.html;
import goryachev.common.log.Log;
import goryachev.common.util.Base64;
import goryachev.common.util.CComparator;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.CSet;
import goryachev.common.util.Hex;
import goryachev.common.util.SB;
import java.awt.Color;
import java.net.URI;


public class HtmlTools
{
	private static Html4SymbolEntities html4SymbolEntities;
	private static CSet<String> htmlTags;
	
	
	public static String safe(String s)
	{
		if(s != null)
		{
			if(s.indexOf('&') >= 0)
			{
				s = s.replace("&","&#38;");
			}
			
			if(s.indexOf('<') >= 0)
			{
				s = s.replace("<","&#60;");
			}
		}
		
		return s;
	}
	
	
	// returns string equivalent of its HTML4 symbol
	// #0022 -> "
	// #x27 -> ' 
	// amp  -> &
	// returns null if code is unknown
	public static Character characterEntity(String token)
	{
		if(token.startsWith("#x"))
		{
			try
			{
				int unicode = Integer.parseInt(token.substring(2, token.length()), 16);
				return (char)unicode;
			}
			catch(Exception e)
			{
				return null;
			}
		}
		else if(token.startsWith("#"))
		{
			try
			{
				int unicode = Integer.parseInt(token.substring(1, token.length()));
				return (char)unicode;
			}
			catch(Exception e)
			{
				return null;
			}
		}
		
		return html4SymbolEntities().lookupChar(token);
	}
	
	
	private static synchronized Html4SymbolEntities html4SymbolEntities()
	{
		if(html4SymbolEntities == null)
		{
			html4SymbolEntities = new Html4SymbolEntities();
		}
		return html4SymbolEntities;
	}


	/** converts html escape sequences (&XX;) to their unicode equivalents */
	public static String decodeHtmlCharacterEntities(String text)
	{
		if(text == null)
		{
			return null;
		}
		
		// process html character entities 
		// &nbsp;
		SB sb = new SB(text);
		return decodeHtmlCharacterEntities(sb);
	}
	
	
	/** converts html escape sequences (&XX;) to their unicode equivalents */
	public static String decodeHtmlCharacterEntities(SB sb)
	{
		int ix = 0;
		while((ix = sb.indexOf("&", ix)) >= 0)
		{
			int xe = sb.indexOf(";", ix + 1);
			if(xe < 0)
			{
				break;
			}
			else
			{
				String entity = sb.substring(ix+1, xe);
				Character c = characterEntity(entity);
				if(c == null)
				{
					ix = xe + 1;
				}
				else
				{
					sb.replace(ix, xe+1, String.valueOf(c));
					ix += 1;
				}
			}
		}
		
		return sb.toString();
	}
	
	
	public static String toHtml(String s)
	{
		s = safe(s);
		s = s.replace("\u00a0", "&nbsp;");
		
		// convert all other characters not supported by the encoding 
		
		return s;
	}
	
	
	public static String formatPercent(int x)
	{
		return x + "%";
	}
	
	
	public static String formatPixels(int x)
	{
		return x + "px";
	}
	
	
	public static String toColorString(Color c)
	{
		return "#" + Hex.toHexString(c.getRGB(), 6);
	}
	
	
	public static String safe(Object x)
	{
		if(x == null)
		{
			return null;
		}
		
		String text = x.toString();
		if(text.contains("&"))
		{
			text = text.replace("&", "&amp;");
		}
		
		if(text.contains("<"))
		{
			text = text.replace("<", "&lt;");
		}
		
		if(text.contains("\n"))
		{
			text = text.replace("\n", "<br>");
		}
		
		return text;
	}
	
	
	public static String removeQuotes(String s)
	{
		if(s != null)
		{
			int last = s.length() - 1;
			if(last >= 0)
			{
				char c = s.charAt(0);
				switch(c)
				{
				case '\'':
				case '"':
					char ce = s.charAt(last);
					if(c == ce)
					{
						return s.substring(1, last);
					}
					break;
				}
			}
		}
		return s;
	}
	
	
	public static String quoteConditionally(String s)
	{
		if(s.startsWith("\"") && s.endsWith("\""))
		{
			return s;
		}
		else if(s.startsWith("\'") && s.endsWith("\'"))
		{
			return s;
		}
		
		boolean needToQuote = s.contains(" ");
		needToQuote |= s.contains("+");

		if(s.contains("\""))
		{
			needToQuote = true;
			s = s.replace("\"", "&#34;");
		}
		
		if(s.contains("'"))
		{
			s = s.replace("'", "&#39;");
		}
		
		if(needToQuote)
		{
			return "\"" + s + "\"";
		}
		else
		{
			return s;
		}
	}


	public static void sort(CList<String> ss)
	{
		new CComparator<String>()
		{
			public int compare(String a, String b)
			{
				return compareAsStrings(a, b);
			}
		}.sort(ss);
	}
	
	
	public static String generate(Object ... ss)
	{
		SB sb = new SB();
		for(Object x: ss)
		{
			sb.a(x);
		}
		return sb.toString();
	}
	
	
	public static String color(Color c)
	{
		SB sb = new SB(7);
		color(sb, c);
		return sb.toString(); 
	}
	
	
	public static void color(SB sb, Color c)
	{
		sb.a('#');
		sb.a(Hex.toHexByte(c.getRed()));
		sb.a(Hex.toHexByte(c.getGreen()));
		sb.a(Hex.toHexByte(c.getBlue()));
	}


	public static boolean isTag(String s)
	{
		if(s == null)
		{
			return false;
		}
		
		s = CKit.toLowerCase(s);
		return htmlTags().contains(s);
	}
	
	
	private static synchronized CSet<String> htmlTags()
	{
		if(htmlTags == null)
		{
			htmlTags = new CSet<>(CKit.collectPublicStaticFields(HTML4.class, String.class));
		}
		return htmlTags;
	}
	
	
	/** 
	 * Properly escapes an url to be inserted into html. 
	 */
	public static String toUrlString(String u)
	{
		if(u != null)
		{
			try
			{
				u = u.trim();
				
				String prefix = "://";
				int ix = u.indexOf(prefix);
				String protocol = u.substring(0, ix);
				
				String host;
				String path;
				String query;
				int start = ix + prefix.length();
				ix = u.indexOf("/", start);
				if(ix < 0)
				{
					ix = u.indexOf('?', start);
					if(ix < 0)
					{
						host = u.substring(start);
						path = null;
						query = null;
					}
					else
					{
						host = u.substring(start, ix);
						path = null;
						query = u.substring(ix + 1);
					}
				}
				else
				{
					host = u.substring(start, ix);
					start = ix;
					
					ix = u.indexOf('?');
					if(ix < 0)
					{
						path = u.substring(start);
						query = null;
					}
					else
					{
						path = u.substring(start, ix);
						query = u.substring(ix + 1);
					}
				}
				
				URI uri = new URI(protocol, host, path, query, null);
				return uri.toASCIIString();
			}
			catch(Exception e)
			{
				Log.err(e);
			}
		}
		return u;
	}
	

	// src= data:image/png;base64,iVBORw0K...
	public static byte[] parseBase64Data(String s) throws Exception
	{
		int ix = s.indexOf(',');
		if(ix >= 0)
		{
			s = s.substring(ix+1);
			return Base64.decode(s);
		}
		return null;
	}
}
