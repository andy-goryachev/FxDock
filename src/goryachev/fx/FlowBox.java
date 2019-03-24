// Copyright © 2019 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.util.D;
import java.util.List;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;


/**
 * Flow Box Pane arranges its children in a horizontal flow,
 * forcing equal height. 
 */
public class FlowBox
	extends Pane
{
	// TODO css properties
	protected int vgap;
	protected int hgap;
	
	
	public FlowBox()
	{
	}
	
	
	public void setVGap(int x)
	{
		vgap = x;
	}
	
	
	public void setHGap(int x)
	{
		hgap = x;
	}
	
	
	protected double computePrefHeight(double forWidth)
	{
		return new Helper().computePrefHeight(forWidth);
	}
	
	
	protected double computePrefWidth(double forHeight)
	{
		return -1;
	}

	
	protected void layoutChildren()
	{
		new Helper().layoutChildren();
	}
	
	
	protected void layoutInArea(Node n, double x, double y, double w, double h)
	{
		layoutInArea(n, x, y, w, h, 0.0, null, HPos.CENTER, VPos.CENTER);
	}
	
	
	//
	
	
	protected class Helper
	{
		protected final List<Node> children;
		protected final int sz;
		protected final double top;
		protected final double bottom;
		protected final double left;
		protected final double right;
		protected final double lineHeight;
		protected final double[] widths;
		
		
		public Helper()
		{
			Insets m = getInsets();
			top = m.getTop();
			bottom = m.getBottom();
			left = m.getLeft();
			right = m.getRight();
			double lh = 0.0;
			
			children = getChildren();
			sz = children.size();
			widths = new double[sz];
			
			for(int i=0; i<sz; i++)
			{
				Node ch = children.get(i);
				if(ch.isManaged())
				{
					double w = ch.prefWidth(-1);
					widths[i] = w;
					
					double h = ch.prefHeight(-1);
					if(h > lh)
					{
						lh = h;
					}
				}
			}
			
			lineHeight = lh;
		}
		
		
		public double computePrefHeight(double forWidth)
		{
			if(forWidth < 0)
			{
				return -1;
			}
			
			double max = forWidth - right;
			double x = left;
			double y = top;
			boolean addRow = false;
			
			for(int i=0; i<sz; i++)
			{
				Node ch = children.get(i);
				if(ch.isManaged())
				{
					addRow = true;
					
					double w = widths[i];
					if((x + w) >= max)
					{
						x = left;
						y += (vgap + lineHeight);
					}
					
					x += (hgap + w);
				}
			}
			
			if(addRow)
			{
				y += lineHeight;
			}
			
			return y + bottom;
		}
		
		
		public void layoutChildren()
		{
			double x = left;
			double y = top;
			int col = 0;
			double max = getWidth() - right;
			
			for(int i=0; i<sz; i++)
			{
				Node ch = children.get(i);
				if(ch.isManaged())
				{
					double w = widths[i];
					if((x + w) >= max)
					{
						x = left;
						y += (vgap + lineHeight);
					}
					
					layoutInArea(ch, x, y, w, lineHeight);
					
					x += (hgap + w);
				}
			}
		}
	}
}
