// Copyright © 2021 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.internal;


/**
 * Simple Text Style Flags
 */
public class TextStyleFlags
{
	private static final int BOLD = 1;
	private static final int ITALIC = 1 << 2;
	private static final int UNDERSCORE = 1 << 3;
	private static final int STRIKETHROUGH = 1 << 4;

	
	public static boolean isBold(int x)
	{
		return (x & BOLD) != 0;
	}
	
	
	public static void setBold(byte[] flags, int ix, boolean on)
	{
		if(on)
		{
			flags[ix] |= BOLD;
		}
		else
		{
			flags[ix] &= (~BOLD);
		}
	}
	
	
	public static boolean isItalic(int x)
	{
		return (x & ITALIC) != 0;
	}
	
	
	public static void setItalic(byte[] flags, int ix, boolean on)
	{
		if(on)
		{
			flags[ix] |= ITALIC;
		}
		else
		{
			flags[ix] &= (~ITALIC);
		}
	}
	
	
	public static boolean isStrikeThrough(int x)
	{
		return (x & STRIKETHROUGH) != 0;
	}
	
	
	public static void setStrikeThrough(byte[] flags, int ix, boolean on)
	{
		if(on)
		{
			flags[ix] |= STRIKETHROUGH;
		}
		else
		{
			flags[ix] &= (~STRIKETHROUGH);
		}
	}
	
	
	public static boolean isUnderscore(int x)
	{
		return (x & UNDERSCORE) != 0;
	}
	
	
	public static void setUnderscore(byte[] flags, int ix, boolean on)
	{
		if(on)
		{
			flags[ix] |= UNDERSCORE;
		}
		else
		{
			flags[ix] &= (~UNDERSCORE);
		}
	}
}
