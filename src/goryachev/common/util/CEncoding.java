// Copyright © 2009-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import goryachev.common.log.Log;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.SortedMap;


public class CEncoding
	implements HasDisplayName, HasProperty
{
	protected static final Log log = Log.get("CEncoding");
	public static final CEncoding UTF8 = new CEncoding(Charset.forName("UTF-8"));
	public static final CEncoding UTF16 = new CEncoding(Charset.forName("UTF-16"));
	
	private Charset charset;
	private static CEncoding[] encodings;


	public CEncoding(Charset cs)
	{
		this.charset = cs;
	}
	
	
	public Charset getCharset()
	{
		return charset;
	}


	@Override
	public String getDisplayName()
	{
		return charset.displayName(Locale.getDefault());
	}
	
	
	@Override
	public String toString()
	{
		return getDisplayName();
	}
	
	
	public String getID()
	{
		return charset.name();
	}
	
	
	@Override
	public String getProperty()
	{
		return getID();
	}


	public static CEncoding[] list()
	{
		if(encodings == null)
		{
			SortedMap<String,Charset> m = Charset.availableCharsets();
			CEncoding[] a = new CEncoding[m.size()];
			int ix = 0;
			for(Charset cs: m.values())
			{
				a[ix++] = new CEncoding(cs);
			}
			encodings = a;
		}
		return encodings;
	}
	
	
	public static CEncoding parse(Object x)
	{
		if(x instanceof CEncoding)
		{
			return (CEncoding)x;
		}
		else if(x instanceof String)
		{
			try
			{
				Charset cs = Charset.forName((String)x);
				return new CEncoding(cs);
			}
			catch(Exception e)
			{
				log.error(e);
			}
		}
		
		return null;
	}
	
	
	@Override
	public boolean equals(Object x)
	{
		if(x == this)
		{
			return true;
		}
		else if(x instanceof CEncoding)
		{
			return charset.equals(((CEncoding)x).charset);
		}
		else
		{
			return false;
		}
	}
	
	
	@Override
	public int hashCode()
	{
		return getClass().hashCode() ^ charset.hashCode();
	}
}
