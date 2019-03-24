// Copyright © 2016-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit.internal;
import goryachev.common.util.SB;
import goryachev.fx.FxSize;
import java.text.DecimalFormat;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
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

	
	public static CaretLocation translateCaretLocation(Region target, Region src, PathElement[] elements)
	{
		double x = 0.0;
		double y0 = 0.0;
		double y1 = 0.0;
		
		Insets pad = src.getPadding();
		Point2D p = src.localToScreen(pad.getLeft(), pad.getTop());
		
		p = target.screenToLocal(p);
		double dx = p.getX();
		double dy = p.getY();
		
		int sz = elements.length;
		for(int i=0; i<sz; i++)
		{
			PathElement em = elements[i];
			if(em instanceof LineTo)
			{
				LineTo m = (LineTo)em;
				x = halfPixel(m.getX() + dx);
				y0 =  halfPixel(m.getY() + dy);
			}
			else if(em instanceof MoveTo)
			{
				MoveTo m = (MoveTo)em;
				x = halfPixel(m.getX() + dx);
				y1 = halfPixel(m.getY() + dy);
			}
		}
		
		if(y0 > y1)
		{
			return new CaretLocation(x, y1, y0);
		}
		else
		{
			return new CaretLocation(x, y0, y1);
		}
	}


	public static PathElement[] translatePath(Region target, Region src, PathElement[] elements)
	{
		Insets pad = src.getPadding();
		Point2D p = src.localToScreen(pad.getLeft(), pad.getTop());
		if(p == null)
		{
			return null;
		}
		
		p = target.screenToLocal(p);
		double dx = p.getX();
		double dy = p.getY();
		
		for(int i=0; i<elements.length; i++)
		{
			PathElement em = elements[i];
			if(em instanceof LineTo)
			{
				LineTo m = (LineTo)em;
				em = new LineTo(m.getX() + dx, m.getY() + dy);
			}
			else if(em instanceof MoveTo)
			{
				MoveTo m = (MoveTo)em;
				em = new MoveTo(m.getX() + dx, m.getY() + dy);
			}
			else
			{
				throw new Error("can't handle " + em);
			}
			
			elements[i] = em;
		}
		return elements;
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
	
	
	public static boolean isCloseEnough(double a, double b)
	{
		// in case for some reason floating point computation result is slightly off
		return Math.abs(a - b) < 0.01;
	}


	/** debugging tool */
	public static String dump(PathElement[] elements)
	{
		if(elements == null)
		{
			return "null";
		}
		
		DecimalFormat f = new DecimalFormat("#0.#");
		boolean sp = false;

		SB sb = new SB();
		for(PathElement em: elements)
		{
			if(sp)
			{
				sb.sp();
			}
			else
			{
				sp = true;
			}
			
			if(em instanceof LineTo)
			{
				LineTo m = (LineTo)em;
				sb.a("L");
				sb.a(f.format(m.getX()));
				sb.comma();
				sb.a(f.format(m.getY()));
			}
			else if(em instanceof MoveTo)
			{
				MoveTo m = (MoveTo)em;
				sb.a("M");
				sb.a(f.format(m.getX()));
				sb.comma();
				sb.a(f.format(m.getY()));
			}
			else
			{
				sb.a(em);
			}
		}
		return sb.toString();
	}
}
