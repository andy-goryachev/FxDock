// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package research.fx.edit;
import goryachev.common.util.D;
import goryachev.fx.FxSize;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.PathElement;
import javafx.scene.text.Text;


/**
 * Editor Tools.
 */
public class EditorTools
{
	private static Text helper = new Text();

	
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
	
	
	// from http://stackoverflow.com/questions/15593287/binding-textarea-height-to-its-content/19717901#19717901
	// FIX return Dimension2D (or Dim)
	public static FxSize getTextBounds(TextArea t, double width)
	{
		String text = t.getText();
		if(width < 0)
		{
			// Note that the wrapping width needs to be set to zero before
			// getting the text's real preferred width.
			helper.setWrappingWidth(0);
		}
		else
		{
			helper.setWrappingWidth(width);
		}
		helper.setText(text);
		helper.setFont(t.getFont());
		Bounds r = helper.getLayoutBounds();
		
		Insets m = t.getInsets();
		Insets p = t.getPadding();
		double w = Math.ceil(r.getWidth() + m.getLeft() + m.getRight());
		double h = Math.ceil(r.getHeight() + m.getTop() + m.getBottom());
		
		return new FxSize(w, h);
	}
}
