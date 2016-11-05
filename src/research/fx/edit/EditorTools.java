// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;
import goryachev.common.util.D;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.PathElement;


/**
 * Editor Tools.
 */
public class EditorTools
{
	public static PathElement[] translate(Region target, Node dst, PathElement[] es)
	{
		Point2D p = dst.localToScreen(0, 0);
		p = target.screenToLocal(p);
		double dx = p.getX();
		double dy = p.getY();
		
		int sz = es.length;
		PathElement[] rv = new PathElement[sz];
		
		for(int i=0; i<sz; i++)
		{
			PathElement em = es[i];
			if(em instanceof LineTo)
			{
				LineTo m = (LineTo)em;
				rv[i] = new LineTo(halfPixel(m.getX() + dx), halfPixel(m.getY() + dy));
			}
			else if(em instanceof MoveTo)
			{
				MoveTo m = (MoveTo)em;
				rv[i] = new MoveTo(halfPixel(m.getX() + dx), halfPixel(m.getY() + dy));
			}
			else
			{
				D.print(em); // FIX
			}
		}
		
		return rv;
	}
	
	
	public static double halfPixel(double coord)
	{
		return Math.round(coord + 0.5) - 0.5;
	}
	
	
//	public static double snap(double coord)
//	{
//		return Math.round(coord);
//	}
}
