// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;


/**
 * Selection Segment.
 */
public class SelectionSegment
{
	protected final TextPos start;
	protected final TextPos end;
	
	
	public SelectionSegment(TextPos start, TextPos end)
	{
		this.start = start;
		this.end = end;
	}
	
	
	public TextPos getStart()
	{
		return start;
	}
	
	
	public TextPos getEnd()
	{
		return end;
	}
}
