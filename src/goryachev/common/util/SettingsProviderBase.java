// Copyright © 2016-2019 Andy Goryachev <andy@goryachev.com>
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
	
	// stores String or String[]
	protected CMap<String,Object> data = new CMap<>();
	protected static final Log log = Log.get("SettingsProviderBase");

	
	public SettingsProviderBase()
	{
	}
	
	
	public synchronized List<String> getKeys()
	{
		return data.keys();
	}
	
	
	public synchronized boolean containsKey(String k)
	{
		return data.containsKey(k);
	}
	
	
	public synchronized int size()
	{
		return data.size();
	}
	
	
	protected synchronized Object getValue(String key)
	{
		return data.get(key);
	}
	
	
	protected String getStringPrivate(String key)
	{
		Object x = getValue(key);
		if(x instanceof String)
		{
			return (String)x;
		}
		return null;
	}
	
	
	protected String[] getArrayPrivate(String key)
	{
		Object x = getValue(key);
		if(x instanceof String[])
		{
			return (String[])x;
		}
		else if(x instanceof String)
		{
			return new String[] { (String)x };
		}
		return null;
	}
	
	
	public String getString(String key)
	{
		String v = getStringPrivate(key);
		//log.debug(key, v);
		return v;
	}


	public synchronized void setString(String key, String val)
	{
		if(val == null)
		{
			data.remove(key);
		}
		else
		{
			data.put(key, val);
			//log.debug(key, val);
		}
	}


	public SStream getStream(String key)
	{
		String[] ss = getArrayPrivate(key);
		return new SStream(ss);
	}


	public synchronized void setStream(String key, SStream s)
	{
		data.put(key, s.toArray());
	}
	
	
	public String asString()
	{
		return asString(true);
	}
	
	
	public synchronized String asString(boolean sort)
	{
		List<String> keys = getKeys();
		if(sort)
		{
			CSorter.sort(keys);
		}
		
		SB sb = new SB(keys.size() * 128);
		for(String k: keys)
		{
			Object x = data.get(k);
			sb.a(encode(k));
			sb.a('=');
			
			if(x == null)
			{
				sb.a("\\");
			}
			else if(x instanceof String)
			{
				sb.a(encode((String)x));
			}
			else if(x instanceof String[])
			{
				boolean comma = false;
				for(String s: (String[])x)
				{
					if(comma)
					{
						sb.a(',');
					}
					else
					{
						comma = true;
					}
					
					sb.a(encode(s));
				}
			}
			else
			{
				throw new Error("?" + x);
			}
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
				
				String k = line.substring(0, ix);
				k = decode(k);
				
				String v = line.substring(ix + 1);
				if(v.contains(","))
				{
					String[] ss = CKit.split(v, ',');
					for(int i=0; i<ss.length; i++)
					{
						ss[i] = decode(ss[i]);
					}
					data.put(k, ss);
				}
				else
				{
					v = decode(v);
					data.put(k, v);
				}
			}
		}
		finally
		{
			CKit.close(rd);
		}
	}
	
	
	protected static SStream parseStream(String text)
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
	
	
	protected static String decode(String s)
	{
		if("\\".equals(s))
		{
			return null;
		}
		
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
					String sub = s.substring(i, i + 2);
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
			Log.ex(e);
		}
		return null;
	}
	
	
	// replaces commas, \, and non-printable chars with hex values \HH
	protected static String encode(String s)
	{
		if(s == null)
		{
			return "\\";
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
	
	
	protected static void encode(char c, SB sb)
	{
		sb.a('\\');
		sb.a(Hex.toHexByte(c));
	}
}
