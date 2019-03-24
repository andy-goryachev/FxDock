// Copyright © 2017-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.common.util.Assert;
import goryachev.common.util.FH;
import goryachev.common.util.SB;
import goryachev.fx.edit.internal.Markers;


/**
 * Marker represents a position in the text model maintained 
 * in the presence of insertion and removals.
 * 
 * TODO perhaps it should refer to insert position instead of (char index / leading)
 */
public class Marker
	implements Comparable<Marker>
{
	public static final Marker ZERO = new Marker();
	private int line;
	private int charIndex;
	private boolean leading;
	
	
	public Marker(Markers owner, int line, int charIndex, boolean leading)
	{
		Assert.notNull(owner, "owner");
		
		this.line = line;
		this.charIndex = charIndex;
		this.leading = leading;		
	}
	
	
	private Marker()
	{
		this.line = 0;
		this.charIndex = 0;
		this.leading = true;	
	}
	
	
	public void reset(int line, int charIndex, boolean leading)
	{
		this.line = line;
		this.charIndex = charIndex;
		this.leading = leading;
	}
	

	public int hashCode()
	{
		int h = FH.hash(Marker.class);
		h = FH.hash(h, line);
		h = FH.hash(h, charIndex);
		return FH.hash(h, leading);
	}


	/** returns the line index */
	public int getLine()
	{
		return line;
	}
	
	
	/** returns the effective caret position */
	public int getLineOffset()
	{
		return leading ? charIndex : charIndex + 1;
	}
	
	
	public int getCharIndex()
	{
		return charIndex;
	}
	
	
	public boolean isLeading()
	{
		return leading;
	}
	
	
	public String toString()
	{
		SB sb = new SB(16);
		sb.a(line);
		sb.a(':');
		sb.a(getCharIndex());
		if(leading)
		{
			sb.a('L');
		}
		else
		{
			sb.a('T');
		}
		sb.a(':');
		sb.a(getLineOffset());
		return sb.toString();
	}

	
	public int compareTo(Marker m)
	{
		int d = line - m.line;
		if(d == 0)
		{
			d = getLineOffset() - m.getLineOffset();
			if(d == 0)
			{
				if(leading != m.leading)
				{
					return leading ? -1 : 1;
				}
			}
		}
		return d;
	}
	
	
	public boolean equals(Object x)
	{
		if(x == this)
		{
			return true;
		}
		else if(x instanceof Marker)
		{
			Marker m = (Marker)x;
			return (leading == m.leading) && (line == m.line) && (charIndex == m.charIndex);
		}
		else
		{
			return false;
		}
	}


	public boolean isBefore(Marker m)
	{
		if(line < m.line)
		{
			return true;
		}
		else if(line == m.line)
		{
			// TODO or use insertion index?
			if(charIndex < m.charIndex)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
	
	
	public boolean isBefore(int line, int pos)
	{
		if(this.line < line)
		{
			return true;
		}
		else if(this.line == line)
		{
			if(getPosition() < pos)
			{
				return true;
			}
		}
		return false;
	}
	
	
	public boolean isAfter(int line, int pos)
	{
		if(this.line > line)
		{
			return true;
		}
		else if(this.line == line)
		{
			if(getPosition() > pos)
			{
				return true;
			}
		}
		
		return false;
	}
	
	
	public void moveLine(int delta)
	{
		line += delta;
	}
	
	
	public void moveCharIndex(int delta)
	{
		// TODO validate
		charIndex += delta;
	}
	
	
	public int getPosition()
	{
		// TODO perhaps I should use position only in the marker
		if(leading)
		{
			return charIndex;
		}
		else
		{
			return charIndex + 1;
		}
	}
}
