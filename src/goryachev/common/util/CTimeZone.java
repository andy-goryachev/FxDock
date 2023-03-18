// Copyright © 2010-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.util.Locale;
import java.util.TimeZone;


// TODO why do I have this class?
public class CTimeZone
	implements HasProperty, HasDisplayName
{
	public final TimeZone tz;


	public CTimeZone(TimeZone tz)
	{
		this.tz = tz;
	}
	
	
	public TimeZone getTimeZone()
	{
		return tz;
	}


	public String getDisplayName()
	{
		return tz.getDisplayName(Locale.getDefault());
	}


	public String getProperty()
	{
		return tz.getID();
	}
	
	
	public String toString()
	{
		return getDisplayName() + " - " + getID();
	}
	
	
	public String getID()
	{
		return tz.getID();
	}
	
	
	public static String getID(CTimeZone tz)
	{
		return tz == null ? null : tz.getID();
	}
	
	
	public int getOffset(long t)
	{
		return tz.getOffset(t);
	}
	
	
	public static String toStringCode(CTimeZone z)
	{
		if(z != null)
		{
			return z.getID();
		}
		return null;
	}
	
	
	public boolean equals(Object x)
	{
		if(this == x)
		{
			return true;
		}
		else if(x instanceof CTimeZone)
		{
			return tz.equals(((CTimeZone)x).tz);
		}
		else
		{
			return false;
		}
	}
	
	
	public int hashCode()
	{
		return CTimeZone.class.hashCode() ^ tz.hashCode();
	}
	
	
	public static CTimeZone getDefault()
	{
		return new CTimeZone(TimeZone.getDefault());
	}
	
	
	public static CTimeZone getTimeZone(String s)
	{
		if(s != null)
		{
			return new CTimeZone(TimeZone.getTimeZone(s));
		}
		return null;
	}


	public static CTimeZone[] getAll()
	{
		String[] ids = TimeZone.getAvailableIDs();
		int sz = ids.length;

		CTimeZone[] a = new CTimeZone[sz];
		for(int i=0; i<sz; i++)
		{
			String id = ids[i];
			a[i] = new CTimeZone(TimeZone.getTimeZone(id));
		}
		return a;
	}


	public static CTimeZone[] getAllSorted()
	{
		CTimeZone[] tz = getAll();
		CSorter.sort(tz);
		return tz;
	}
	
	
	public static CTimeZone parse(Object x)
	{
		if(x != null)
		{
			if(x instanceof CTimeZone)
			{
				return (CTimeZone)x;
			}
			else
			{
				String s = Parsers.parseString(x);
				if(CKit.isNotBlank(s))
				{
					TimeZone tz = TimeZone.getTimeZone(s);
					return new CTimeZone(tz);
				}
			}
		}
		return null;
	}
	
	
	/** returns city name */
	public String getCityName()
	{
		String s = getID();
		s = s.replace('_', ' ');
		int ix = s.indexOf('/');
		if(ix < 0)
		{
			return s;
		}
		else
		{
			return s.substring(ix + 1);
		}
	}
}
