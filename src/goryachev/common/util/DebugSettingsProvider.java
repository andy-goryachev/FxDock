// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.util.List;


/**
 * DebugSettingsProvider.
 */
public class DebugSettingsProvider
    implements GlobalSettings.Provider
{
	protected static final CLog log = Log.get("DebugSettingsProvider");
	private CMap<String,String> data = new CMap();
	
	
	public DebugSettingsProvider()
	{
	}
	

	public void save()
	{
		D.print("\n" + asString());
	}

	
	public synchronized String getString(String key)
	{
		String v = data.get(key);
		log.print(key, v);
		D.print(key, v);
		return v;
	}


	public synchronized void setString(String key, String val)
	{
		data.put(key, val);
		log.print(key, val);
		D.print(key, val);
	}


	public SStream getStream(String key)
	{
		String s = getString(key);
		return parseStream(s);
	}


	public void setStream(String key, SStream s)
	{
		setString(key, asString(s));
	}
	
	
	private static SStream parseStream(String text)
	{
		SStream rv = new SStream();
		if(text != null)
		{
			String[] ss = CKit.split(text, ",");
			for(String s: ss)
			{
				rv.add(decode(s));
			}
		}
		return rv;
	}
	
	
	private static String asString(SStream ss)
	{
		SB sb = new SB();
		
		boolean comma = false;
		for(String s: ss)
		{
			if(comma)
			{
				sb.a(",");
			}
			else
			{
				comma = true;
			}
			
			sb.a(encode(s));
		}
		return sb.toString();
	}
	
	
	private static String decode(String s)
	{
		try
		{
			SB sb = new SB();
			int sz = s.length();
			for(int i=0; i<sz; i++)
			{
				char c = s.charAt(i);
				if(c == '\\')
				{
					i++;
					String sub = sb.substring(i, i + 2);
					sb.append((char)Hex.parseByte(sub));
					i++;
				}
				else
				{
					sb.append(c);
				}
			}
			return sb.toString();
		}
		catch(Exception e)
		{
			Log.err(e);
		}
		return "";
	}
	
	
	// replaces commas, \, and non-printable chars with hex values \HH
	private static String encode(String s)
	{
		if(s == null)
		{
			return "";
		}
		
		int sz = s.length();
		SB sb = new SB(sz * 3);
		for(int i=0; i<sz; i++)
		{
			char c = s.charAt(i);
			if(c < 0x20)
			{
				encode(c, sb);
			}
			else
			{
				switch(c)
				{
				case ',':
				case '\\':
				case '=':
					encode(c, sb);
					break;
				default:
					sb.append(c);
				}
			}
		}
		return sb.toString();
	}
	
	
	private static void encode(char c, SB sb)
	{
		sb.a('\\');
		sb.a(Hex.toHexByte(c));
	}
	
	
	public List<String> getKeys()
	{
		return data.keys();
	}
	
	
	public synchronized String asString()
	{
		List<String> keys = getKeys();
		CSorter.sort(keys);
		
		SB sb = new SB(keys.size() * 128);
		for(String k: keys)
		{
			String v = data.get(k);
			sb.a(k);
			sb.a('=');
			sb.a(v);
			sb.nl();
		}
		return sb.toString();
	}
}
