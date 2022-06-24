// Copyright © 2013-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


/** Logic for dot-separated version numbers */
public class DotSeparatedVersion
{
	/** parses dot-separated version number into an array of integers, ignoring trailing non-numbers */
	public static int[] parse(String text)
	{
		String[] ss = CKit.split(text, '.');
		int sz = ss.length;
		int[] rv = new int[sz];
		
		for(int i=0; i<sz; i++)
		{
			rv[i] = parseNumber(ss[i]);
		}
		
		return rv;
	}
	
	
	public static int compare(String a, String b) throws Exception
	{
		int[] ai = parse(a);
		int[] bi = parse(b);
		
		int sz = Math.min(ai.length, bi.length);
		if(sz > 0)
		{
			int d;
			for(int i=0; i<sz; i++)
			{
				d = ai[i] - bi[i];
				if(d < 0)
				{
					return -1;
				}
				else if(d > 0)
				{
					return 1;
				}
			}
			
			d = ai.length - bi.length;
			return d;
		}
		
		throw new Exception("unable to compare " + a + " and " + b);
	}
	
	
	private static int parseNumber(String s)
	{
		// trim trailing non-numbers
		for(int i=0; i<s.length(); i++)
		{
			char c = s.charAt(i);
			if(!Character.isDigit(c))
			{
				s = s.substring(0, i);
			}
		}
		
		return Parsers.parseInt(s, 0);
	}
}
