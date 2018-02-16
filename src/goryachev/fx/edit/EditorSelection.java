// Copyright © 2017-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.common.util.CComparator;
import goryachev.common.util.CKit;
import goryachev.common.util.Rex;


/**
 * An immutable object that represents text selection within FxEditor.
 */
public class EditorSelection
{
	public static final EditorSelection EMPTY = createEmpty();
	private final SelectionSegment[] segments;
	
	
	public EditorSelection(SelectionSegment[] segments)
	{
		this.segments = segments;
		
		// TODO remove this check later
		check();
	}
	
	
	public String toString()
	{
		return CKit.toString(segments);
	}
	
	
	private void check()
	{
		SelectionSegment prev = null;
		for(SelectionSegment s: segments)
		{
			if(prev != null)
			{
				if(!prev.isBefore(s))
				{
					throw new Rex("selection is not ordered " + this);
				}
			}
			prev = s;
		}
	}
	
	
	private static EditorSelection createEmpty()
	{
		return new EditorSelection(new SelectionSegment[] { new SelectionSegment(Marker.ZERO, Marker.ZERO, false) });
	}

	
	/** returns original segment array */
	public SelectionSegment[] getSegments()
	{
		return segments;
	}
	
	
	/** returns last or the only selection segment */
	public SelectionSegment getSegment()
	{
		if(segments.length == 0)
		{
			return null;
		}
		return segments[segments.length - 1];
	}
	
	
	public int getSegmentCount()
	{
		return segments.length;
	}
	
	
	public boolean isEmpty()
	{
		for(SelectionSegment s: segments)
		{
			if(!s.isEmpty())
			{
				return false;
			}
		}
		return false;
	}
	
	
	public boolean isNotEmpty()
	{
		return !isEmpty();
	}
	
	
	public boolean hasMultipleSegments()
	{
		return segments.length > 1;
	}


	public EditorSelection getSelection()
	{
		return new EditorSelection(segments);
	}
}
