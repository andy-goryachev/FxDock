// Copyright © 2017-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import java.util.function.BiConsumer;


/**
 * Simple Word Selector uses whitespace and punctuation to delimit words.
 */
public class SimpleWordSelector
	implements BiConsumer<FxEditor,Marker>
{
	public SimpleWordSelector()
	{
	}
	
	
	protected boolean isWordChar(int c)
	{
		return Character.isLetterOrDigit(c);
	}
	
	
	protected int skipWordCharsForward(String text, int start)
	{
		int len = text.length();
		for(int i=start; i<len; i++)
		{
			// TODO surrogate
			char c = text.charAt(i);
			if(!isWordChar(c))
			{
				return i;
			}
		}
		return len;
	}
	
	
	protected int skipNonWordCharsForward(String text, int start)
	{
		int len = text.length();
		for(int i=start; i<len; i++)
		{
			// TODO surrogate
			char c = text.charAt(i);
			if(isWordChar(c))
			{
				return i;
			}
		}
		return len;
	}
	
	
	protected int skipWordCharsBackward(String text, int start)
	{
		for(int i=start; i>=0; i--)
		{
			// TODO surrogate
			char c = text.charAt(i);
			if(!isWordChar(c))
			{
				return i;
			}
		}
		// this is legitimate offset
		return -1;
	}
	
	
	protected int skipNonWordCharsBackward(String text, int start)
	{
		for(int i=start; i>=0; i--)
		{
			// TODO surrogate
			char c = text.charAt(i);
			if(isWordChar(c))
			{
				return i;
			}
		}
		// this is legitimate offset
		return -1;
	}
	

	public void accept(FxEditor ed, Marker m)
	{
		int line = m.getLine();
		String text = ed.getTextOnLine(line);
		if(text == null)
		{
			return;
		}
		
		int len = ed.getTextLength(line);
		if(len == 0)
		{
			return;
		}

		int pos = m.getCharIndex();

		int start;
		int end;
		
		if(isWordChar(text.charAt(pos)))
		{
			start = skipWordCharsBackward(text, pos) + 1;
			end = skipWordCharsForward(text, pos) - 1;
		}
		else
		{
			// hit whitespace.  let's try going forward first
			// TODO we might try selecting the closest word instead
			start = skipNonWordCharsForward(text, pos);
			if(start == len)
			{
				// nothing to the right.  let's go backwards
				end = skipNonWordCharsBackward(text, pos);
				if(end < 0)
				{
					// nothing to select
					return;
				}
				else
				{
					start = skipWordCharsBackward(text, end);
					if(start == end)
					{
						return;
					}
					start++;
				}
			}
			else
			{
				end = skipWordCharsForward(text, start);
				if(end == start)
				{
					return;
				}
				
				end--;
			}
		}
		
		Marker m0 = ed.newMarker(line, start, true);
		Marker m1 = ed.newMarker(line, end, false);
		ed.select(m0, m1);
	}
}
