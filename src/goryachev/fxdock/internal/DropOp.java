// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock.internal;
import goryachev.common.util.CList;
import goryachev.fx.FX;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;


/**
 * Drop Operation - contains highlights and the code to handle the drop.
 */
public abstract class DropOp
{
	protected abstract void executePrivate();
	
	//
	
	private final Pane target;
	private final Object where;
	private CList<Node> highlights;
	
	
	public DropOp(Pane target, Object where)
	{
		this.target = target;
		this.where = where;
	}
	
	
	public void execute()
	{
		executePrivate();
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
		r.setBackground(FX.background(Color.color(0, 0, 0, 0.3)));
		
		add(r);
	}
	
	
	public void addOutline(Node ref, double x, double y, double w, double h)
	{
		BoundingBox screenr = new BoundingBox(x, y, w, h);
		Bounds b = ref.localToScreen(screenr);
		b = target.screenToLocal(b);
		
		Region r = new Region();
		r.relocate(b.getMinX(), b.getMinY());
		r.resize(b.getWidth(), b.getHeight());
		r.setBackground(FX.background(Color.color(0, 0, 0, 0.1)));
		
		add(r);
	}
	
	
	protected void add(Node n)
	{
		if(highlights == null)
		{
			highlights = new CList<>();
		}
		highlights.add(n);
	}


	public void installHighlights()
	{
		if(highlights != null)
		{
			target.getChildren().addAll(highlights);
		}
	}


	public void removeHighlights()
	{
		if(highlights != null)
		{
			target.getChildren().removeAll(highlights);
		}
	}
	
	
	public String toString()
	{
		return "op:" + where;
	}
}
