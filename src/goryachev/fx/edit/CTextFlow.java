// Copyright © 2016-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit;
import goryachev.common.util.SB;
import javafx.scene.Node;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.PathElement;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;


/**
 * A TextFlow with public methods otherwise inaccessible in JavaFX.
 */
public class CTextFlow
	extends TextFlow
{
	public CTextFlow(Node ... children)
	{
		super(children);
	}


	public CTextFlow()
	{
	}


	public PathElement[] getCaretShape(int index, boolean leading)
	{
		PathElement[] pe = FxHacks.get().getCaretShape(this, index, leading);
		
		// empty line generates a simple dot shape which is not what we need
		if(pe.length == 2)
		{
			PathElement p0 = pe[0];
			PathElement p1 = pe[1];
			if((p0 instanceof MoveTo) && (p1 instanceof LineTo))
			{
				MoveTo m0 = (MoveTo)p0;
				LineTo m1 = (LineTo)p1;
				
				// close enough?
				if(Math.abs(m0.getY() - m1.getY()) < 0.01)
				{
					double x = m0.getX();
					double y = m0.getY();
					
					// little hack: using the text flow height
					pe[1] = new LineTo(x, y + getHeight());
				}
			}
		}
		return pe;
	}
	
	
	/** returns selection shape for a given range.  negative 'end' value is equivalent to the offset of the last symbol in the text */
	public PathElement[] getRange(int start, int end)
	{
		PathElement[] pe = FxHacks.get().getRange(this, start, end);
		if(pe.length == 0)
		{
			// happens with empty line
			pe = new PathElement[2];
			pe[0] = new MoveTo(0, 0);
			pe[1] = new LineTo(0, getHeight());
		}
		return pe;
	}


	/** returns hit info at the specified local coordinates */
	public CHitInfo getHit(double x, double y)
	{
		return FxHacks.get().getHit(this, x, y);
	}
	
	
	public String getText()
	{
		SB sb = new SB();
		for(Node n: getChildrenUnmodifiable())
		{
			if(n instanceof Text)
			{
				sb.append(((Text)n).getText());
			}
		}
		return sb.toString();
	}
	
	
	public void add(Node n)
	{
		getChildren().add(n);
	}
}
