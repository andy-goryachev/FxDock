// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.util.Log;
import goryachev.common.util.Parsers;
import java.util.List;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;


/**
 * Vertically arranged Pane that lays out its child nodes using the following constraints:
 * PREF, FILL, percentage, exact pixels.
 */
public class VPane
	extends Pane
{
	public static final double FILL = -1.0;
	public static final double PREF = -2.0;
	protected int gap;
	protected static final Object KEY_CONSTRAINT = new Object();
	
	
	public VPane(int hgap)
	{
		this.gap = hgap;
	}
	
	
	public VPane()
	{
	}
	
	
	public void add(Node n)
	{
		getChildren().add(n);
	}
	
	
	public void fill(Node n)
	{
		getChildren().add(n);
		FX.setProperty(n, KEY_CONSTRAINT, FILL);
	}
	
	
	public void fill()
	{
		Region n = new Region();
		getChildren().add(n);
		FX.setProperty(n, KEY_CONSTRAINT, FILL);
	}
	
	
	public void add(Node n, double constraint)
	{
		getChildren().add(n);
		FX.setProperty(n, KEY_CONSTRAINT, Double.valueOf(constraint));
	}

	
	protected double computePrefWidth(double height)
	{
		return h().computeWidth(height, true);	
	}
	

	protected double computeMinWidth(double height)
	{
		return h().computeWidth(height, false);
	}
	
	
	protected double computePrefHeight(double width)
	{
		return h().computeSizes(true);
	}

	
	protected double computeMinHeight(double width)
	{
		return h().computeSizes(false);
	}
	
	
	protected void layoutChildren()
	{
		try
		{
			h().layout();
		}
		catch(Exception e)
		{
			Log.fail(e);
		}
	}
	
	
	protected Helper h()
	{
		return new Helper(getManagedChildren(), getInsets());
	}
	
	
	protected void setBounds(Node nd, double left, double top, double width, double height)
	{
		layoutInArea(nd, left, top, width, height, 0, HPos.CENTER, VPos.CENTER);
	}
	
	
	//
	
	
	public class Helper
	{
		public final List<Node> nodes;
		public final int sz;
		public final int gaps;
		public int top;
		public int bottom;
		public int left;
		public int right;
		public int[] size;
		public int[] pos;


		public Helper(List<Node> nodes, Insets m)
		{
			this.nodes = nodes;
			this.sz = nodes.size();
			top = FX.round(m.getTop());
			bottom = FX.round(m.getBottom());
			left = FX.round(m.getLeft());
			right = FX.round(m.getRight());
			gaps = (sz < 2) ? 0 : (gap * (sz - 1));
		}
		
		
		protected double getConstraint(Node n)
		{
			Object x = FX.getProperty(n, KEY_CONSTRAINT);
			return Parsers.parseDouble(x, PREF);
		}

		
		protected boolean isFixed(double x)
		{
			return (x > 1.0);
		}
		
		
		protected boolean isPercent(double x)
		{
			return (x < 1.0) && (x >= 0.0);
		}
		
		
		protected boolean isFill(double x)
		{
			return (x == FILL);
		}
		
		
		protected int computeSizes(boolean preferred)
		{
			int sum = 0;
			for(int i=0; i<sz; i++)
			{
				Node n = nodes.get(i);
				double cc = getConstraint(n);
				int d;
				if(isFixed(cc))
				{
					d = FX.ceil(cc);
				}
				else
				{
					if(preferred)
					{
						d = FX.ceil(n.prefHeight(-1));
					}
					else
					{
						d = FX.ceil(n.minHeight(-1));
					}
				}
				
				if(size != null)
				{
					size[i] = d;
				}
				
				sum += d;
			}
			
			return sum + top + bottom + gaps;
		}
		
		
		protected double computeWidth(double height, boolean preferred)
		{
			int max = 0;
			for(int i=0; i<sz; i++)
			{
				Node n = nodes.get(i);
				int d;
				if(preferred)
				{
					d = FX.ceil(n.prefWidth(height));
				}
				else
				{
					d = FX.ceil(n.minWidth(height));				
				}
				if(d > max)
				{
					max = d;
				}
			}
			
			return max + left + right;
		}
		
		
		/** size to allocate extra space between FILL and PERCENT cells */
		protected void expand()
		{
			int sum = 0;
			double totalPercent = 0;
			int fills = 0;
			
			for(int i=0; i<sz; i++)
			{
				Node n = nodes.get(i);
				double cc = getConstraint(n);
				if(isPercent(cc))
				{
					totalPercent += cc;
				}
				else if(isFill(cc))
				{
					fills++;
				}
				else
				{
					sum += size[i];
				}
			}
			
			double extra = (getHeight() - top - bottom - gaps - sum);
			double percentRatio = (totalPercent > 1.0) ? (1 / totalPercent) : 1.0;
			double fillRatio = (1.0 - totalPercent * percentRatio) / fills;
			
			// keeping a double cumulative value in addition to the integer one
			// in order to avoid accumulating rounding errors
			int isum = gaps + top + bottom;
			double dsum = isum;
			
			// compute sizes
			int last = sz - 1;
			for(int i=0; i<=last; i++)
			{
				Node n = nodes.get(i);
				double cc = getConstraint(n);
				int d = size[i];
				int di;
				
				if(isFixed(cc))
				{
					di = d;
					dsum += di;
				}
				else if(isFill(cc))
				{
					dsum += (extra * fillRatio);
					di = FX.round(dsum - isum);
				}
				else if(isPercent(cc))
				{
					dsum += (extra * percentRatio * cc);
					di = FX.round(dsum - isum);
				}
				else
				{
					dsum += d;
					di = d;
				}
				
				if(i == last)
				{
					di = FX.floor(getHeight() - isum);
				}
				
				size[i] = di;
				isum += di;
			}
		}
		
		
		/** keep minimum sizes, redistributing extra space between PERCENT and FILL components */
		protected void contract()
		{
			int ct = 0;
			int sum = 0;
			
			for(int i=0; i<sz; i++)
			{
				Node n = nodes.get(i);
				double cc = getConstraint(n);
				if(!isFixed(cc))
				{
					ct++;
				}
				
				sum += size[i];
			}
			
			double extra = (getHeight() - top - bottom - gaps - sum);
			if(extra < 0)
			{
				// do not make it smaller than permitted by the minimum size
				extra = 0;
			}
			
			// keeping a double cumulative value in addition to the integer one
			// in order to avoid accumulating rounding errors
			int isum = gaps + top + bottom;
			double dsum = isum;
			
			// compute sizes
			int last = sz - 1;
			for(int i=0; i<=last; i++)
			{
				Node n = nodes.get(i);
				double cc = getConstraint(n);
				int d0 = size[i];
				int di;
				
				if(isFixed(cc))
				{
					di = FX.ceil(cc);
					dsum += di;
				}
				else
				{
					// TODO multiply by percent, fill, preferred ratios
					dsum += (d0 + extra / ct);
					di = FX.round(dsum - isum);
				}
				
				if(i == last)
				{
					di = FX.floor(getHeight() - isum);
				}
				
				size[i] = di;
				isum += di;
			}
		}
		
		
		protected void computePositions()
		{
			int start = left;
			pos = new int[sz + 1];
			pos[0] = start;
			
			for(int i=0; i<sz; i++)
			{
				start += (size[i] + gap);
				pos[i+1] = start;
			}
		}
		
		
		public void applySizes()
		{
			computePositions();
			
			int w = FX.floor(getWidth() - left - right);
			for(int i=0; i<sz; i++)
			{
				Node n = nodes.get(i);
				int y = pos[i];
				int h = size[i];
				
				setBounds(n, left, y, w, h);
			}
		}
		

		public void layout()
		{
			size = new int[sz];
			
			// populate size[] with preferred sizes
			int ph = computeSizes(true);
			double dh = getHeight() - ph;
			if(dh < 0)
			{
				// populate size[] array with minimum sizes
				computeSizes(false);
				
				// contract between min size and pref size
				contract();
			}
			else if(dh > 0)
			{
				expand();
			}

			applySizes();
		}
	}
}
