// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock.dnd;
import goryachev.common.util.D;
import javafx.scene.Node;


/**
 * Drop Operation - contains highlights and the code to handle the drop.
 */
public abstract class DropOp
{
	private final Node target;
	private final Object where;
	
	
	public DropOp(Node target, Object where)
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


	public void installHighlights()
	{
	}


	public void removeHighlights()
	{
	}


	public void execute()
	{
		D.print(); // TODO
	}
}
