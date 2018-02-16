// Copyright © 2017-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.edit.internal;
import goryachev.fx.util.FxPathBuilder;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.PathElement;


/**
 * Selection Helper encapsulates the logic required to generate selection shapes.
 * 
 * The goal is to find out which shapes correspond to the top-most and bottom-most 
 * text rows (in the presence of wrapping).  These shapes should be added to selection as is.
 * The (possible) space in between would generate a single rectangular block that fills the
 * width of the container:
 * 
 *	 ----***--***----
 *	 ****************
 *	 ****************
 *	 ----**----------
 * 
 * This algorithm has a minor drawback where the top and bottom row selection may not go
 * to the left and right side of the container.  This is because I am too lazy to account 
 * for RTL/LTR text and wrapping.
 */
public class SelectionHelper
{
	private final FxPathBuilder pathBuilder;
	private final double left;
	private final double right;
	private double topUp = Double.NaN;
	private double topDn = Double.NaN;
	private double botUp = Double.NaN;
	private double botDn = Double.NaN;

	
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
			" botUp=" + botUp +
			" botDn=" + botDn;
	}


	public void process(PathElement[] elements)
	{
		for(PathElement em: elements)
		{
			double y = getY(em);
			setTop(y);
			setBottom(y);
		}
	}


	public void generateTop(PathElement[] elements)
	{
		boolean include = false;
		double y;
		for(PathElement em: elements)
		{
			if(em instanceof LineTo)
			{
				if(include)
				{
					pathBuilder.add(em);
				}
			}
			else if(em instanceof MoveTo)
			{
				y = getY(em);
				if((y == topUp) || (y == topDn))
				{
					pathBuilder.add(em);
					include = true;
				}
				else
				{
					include = false;
				}
			}
		}
	}


	public void generateMiddle()
	{
		if(Double.isNaN(topUp))
		{
			return;
		}
		
		if(botUp > topDn)
		{
			// only if the middle exists
			pathBuilder.moveto(left, topDn);
			pathBuilder.lineto(right, topDn);
			pathBuilder.lineto(right, botUp);
			pathBuilder.lineto(left, botUp);
			pathBuilder.lineto(left, topDn);
		}
	}


	public void generateBottom(PathElement[] elements)
	{
		boolean include = false;
		double y;
		for(PathElement em: elements)
		{
			if(em instanceof LineTo)
			{
				if(include)
				{
					pathBuilder.add(em);
				}
			}
			else if(em instanceof MoveTo)
			{
				y = getY(em);
				if((y == botUp) || (y == botDn))
				{
					pathBuilder.add(em);
					include = true;
				}
				else
				{
					include = false;
				}
			}
		}
	}
	
	
	protected double getY(PathElement em)
	{
		if(em instanceof LineTo)
		{
			LineTo m = (LineTo)em;
			return m.getY();
		}
		else if(em instanceof MoveTo)
		{
			MoveTo m = (MoveTo)em;
			return m.getY();
		}
		else
		{
			throw new Error("?" + em);
		}
	}
	
	
	protected void setTop(double y)
	{
		if(isSmaller(y, topUp))
		{
			if(isSmaller(topUp, topDn))
			{
				topDn = topUp;
			}
			topUp = y;
		}
		else if(isSmaller(y, topDn) && (y > topUp))
		{
			topDn = y;
		}
	}
	

	protected void setBottom(double y)
	{
		if(isLarger(y, botDn))
		{
			if(isLarger(botDn, botUp))
			{
				botUp = botDn;
			}
			botDn = y;
		}
		else if(isLarger(y, botUp) && (y < botDn))
		{
			botUp = y;
		}
	}
	

	protected boolean isSmaller(double y, double current)
	{
		if(Double.isNaN(y))
		{
			return false;
		}
		else if(Double.isNaN(current))
		{
			return true;
		}
		else if(y < current)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	
	protected boolean isLarger(double y, double current)
	{
		if(Double.isNaN(y))
		{
			return false;
		}
		else if(Double.isNaN(current))
		{
			return true;
		}
		else if(y > current)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
