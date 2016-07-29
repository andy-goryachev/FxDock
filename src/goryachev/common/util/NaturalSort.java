// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.math.BigInteger;


/**
 * Utility class performs natural order comparison of strings.
 * 
 * At first, strings are compared by looking at the alphanumeric parts, ignoring
 * case, whitespace, and punctuation.  If these parts appear to be the same, 
 * punctuation parts are compared.  If the punctuation appears to be the same, 
 * the strings are compared using String.compareTo().
 * 
 * When comparing numbers, a simple algorithm is used that looks at uninterrupted 
 * sequences of ASCII digits.
 * 
 * TODO normalize unicode
 * TODO handle various numeric chars such as full-width digits
 * TODO ignore accents
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
			// first stage is to look at text and numbers only
			int rv = compareTextAndNumbers(a, b);
			if(rv == 0)
			{
				// second stage, see if punctuation is different
				rv = comparePunctuation(a, b);
				if(rv == 0)
				{
					// third stage, straightforward string comparison
					rv = a.compareTo(b);
				}
			}
			return rv;
		}
	}
	
	
	private static int charAt(String s, int ix)
	{
		if(ix < s.length())
		{
			int c = s.codePointAt(ix);
			return Character.toLowerCase(c);
		}
		return -1;
	}
	
	
	private static boolean isNotTextOrNumber(int c)
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
	
	
	private static boolean isNotPunctuation(int c)
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
			return true;
		}
		
		return false;
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
	
	
	/** compares two strings looking at text and numbers, ignoring whitespace and punctuation. */
	private static int compareTextAndNumbers(String a, String b)
	{
		int ixa = 0;
		int ixb = 0;
		
		for(;;)
		{
			int ca;
			int cb;
			
			boolean skipa = false;
			boolean skipb = false;
			
			while(isNotTextOrNumber(ca = charAt(a, ixa)))
			{
				skipa = true;
				ixa += Character.charCount(ca);
			}
			
			while(isNotTextOrNumber(cb = charAt(b, ixb)))
			{
				skipb = true;
				ixb += Character.charCount(cb);
			}
			
			if(skipa != skipb)
			{
				if(skipa)
				{
					if(cb < 0)
					{
						return 1;
					}
					else
					{
						return -1;
					}
				}
				else if(skipb)
				{
					if(ca < 0)
					{
						return -1;
					}
					else
					{
						return 1;
					}
				}
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
			else if(ca == cb)
			{
				if(ca < 0)
				{
					return 0;
				}
				else
				{
					ixa += Character.charCount(ca);
					ixb += Character.charCount(cb);
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
				else
				{
					return ca - cb;
				}
			}
		}
	}
	
	
	/** compares two strings looking at punctuation only and ignoring everything else. */
	private static int comparePunctuation(String a, String b)
	{
		int ixa = 0;
		int ixb = 0;
		
		for(;;)
		{
			int ca;
			int cb;
			
			while(isNotPunctuation(ca = charAt(a, ixa)))
			{
				ixa++;
			}
			
			while(isNotPunctuation(cb = charAt(b, ixb)))
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
				else
				{
					return ca - cb;
				}
			}
		}
	}
}
