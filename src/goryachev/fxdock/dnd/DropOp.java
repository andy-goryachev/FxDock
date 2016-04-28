// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock.dnd;
import goryachev.common.util.CList;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;


/**
 * Drop Operation - contains highlights and the code to handle the drop.
 */
public abstract class DropOp
{
	public abstract void execute();
	
	//
	
	private final Pane target;
	private final Object where;
	private CList<Node> highlights = new CList();
	
	
	public DropOp(Pane target, Object where)
	{
		this.target = target;
		this.where = where;
	}


	public boolean isSame(DropOp op)
	{
		if(op != null)
		{
			return 
				(target == op.target) &&
				(where.equals(op.where));
		}
		return false;
	}
	
	
	public void addRect(Node ref, double x, double y, double w, double h)
	{
		BoundingBox screenr = new BoundingBox(x, y, w, h);
		Bounds b = ref.localToScreen(screenr);
		b = target.screenToLocal(b);
		
		Region r = new Region();
		r.relocate(b.getMinX(), b.getMinY());
		r.resize(b.getWidth(), b.getHeight());
		r.setBackground(new Background(new BackgroundFill(Color.color(0, 0, 0, 0.3), null, null)));
		highlights.add(r);
	}


	public void installHighlights()
	{
		target.getChildren().addAll(highlights);
	}


	public void removeHighlights()
	{
		target.getChildren().removeAll(highlights);
	}
}
