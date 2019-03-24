// Copyright © 2017-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.common.util.CList;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 * FxEditor Selection Controller.
 */
public class SelectionController
{
	public final ObservableList<SelectionSegment> segments = FXCollections.observableArrayList();
	private final ReadOnlyObjectWrapper<EditorSelection> selectionProperty = new ReadOnlyObjectWrapper(EditorSelection.EMPTY);
	private Marker anchor;
	private CList<SelectionSegment> originalSelection;


	public SelectionController()
	{
	}
	
	
	public ReadOnlyObjectProperty<EditorSelection> selectionProperty()
	{
		return selectionProperty.getReadOnlyProperty();
	}
	
	
	public EditorSelection getSelection()
	{
		return selectionProperty.get();
	}


	public void clear()
	{
		segments.clear();
		originalSelection = null;
	}
	
	
	/** returns true if marker is inside of any selection segment */
	public boolean isInsideOfSelection(Marker pos)
	{
		for(SelectionSegment s: segments)
		{
			if(s.contains(pos))
			{
				return true;
			}
		}
		return false;
	}
	
	
	public void setSelection(Marker anchor, Marker caret)
	{
		clear();
		addSelectionSegment(anchor, caret);
	}
	

	public void addSelectionSegment(Marker anchor, Marker caret)
	{
		mergeSegments(new SelectionSegment(anchor, caret));
		originalSelection = null;
	}
	
	
	public void setSelection(Marker m)
	{
		setSelection(m, m);
	}
	
	
	public void clearAndExtendLastSegment(Marker pos)
	{
		if(anchor == null)
		{
			anchor = pos;
		}
		
		setSelection(anchor, pos);
	}
	
	
	public void setAnchor(Marker p)
	{
		anchor = p;
		originalSelection = new CList<>(segments);
	}
	
	
	/** 
	 * extends the new selection segment from the anchor point to the specified position,
	 * updating the segments list such that it remains to be ordered and the segments do not overlap each other
	 */
	public void extendLastSegment(Marker pos)
	{
		if(anchor == null)
		{
			anchor = pos;
		}
		
		SelectionSegment seg = new SelectionSegment(anchor, pos);
		mergeSegments(seg);
	}
	
	
	protected void mergeSegments(SelectionSegment seg)
	{
		if(originalSelection == null)
		{
			originalSelection = new CList<>(segments);
		}
		
		// merge last segment and original selection to produce ordered, non-overlapping segments
		CList<SelectionSegment> ordered = new CList(originalSelection.size() + 1);
		for(SelectionSegment s: originalSelection)
		{
			if(seg == null)
			{
				ordered.add(s);
			}
			else
			{
				if(seg.overlaps(s))
				{
					seg = seg.merge(s);
				}
				else if(seg.isBefore(s))
				{
					ordered.add(seg);
					ordered.add(s);
					seg = null;
				}
				else
				{
					ordered.add(s);
				}
			}
		}
		
		if(seg != null)
		{
			ordered.add(seg);
		}
		
		// sorted list contains the new selection
		// we could merge the original list, but for now it's easier just to replace the items
		segments.setAll(ordered);
	}
	

	/** called at the end of drag gesture to clear transient values and update the selection property */
	public void commitSelection()
	{
		originalSelection = null;
		
		EditorSelection es = new EditorSelection(segments.toArray(new SelectionSegment[segments.size()]));
		selectionProperty.set(es);
	}
}
