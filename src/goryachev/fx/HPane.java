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
 * Horizontally arranged Pane that lays out its child nodes using the following constraints:
 * PREF, FILL, percentage, or exact pixels.
 */
public class HPane
	extends Pane
{
	protected static final Log log = Log.get("HPane");
	public static final double FILL = -1.0;
	public static final double PREF = -2.0;
	protected int gap;
	protected static final Object KEY_CONSTRAINT = new Object();
	
	
	public HPane(int hgap)
	{
		this.gap = hgap;
	}
	
	
	public HPane()
	{
	}
	
	
	public HPane(Node ... nodes)
	{
		for(Node n: nodes)
		{
			add(n);
		}
	}
	
	
	public HPane(int gap, Node ... nodes)
	{
		this.gap = gap;
		
		for(Node n: nodes)
		{
			add(n);
		}
	}
	
	
	public void setGap(int gap)
	{
		this.gap = gap;
	}
	
	
	public void space()
	{
		space(10);
	}
	
	
	public void space(int width)
	{
		Pane p = new Pane();
		p.setPrefWidth(width);
		add(p);
	}
	
	
	/** adds a node with preferred width constraint */
	public void add(Node n)
	{
		massage(n);
		getChildren().add(n);
	}
	
	
	public void addAll(Node ... nodes)
	{
		for(Node n: nodes)
		{
			add(n);
		}
	}
	
	
	/** adds a node at the specified position */
	public void add(int ix, Node n)
	{
		massage(n);
		getChildren().add(ix, n);
	}
	
	
	/** adds a node with the specified width constraint */
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
		// once in an HPane, surrender your limitations!
		if(n instanceof Region)
		{
			Region r = (Region)n;
			r.setMaxHeight(Double.MAX_VALUE);
			r.setMaxWidth(Double.MAX_VALUE);
		}
	}

	
	protected double computePrefWidth(double height)
	{
		return h().computeSizes(true);
	}
	

	protected double computeMinWidth(double height)
	{
		return h().computeSizes(false);
	}
	
	
	protected double computePrefHeight(double width)
	{
		return h().computeHeight(width, true);
	}

	
	protected double computeMinHeight(double width)
	{
		return h().computeHeight(width, false);
	}
	
	
	protected void layoutChildren()
	{
		try
		{
			h().layout();
		}
		catch(Exception e)
		{
			log.error(e);
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
		setPadding(FX.insets(gap));
	}
	
	
	/** a shortcut to set padding on the panel */
	public void setPadding(double ver, double hor)
	{
		setPadding(FX.insets(ver, hor));
	}
	
	
	/** a shortcut to set padding on the panel */
	public void setPadding(double top, double right, double bottom, double left)
	{
		setPadding(FX.insets(top, right, bottom, left));
	}


	public void remove(Node n)
	{
		getChildren().remove(n);
	}
	
	
	public void clear()
	{
		getChildren().clear();
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
						d = CKit.ceil(Math.max(n.prefWidth(-1), n.minWidth(-1)));
					}
					else
					{
						d = CKit.ceil(n.minWidth(-1));
					}
				}
				
				if(size != null)
				{
					size[i] = d;
				}
				
				total += d;
			}
			
			return total + left + right + gaps;
		}
		
		
		protected double computeHeight(double width, boolean preferred)
		{
			int max = 0;
			for(int i=0; i<sz; i++)
			{
				Node n = nodes.get(i);
				int d;
				if(preferred)
				{
					d = CKit.ceil(n.prefHeight(width));
				}
				else
				{
					d = CKit.ceil(n.minHeight(width));				
				}
				if(d > max)
				{
					max = d;
				}
			}
			
			return max + top + bottom;
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
		
		
		// FIX minimum size for percent and fills? or clip?
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
			
			int h = CKit.floor(getHeight() - top - bottom);
			for(int i=0; i<sz; i++)
			{
				Node n = nodes.get(i);
				int x = pos[i];
				int w = size[i];
				
				setBounds(n, x, top, w, h);
			}
		}
		

		public void layout()
		{
			size = new int[sz];
			
			// populate size[] with preferred sizes
			int pw = computeSizes(true);
			int dw = CKit.floor(getWidth()) - pw;
			if(dw != 0)
			{
				adjust(dw);
			}

			applySizes();
		}
	}
}
