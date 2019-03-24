// Copyright © 2017-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.util.CList;
import goryachev.fx.edit.CTextFlow;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;


/**
 * TextFlow With Highlights.
 * 
 * Since we can't style Text background directly, this has to be done in TextFlow.
 */
public class TextFlowWithHighlights
	extends CTextFlow
{
	private final CList<HL> highlights = new CList();
	
	
	public TextFlowWithHighlights()
	{
	}
	
	
	public void clearHighlights()
	{
		removeHighlights();
		highlights.clear();
	}
	
	
	/**
	 * adds a highlight.  the node that implements a highlight is a Path (Shape)
	 */
	public void addHighlight(CssStyle style, int start, int end)
	{
		HL h = new HL();
		h.style = style;
		h.start = start;
		h.end = end;
		highlights.add(h);
		requestLayout();
	}
	
	
	protected void layoutChildren()
	{
		super.layoutChildren();
		
		updateHighlights();
	}
	
	
	protected void updateHighlights()
	{
		removeHighlights();
		
		int ix = 0;
		for(HL h: highlights)
		{
			PathElement[] es = getRange(h.start, h.end);
			
			Path p = new Path(es);
			p.setManaged(false);
			p.getStyleClass().add(h.style.getName());
			getChildren().add(ix, p);
			
			h.path = p;
			ix++;
		}
	}
	
	
	protected void removeHighlights()
	{
		for(HL h: highlights)
		{
			if(h.path != null)
			{
				getChildren().remove(h.path);
			}
		}
	}
	
	
	//
	
	
	protected static class HL
	{
		public CssStyle style;
		public int start;
		public int end;
		public Path path;
	}
}
