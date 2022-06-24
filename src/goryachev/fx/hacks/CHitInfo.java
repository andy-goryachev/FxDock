// Copyright © 2017-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.hacks;


/**
 * A public version of HitInfo.
 */
public class CHitInfo
{
	private final int charIndex;
	private final boolean leading;


	public CHitInfo(int charIndex, boolean leading)
	{
		this.charIndex = charIndex;
		this.leading = leading;
	}
	
	
	public String toString()
	{
		return charIndex + (leading ? ".L" : ".T");
	}


	public int getCharIndex()
	{
		return charIndex;
	}


	public boolean isLeading()
	{
		return leading;
	}


	public int getInsertionIndex()
	{
		return leading ? charIndex : charIndex + 1;
	}
}
