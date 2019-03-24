// Copyright © 2017-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit.internal;
import goryachev.common.util.SB;
import goryachev.fx.FX;
import goryachev.fx.util.FxPathBuilder;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.PathElement;


/**
 * Selection Helper encapsulates the logic required to generate selection shapes.
 * 
 * The goal is to find out which shapes correspond to the top-most and bottom-most 
 * text rows (in the presence of wrapping).  These shapes (#) should be added to selection as is.
 * Any space in between (x) would generate a single rectangular block that fills the
 * width of the container.  Additional shapes (#) will be added when necessary to make
 * the selection appear contiguious.  These shapes are positioned to the left or to the right
 * of the selected text depending on the direction of text.
 * 
 * TODO RTL text
 * 
 *	 ----***--***####
 *	 xxxxxxxxxxxxxxxx
 *	 xxxxxxxxxxxxxxxx
 *	 ####**----------
 *
 * TODO this class can be static because everything happens in FX app thread.
 */
public class SelectionHelper
{
	private final FxPathBuilder pathBuilder;
	private final double left;
	private final double right;
	private double topUp = Double.POSITIVE_INFINITY;
	private double topDn = Double.POSITIVE_INFINITY;
	private double topLeft = Double.POSITIVE_INFINITY;
	private double topRight = Double.NEGATIVE_INFINITY;
	private double bottomUp = Double.NEGATIVE_INFINITY;
	private double bottomDn = Double.NEGATIVE_INFINITY;
	private double bottomLeft = Double.POSITIVE_INFINITY;
	private double bottomRight = Double.NEGATIVE_INFINITY;
	private static final double EPSILON = 0.001; // float point arithmetic is inexact

	
	public SelectionHelper(FxPathBuilder b, double left, double right)
	{
		this.pathBuilder = b;
		this.left = left;
		this.right = right;
	}
	
	
	public String toString()
	{
		return 
			"topUp=" + topUp +
			" topDn=" + topDn +
			" botUp=" + bottomUp +
			" botDn=" + bottomDn;
	}
	
	
	@FunctionalInterface
	protected interface PathHandler
	{
		public void processPoint(double x, double y);
	}
	
	
	protected void process(PathElement[] elements, PathHandler h)
	{
		for(PathElement em: elements)
		{
			if(em instanceof LineTo)
			{
				LineTo m = (LineTo)em;
				h.processPoint(m.getX(), m.getY());
			}
			else if(em instanceof MoveTo)
			{
				MoveTo m = (MoveTo)em;
				h.processPoint(m.getX(), m.getY());
			}
			else
			{
				throw new Error("?" + em);
			}
		}
	}


	protected void generateMiddle(boolean topLTR, boolean bottomLTR)
	{
		if(Double.isNaN(topUp))
		{
			return;
		}
		
		// only if the middle exists
		if(bottomUp > topDn)
		{
			if(topLTR)
			{
				pathBuilder.moveto(topRight, topUp);
				pathBuilder.lineto(right, topUp);
				pathBuilder.lineto(right, topDn);
				pathBuilder.lineto(topRight, topDn);
				pathBuilder.lineto(topRight, topUp);
			}
			else
			{
				// TODO
			}
			
			pathBuilder.moveto(left, topDn);
			pathBuilder.lineto(right, topDn);
			pathBuilder.lineto(right, bottomUp);
			pathBuilder.lineto(left, bottomUp);
			pathBuilder.lineto(left, topDn);
			
			// trailer
			
			if(bottomLTR)
			{
				pathBuilder.moveto(left, bottomUp);
				pathBuilder.lineto(bottomLeft, bottomUp);
				pathBuilder.lineto(bottomLeft, bottomDn);
				pathBuilder.lineto(left, bottomDn);
				pathBuilder.lineto(left, bottomUp);
			}
			else
			{
				// TODO
			}
		}
	}


	@Deprecated // debugging
	private static int r(double x)
	{
		return FX.round(x);
	}
	
	
	@Deprecated // debugging
	private static String dump(PathElement[] elements)
	{
		SB sb = new SB();
		if(elements == null)
		{
			sb.append("null");
		}
		else
		{
			for(PathElement em: elements)
			{
				if(em instanceof MoveTo)
				{
					MoveTo m = (MoveTo)em;
					sb.a("M");
					sb.a(r(m.getX()));
					sb.a(",");
					sb.a(r(m.getY()));
					sb.a(" ");
				}
				else if(em instanceof LineTo)
				{
					LineTo m = (LineTo)em;
					sb.a("L");
					sb.a(r(m.getX()));
					sb.a(",");
					sb.a(r(m.getY()));
					sb.a(" ");
				}
			}
		}
		return sb.toString();
	}
	
	
	protected boolean isNear(double a, double b)
	{
		return Math.abs(a - b) < EPSILON;
	}
	
	
	protected void determineTopYLimits(double x, double y)
	{
		if(y < topUp)
		{
			topUp = y;
		}
	}
	
	
	protected void determineTopXLimits(double x, double y)
	{
		if(isNear(y, topUp))
		{			
			if(x < topLeft)
			{
				topLeft = x;
			}
			
			if(x > topRight)
			{
				topRight = x;
			}
		}
		else
		{
			if(y < topDn)
			{
				topDn = y;
			}	
		}
	}
	
	
	protected void determineBottomYLimits(double x, double y)
	{
		if(y > bottomDn)
		{
			bottomDn = y;
		}
	}
	
	
	protected void determineBottomXLimits(double x, double y)
	{
		if(isNear(y, bottomDn))
		{
			if(x < bottomLeft)
			{
				bottomLeft = x;
			}
			
			if(x > bottomRight)
			{
				bottomRight = x;
			}
		}
		else
		{
			if(y > bottomUp)
			{
				bottomUp = y;
			}
		}
	}


	public void generate(PathElement[] top, PathElement[] bottom, boolean topLTR, boolean bottomLTR)
	{		
		if(bottom == null)
		{
			// TODO special handling when outside of visible area
			pathBuilder.addAll(top);
		}
		else
		{
			process(top, this::determineTopYLimits);
			process(top, this::determineTopXLimits);

			process(bottom, this::determineBottomYLimits);
			process(bottom, this::determineBottomXLimits);
			
//			D.print("top", dump(top), "bottom", dump(bottom)); // FIX
//			D.print(" top: y=" + r(topUp) + ".." + r(topDn) + " x=" + r(topLeft) + ".." +  r(topRight));
//			D.print(" bot: y=" + r(bottomUp) + ".." + r(bottomDn) + " x=" + r(bottomLeft) + ".." + r(bottomRight));
			
			pathBuilder.addAll(top);
			generateMiddle(topLTR, bottomLTR);
			pathBuilder.addAll(bottom);
		}
	}
}
