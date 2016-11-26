// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;
import goryachev.common.util.CList;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.shape.PathElement;
import research.fx.edit.internal.TextPosExt;


/**
 * FxEditor Layout.
 */
public class FxEditorLayout
{
	private int offsety;
	private int startLine;
	private CList<LineBox> lines = new CList<>();
	

	public FxEditorLayout(int startLine, int offsety)
	{
		this.startLine = startLine;
		this.offsety = offsety;
	}
	
	
	/** returns text position at the screen coordinates, or null */
	public TextPosExt getTextPos(double screenx, double screeny)
	{
		for(LineBox b: lines)
		{
			Region box = b.box;
			Point2D p = box.screenToLocal(screenx, screeny);
			double y = p.getY();
			
			if(y > 0)
			{
				if(y < box.getHeight())
				{
					if(box instanceof CTextFlow)
					{
						TextPos pos = ((CTextFlow)box).getTextPos(b.line, p.getX(), y);
						if(pos != null)
						{
							return new TextPosExt(b, pos);
						}
					}
				}
			}
			else
			{
				break;
			}
		}
		return null;
	}
	
	
	public LineBox getLineBox(int line)
	{
		line -= startLine;
		if((line >= 0) && (line < lines.size()))
		{
			return lines.get(line);
		}
		return null;
	}
	
	
	/** returns caret shape at the screen coordinates, or null */
	@Deprecated
	public PathElement[] getCaretShape(Region parent, double screenx, double screeny)
	{
		TextPosExt pos = getTextPos(screenx, screeny);
		if(pos != null)
		{
			Region box = pos.line.box;
			if(box instanceof CTextFlow)
			{
				PathElement[] es = ((CTextFlow)box).getCaretShape(pos.getIndex(), pos.isLeading());
				if(es != null)
				{
					return EditorTools.translate(parent, box, es);
				}
			}
		}
		return null;
	}
	
	
	public CaretLocation getCaretLocation(Region parent, TextPos pos)
	{
		if(pos != null)
		{
			LineBox b = getLineBox(pos.getLine());
			if(b != null)
			{
				Region box = b.box;
				if(box instanceof CTextFlow)
				{
					PathElement[] es = ((CTextFlow)box).getCaretShape(pos.getIndex(), pos.isLeading());
					if(es != null)
					{
						return EditorTools.translateCaretLocation(parent, box, es);
					}
				}
			}
		}
		return null;
	}
	
	
	public void addTo(ObservableList<Node> cs)
	{
		for(LineBox b: lines)
		{
			cs.add(b.box);
		}
	}


	public void removeFrom(ObservableList<Node> cs)
	{
		for(LineBox b: lines)
		{
			cs.remove(b.box);
		}
	}


	public void add(LineBox b)
	{
		lines.add(b);
	}


	public int startLine()
	{
		return startLine;
	}
}