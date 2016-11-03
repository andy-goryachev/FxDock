// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;
import goryachev.common.util.FH;


/**
 * Text Position.
 */
public class TextPos
{
	private final int index;
	private final boolean leading;
	
	
	public TextPos(int index, boolean leading)
	{
		this.index = index;
		this.leading = leading;
	}
	
	
	public String toString()
	{
		return index + ":" + (leading ? "leading" : "trailing");
	}


	public int getInsertionIndex()
	{
		return leading ? index : index + 1;
	}


	public int getIndex()
	{
		return index;
	}
	
	
	public boolean isLeading()
	{
		return leading;
	}
	
	
	public boolean equals(Object x)
	{
		if(x == this)
		{
			return true;
		}
		else if(x instanceof TextPos)
		{
			TextPos z = (TextPos)x;
			return (index == z.index) && (leading == z.leading);
		}
		else
		{
			return false;
		}
	}


	public int hashCode()
	{
		int h = FH.hash(TextPos.class);
		h = FH.hash(h, index);
		return FH.hash(h, leading);
	}
}
