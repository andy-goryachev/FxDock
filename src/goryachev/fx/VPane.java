// Copyright © 2016-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.log.Log;
import goryachev.common.util.CKit;
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
 * PREF, FILL, percentage, or exact pixels.
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
	
	
	public void setGap(int gap)
	{
		this.gap = gap;
	}
	
	
	/** adds a node with preferred height constraint */
	public void add(Node n)
	{
		massage(n);
		getChildren().add(n);
	}
	
	
	/** adds a node at the specified position */
	public void add(int ix, Node n)
	{
		massage(n);
		getChildren().add(ix, n);
	}
	
	
	/** adds a node with the specified height constraint */
	public void add(Node n, double constraint)
	{
		massage(n);
		getChildren().add(n);
		FX.setProperty(n, KEY_CONSTRAINT, Double.valueOf(constraint));
	}
	
	
	/** adds an empty region with the FILL constraint */
	public void fill()
	{
		Region n = new Region();
		massage(n);
		getChildren().add(n);
		FX.setProperty(n, KEY_CONSTRAINT, FILL);
	}
	
	
	/** adds a node with the FILL constraint */
	public void fill(Node n)
	{
		massage(n);
		getChildren().add(n);
		FX.setProperty(n, KEY_CONSTRAINT, FILL);
	}
	
	
	/** adds a node with the FILL constraint at the specified position */
	public void fill(int ix, Node n)
	{
		massage(n);
		getChildren().add(ix, n);
		FX.setProperty(n, KEY_CONSTRAINT, FILL);
	}
	
	
	protected void massage(Node n)
	{
		// once in an VPane, surrender your limitations!
		if(n instanceof Region)
		{
			Region r = (Region)n;
			r.setMaxHeight(Double.MAX_VALUE);
			r.setMaxWidth(Double.MAX_VALUE);
		}
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
			Log.err(e);
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
	
	
	/** a shortcut to set padding on the panel */
	public void setPadding(double gap)
	{
		setPadding(new CInsets(gap));
	}
	
	
	/** a shortcut to set padding on the panel */
	public void setPadding(double ver, double hor)
	{
		setPadding(new CInsets(ver, hor));
	}
	
	
	/** a shortcut to set padding on the panel */
	public void setPadding(double top, double right, double bottom, double left)
	{
		setPadding(new CInsets(top, right, bottom, left));
	}


	public void remove(Node n)
	{
		getChildren().remove(n);
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
			top = CKit.round(m.getTop());
			bottom = CKit.round(m.getBottom());
			left = CKit.round(m.getLeft());
			right = CKit.round(m.getRight());
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
			int total = 0;
			
			for(int i=0; i<sz; i++)
			{
				Node n = nodes.get(i);
				double cc = getConstraint(n);
				int d;
				if(isFixed(cc))
				{
					d = CKit.ceil(cc);
				}
				else
				{
					if(preferred)
					{
						d = CKit.ceil(Math.max(n.prefHeight(-1), n.minHeight(-1)));
					}
					else
					{
						d = CKit.ceil(n.minHeight(-1));
					}
				}
				
				if(size != null)
				{
					size[i] = d;
				}
				
				total += d;
			}
			
			return total + top + bottom + gaps;
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
					d = CKit.ceil(n.prefWidth(height));
				}
				else
				{
					d = CKit.ceil(n.minWidth(height));				
				}
				if(d > max)
				{
					max = d;
				}
			}
			
			return max + left + right;
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
		
		protected void adjust(int delta)
		{
			// space available for FILL/PERCENT columns
			int available = delta;
			// ratio of columns with percentage explicitly set
			double percent = 0;
			// number of FILL columns
			int fillsCount = 0;
			
			for(int i=0; i<sz; i++)
			{
				Node n = nodes.get(i);
				double cc = getConstraint(n);
				if(isPercent(cc))
				{
					// percent
					percent += cc;
					available += size[i];
				}
				else if(isFill(cc))
				{
					// fill
					fillsCount++;
					available += size[i];
				}
			}
			
			if(available < 0)
			{
				available = 0;
			}
			
			double percentFactor = (percent > 1.0) ? (1 / percent) : percent;
			int remaining = available;
			
			// PERCENT sizes first
			for(int i=0; i<sz; i++)
			{
				Node n = nodes.get(i);
				double cc = getConstraint(n);
				if(isPercent(cc))
				{
					double w;
					if(remaining > 0)
					{
						w = cc * available * percentFactor;
					}
					else
					{
						w = 0;
					}
					
					int d = CKit.round(w);
					size[i] = d;
					remaining -= d;
				}
			}
			
			// FILL sizes after PERCENT
			if(fillsCount > 0)
			{
				double cw = remaining / (double)fillsCount;
				
				for(int i=0; i<sz; i++)
				{
					Node n = nodes.get(i);
					double cc = getConstraint(n);
					if(isFill(cc))
					{
						double w;
						if(remaining >= 0)
						{
							w = Math.min(cw, remaining);
						}
						else
						{
							w = 0;
						}
						
						int d = CKit.ceil(w);
						size[i] = d;
						remaining -= d;
					}
				}
			}
		}
		

		public void applySizes()
		{
			computePositions();
			
			int w = CKit.floor(getWidth() - left - right);
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
			int dh = CKit.floor(getHeight()) - ph;
			if(dh != 0)
			{
				adjust(dh);
			}

			applySizes();
		}
	}
}
