// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import goryachev.common.io.CReader;
import java.util.List;


/**
 * In-memory map-based Settings Provider.
 */
public abstract class SettingsProviderBase
    implements GlobalSettings.Provider
{
	public abstract void save();

	//
	
	protected CMap<String,String> data = new CMap();
	protected static final CLog log = Log.get("SettingsProviderBase");

	
	public SettingsProviderBase()
	{
	}
	
	
	public List<String> getKeys()
	{
		return data.keys();
	}
	
	
	public synchronized String getString(String key)
	{
		String v = data.get(key);
		//log.debug(key, v);
		return v;
	}


	public synchronized void setString(String key, String val)
	{
		data.put(key, val);
		//log.debug(key, val);
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
	
	
	public synchronized String asString()
	{
		List<String> keys = getKeys();
		CSorter.sort(keys);
		
		SB sb = new SB(keys.size() * 128);
		for(String k: keys)
		{
			String v = data.get(k);
			sb.a(encode(k));
			sb.a('=');
			sb.a(v);
			sb.nl();
		}
		return sb.toString();
	}
	
	
	public synchronized void loadFromString(String s) throws Exception
	{
		CReader rd = new CReader(s);
		try
		{
			String line;
			while((line = rd.readLine()) != null)
			{
				int ix = line.indexOf('=');
				if(ix < 0)
				{
					continue;
				}
				
				String k = s.substring(0, ix);
				k = decode(k);
				
				String v = s.substring(ix + 1);
				// TODO
				D.print(k, v);
			}
		}
		finally
		{
			CKit.close(rd);
		}
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
}
