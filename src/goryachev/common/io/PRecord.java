// Copyright © 2012-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.io;
import goryachev.common.log.Log;
import goryachev.common.util.CList;
import goryachev.common.util.CMap;
import goryachev.common.util.CSorter;
import goryachev.common.util.Dump;
import goryachev.common.util.Parsers;
import goryachev.common.util.SB;
import java.io.Serializable;
import java.math.BigInteger;


public class PRecord
	implements Serializable
{
	/** override this method to pack transient data into attribute(s) */
	protected void commit() throws Exception { }
	
	//
	
	private CMap<String,Object> data;
	private CMap<String,Object> original;


	public PRecord()
	{
	}
	
	
	protected PRecord(CMap data)
	{
		this.data = new CMap<>(data);
		this.original = new CMap<>(data);
	}
	
	
	public PRecord copy()
	{
		return new PRecord(data);
	}
	
	
	protected CMap getAttributeMap()
	{
		return data;
	}
	
	
	protected CMap data()
	{
		if(data == null)
		{
			data = new CMap<>();
			original = new CMap<>();
		}
		return data;
	}
	
	
	protected void setAttributeMap(CMap<String,Object> m) throws Exception
	{
		this.data = m;
		this.original = (CMap<String,Object>)PTools.deepClone(m);
	}
	
	
	/** reset changes */
	public void reset()
	{
		try
		{
			setAttributeMap(data);
		}
		catch(Exception e)
		{
			Log.err(e);
		}
	}
	
	
	public void createNew()
	{
		data = new CMap<>(data);
		original = new CMap<>();
	}


	public Object getAttribute(String key)
	{
		return data().get(key);
	}
	
	
	public static Object getAttribute(PRecord r, String key)
	{
		if(r != null)
		{
			return r.getAttribute(key);
		}
		return null;
	}


	public boolean hasAttribute(String key)
	{
		return (data().get(key) != null);
	}


	public char[] getCharArray(String key)
	{
		return Parsers.parseCharArray(getAttribute(key));
	}
	
	
	public static char[] getCharArray(PRecord r, String key)
	{
		if(r != null)
		{
			return r.getCharArray(key);
		}
		return null;
	}


	public String getString(String key)
	{
		return Parsers.parseString(getAttribute(key));
	}
	
	
	public static String getString(PRecord r, String key)
	{
		return r == null ? null : r.getString(key);
	}
	
	
	public Float getFloat(String key)
	{
		return Parsers.parseFloat(getAttribute(key));
	}
	
	
	public float getFloat(String key, float defaultValue)
	{
		Float v = getFloat(key);
		return v == null ? defaultValue : v;
	}
	
	
	public static float getFloat(PRecord r, String key, float defaultValue)
	{
		if(r != null)
		{
			return r.getFloat(key, defaultValue);
		}
		return defaultValue;
	}
	
	
	public static Integer getInteger(PRecord r, String key)
	{
		if(r != null)
		{
			return r.getInteger(key);
		}
		return null;
	}
	
	
	public Integer getInteger(String key)
	{
		return Parsers.parseInteger(getAttribute(key));
	}
	
	
	public int getInt(String key, int defaultValue)
	{
		Integer v = getInteger(key);
		return v == null ? defaultValue : v;
	}
	
	
	public static int getInt(PRecord r, String key, int defaultValue)
	{
		if(r != null)
		{
			return r.getInt(key, defaultValue);
		}
		return defaultValue;
	}
	
	
	public Long getLong(String key)
	{
		return Parsers.parseLong(getAttribute(key));
	}
	
	
	public long getLong(String key, long defaultValue)
	{
		Long v = getLong(key);
		return v == null ? defaultValue : v;
	}
	
	
	public static long getLong(PRecord r, String key, long defaultValue)
	{
		if(r != null)
		{
			return r.getLong(key, defaultValue);
		}
		return defaultValue;
	}
	
	
	public static Long getLong(PRecord r, String key)
	{
		return r == null ? null : r.getLong(key);
	}
	
	
	public CList getList(String key)
	{
		Object v = getAttribute(key);
		if(v instanceof CList)
		{
			return (CList)v;
		}
		return null;
	}
	
	
	public CMap getMap(String key)
	{
		Object v = getAttribute(key);
		if(v instanceof CMap)
		{
			return (CMap)v;
		}
		return null;
	}
	
	
	public boolean getBoolean(String key)
	{
		return Parsers.parseBooleanStrict(getAttribute(key));
	}
	
	
	public BigInteger getBigInteger(String key)
	{
		return Parsers.parseBigInteger(getAttribute(key));
	}
	
	
	public byte[] getByteArray(String key)
	{
		return Parsers.parseByteArray(getAttribute(key));
	}


	public Object setAttribute(String key, Object x)
	{
		if(x == null)
		{
			return data().remove(key);
		}
		else
		{
			return data().put(key, x);
		}
	}
	
	
	public void setString(String key, Object x)
	{
		String s = Parsers.parseString(x);
		setAttribute(key, s);
	}


	public CList<String> getAttributeNames()
	{
		return new CList(data().keySet());
	}

	
	public boolean isModified()
	{
		return !PTools.deepEquals(data(), original);
	}
	
	
	public boolean equals(Object x)
	{
		if(x == this)
		{
			return true;
		}
		else if(x instanceof PRecord)
		{
			return PTools.deepEquals(data(), ((PRecord)x).data());
		}
		else
		{
			return false;
		}
	}
	
	
	public int hashCode()
	{
		return PRecord.class.hashCode() ^ data().hashCode();
	}
	
	
	public String dump()
	{
		SB sb = new SB();
		CList<String> names = getAttributeNames();
		CSorter.sort(names);
		
		for(String name: names)
		{
			sb.append(name).append(": ");
			
			Object v = getAttribute(name);
			Dump.describe(v, sb, 0);
		}
		
		return sb.toString();
	}
}
