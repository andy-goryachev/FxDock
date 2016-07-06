// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.math.BigInteger;


/**
 * Utility class performs natural order comparison of strings:
 * - whitespace ignored
 * - case ignored
 * - integer numbers considered as an atomic elements when comparing 
 */
public class NaturalSort
{
	public static int compare(String a, String b)
	{
		if(a == null)
		{
			return b == null ? 0 : -1;
		}
		else if(b == null)
		{
			return 1;
		}
		else
		{
			return comparePrivate(a, b);
		}
	}
	
	
	private static int charAt(String s, int ix)
	{
		if(ix < s.length())
		{
			return Character.toLowerCase(s.charAt(ix));
		}
		return -1;
	}
	
	
	private static boolean isIgnore(int c)
	{
		if(c < 0)
		{
			return false;
		}
		
		if(CKit.isBlank(c))
		{
			return true;
		}
		
		if(Character.isLetterOrDigit(c))
		{
			return false;
		}
		
		return true;
	}
	
	
	private static String number(String s, int ix)
	{
		int start = ix;
		
		for(;;)
		{
			ix++;
			
			int c = charAt(s, ix);
			if((c < 0) || !Character.isDigit(c))
			{
				return s.substring(start, ix);
			}
		}
	}
	
	
	private static int compareNumbers(String a, String b)
	{
		if((a.length() < 18) && (b.length() < 18))
		{
			try
			{
				long na = Long.parseLong(a);
				long nb = Long.parseLong(b);
				
				if(na < nb)
				{
					return -1;
				}
				else if(na > nb)
				{
					return 1;
				}
				else
				{
					return 0;
				}
			}
			catch(Exception ignore)
			{ }
		}
		
		// unlikely to get here, but just in case
		BigInteger ai = new BigInteger(a);
		BigInteger bi = new BigInteger(b);
		return ai.compareTo(bi);
	}
	
	
	private static int comparePrivate(String a, String b)
	{
		int ixa = 0;
		int ixb = 0;
		
		for(;;)
		{
			int ca;
			int cb;
			
			while(isIgnore(ca = charAt(a, ixa)))
			{
				ixa++;
			}
			
			while(isIgnore(cb = charAt(b, ixb)))
			{
				ixb++;
			}
			
			if(ca == cb)
			{
				if(ca < 0)
				{
					return 0;
				}
				else
				{
					ixa++;
					ixb++;
					continue;
				}
			}
			else
			{
				if(ca < 0)
				{
					return -1;
				}
				else if(cb < 0)
				{
					return 1;
				}
				
				if(Character.isDigit(ca) && Character.isDigit(cb))
				{
					String na = number(a, ixa);
					String nb = number(b, ixb);
					int d = compareNumbers(na, nb);
					if(d == 0)
					{
						ixa += na.length();
						ixb += nb.length();
					}
					else
					{
						return d;
					}
				}
				else
				{
					return ca - cb;
				}
			}
		}
	}
}
