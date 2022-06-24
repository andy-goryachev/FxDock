// Copyright © 2019-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util.text;
import java.text.BreakIterator;


/**
 * BreakIterator interface.
 *
 * Usage:<pre>
 * bi.setText(text);
 * int start = bi.first();
 * for(int end=bi.next(); end!=BreakIterator.DONE; start=end, end=bi.next())
 * {
 *      String s = text.substring(start,end);
 * }
 */
public interface IBreakIterator
{
	public static final int DONE = -1;
	
	//
	
	public void setText(String text);

	public int first();

	public int next();
	
	public IBreakIterator copy();
	
	//

	/** 
	 * wraps a standard java.util.BreakIterator instance.
	 * it is recommended to use com.ibm.icu.text.BreakIterator instead because
	 * the stock java one is not complete (emoji!)
	 */
	public static IBreakIterator wrap(BreakIterator br)
	{
		return new IBreakIterator()
		{
			public void setText(String text)
			{
				br.setText(text);
			}


			public int first()
			{
				return br.first();
			}


			public int next()
			{
				int rv = br.next();
				if(rv == BreakIterator.DONE)
				{
					return DONE;
				}
				return rv;
			}
			
			
			public IBreakIterator copy()
			{
				return (IBreakIterator)br.clone();
			}
		};
	}
}
