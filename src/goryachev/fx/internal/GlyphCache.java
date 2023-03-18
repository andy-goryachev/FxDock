// Copyright © 2021 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.internal;


/**
 * Glyph Cache quickly converts characters to String for a limited set of alphabets:
 * Latin, Greek, Cyrillic.
 */
public class GlyphCache
{
	private static final int CAPACITY = 0x04ff + 1;
	private static final String[] cache = new String[CAPACITY];
	
	
	/** converts character to a String, caching Latin, Greek, and Cyrillic */
	public static String get(char ch)
	{
		if(ch < CAPACITY)
		{
			String s = cache[ch];
			if(s == null)
			{
				s = String.valueOf(ch);
				cache[ch] = s;
			}
			return s;
		}
		else
		{
			return String.valueOf(ch);
		}
	}
}
