// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.util.Log;
import goryachev.common.util.Parsers;
import java.util.List;
import java.util.function.Function;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;


/**
 * Horizontally arranged Pane that lays out its child nodes using the following constraints:
 * PREF, FILL, percentage, exact pixels.
 */
public class HPane
	extends Pane
{
	public static final double FILL = -1.0;
	public static final double PREF = -2.0;
	protected int hgap;
	protected static final Object KEY_CONSTRAINT = new Object();
	
	
	public HPane(int hgap)
	{
		this.hgap = hgap;
	}
	
	
	public HPane()
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
		return h().computeWidth((nd) -> nd.prefWidth(height), false);
	}
	
	
	protected double computePrefHeight(double width)
	{
		return h().computeHeight((nd) -> nd.prefHeight(width));
	}


	protected double computeMinWidth(double height)
	{
		return h().computeWidth((nd) -> nd.minWidth(height), false);
	}
	
	
	protected double computeMinHeight(double width)
	{
		return h().computeHeight((nd) -> nd.minHeight(width));
	}
	
	
	protected void layoutChildren()
	{
		try
		{
			h().layout();
		}
		catch(Exception e)
		{
//			Log.fail(e); // FIX
			e.printStackTrace();
		}
	}
	
	
	protected Helper h()
	{
		return new Helper(getManagedChildren(), getInsets());
	}
	
	
	protected double snapsize(double x)
	{
		return snapSize(x);
	}
	
	
	protected double snapspace(double x)
	{
		return snapSpace(x);
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
		public double top;
		public double bottom;
		public double left;
		public double right;
		public double[] pos;
		public double[] size;


		public Helper(List<Node> nodes, Insets m)
		{
			this.nodes = nodes;
			this.sz = nodes.size();
			top = snapspace(m.getTop());
			bottom = snapspace(m.getBottom());
			left = snapspace(m.getLeft());
			right = snapspace(m.getRight());
		}
		
		
		protected double getConstraint(Node n)
		{
			Object x = FX.getProperty(n, KEY_CONSTRAINT);
			return Parsers.parseDouble(x, PREF);
		}

		
		protected double getFixedWidth(Node n)
		{
			double w = getConstraint(n);
			if(w > 1.0)
			{
				return w;
			}
			return -1;
		}
		
		
		protected boolean isPercent(double x)
		{
			return (x < 1.0) && (x >= 0);
		}
		
		
		protected boolean isFill(double x)
		{
			return (x == FILL);
		}
		
		
		protected double computeWidth(Function<Node,Double> sizingMethod, boolean inLayout)
		{
			if(inLayout)
			{
				size = new double[sz];
			}
			
			double total = 0;
			for(int i=0; i<sz; i++)
			{
				Node n = nodes.get(i);
				double w = getFixedWidth(n);
				if(w < 0)
				{
					w = snapsize(sizingMethod.apply(n));
				}
				
				if(inLayout)
				{
					size[i] = w;
				}
				total += w;
			}
			
			if(sz > 1)
			{
				total += (hgap * (sz - 1));
			}
			
			return total;
		}
		
		
		protected double computeHeight(Function<Node,Double> sizingMethod)
		{
			double max = 0;
			for(int i=0; i<sz; i++)
			{
				Node n = nodes.get(i);
				double d = snapsize(sizingMethod.apply(n));
				if(d > max)
				{
					max = d;
				}
			}
			
			return max + top + bottom;
		}
		
		
		/** size to allocate extrac space between FILL and PERCENT cells */
		protected void expand(double delta)
		{
			// space available for FILL/PERCENT columns
			double flex = delta;
			// ratio of columns with percentage explicitly set
			double percent = 0;
			// number of FILL columns
			int fillCount = 0;
			
			for(int i=0; i<sz; i++)
			{
				Node n = nodes.get(i);
				double cc = getConstraint(n);
				if(isPercent(cc))
				{
					// percent
					percent += cc;
					flex += size[i];
				}
				else if(isFill(cc))
				{
					// fill
					fillCount++;
					flex += size[i];
				}
			}
			
			// no overbooking
			if(flex < 0)
			{
				flex = 0;
			}
			
			double remaining = flex;
			
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
						w = snapsize(cc * flex);
						remaining -= w;
					}
					else
					{
						w = 0;
					}
					size[i] = w;
				}
			}
			
			// FILL sizes after PERCENT
			if(fillCount > 0)
			{
				double cw = remaining / fillCount;

				for(int i=0; i<sz; i++)
				{
					Node n = nodes.get(i);
					double cc = getConstraint(n);
					if(isFill(cc))
					{
						double w;
						if(remaining >= 0)
						{
							w = Math.min(cw, flex);
							remaining -= w;
						}
						else
						{
							w = 0;
						}
						
						size[i] = w;
					}
				}
			}
		}
		
		
		/** fixed size: keep; distribute space proportionally to their *minimum* size */
		protected void contract(double delta)
		{
			
		}
		
		
		/** fixed size: keep, squeeze the rest proportionally to their *minimum* size */
		protected void squeeze(double delta)
		{
			/*
			// space available for FILL/PERCENT columns
			int available = delta;
			// ratio of columns with percentage explicitly set
			float percent = 0;
			// number of FILL columns
			int fillCount = 0;
			
			int sz = specs.size();
			for(int i=0; i<sz; i++)
			{
				LC lc = specs.get(i);
				if(lc.isPercent())
				{
					// percent
					percent += lc.width;
					available += size[i];
				}
				else if(lc.isFill())
				{
					// fill
					fillCount++;
					available += size[i];
				}
			}
			
			// no overbooking
			if(available < 0)
			{
				available = 0;
			}
			
			int remaining = available;
			
			// PERCENT sizes first
			for(int i=0; i<sz; i++)
			{
				LC lc = specs.get(i);
				if(lc.isPercent())
				{
					int w;
					if(remaining > 0)
					{
						w = s(lc.width * available);
						remaining -= w;
					}
					else
					{
						w = 0;
					}
					size[i] = w;
				}
			}
			
			// FILL sizes after PERCENT
			if(fillCount > 0)
			{
				int cw = remaining / fillCount;
				
				for(int i=0; i<sz; i++)
				{
					LC lc = specs.get(i);
					if(lc.isFill())
					{
						int w;
						if(remaining >= 0)
						{
							w = Math.min(cw, available);
							remaining -= w;
						}
						else
						{
							w = 0;
						}
						
						size[i] = w;
					}
				}
			}
			
			// TODO may be change the order of assignments to avoid jitter in the first columns
			*/
		}
		
		
		protected void computePositions(double start, int gap)
		{
			pos = new double[sz + 1];
			
			pos[0] = start;
			
			for(int i=0; i<sz; i++)
			{
				start += (size[i] + gap);
				pos[i+1] = start;
			}
		}
		
		
		public void sizeComponents()
		{
			computePositions(left, hgap);
			
			double h = getHeight() - top - bottom;
			for(int i=0; i<sz; i++)
			{
				Node n = nodes.get(i);
				double x = pos[i];
				double w = size[i];
				
				setBounds(n, x, top, w, h);
			}
		}
		

		public void layout()
		{
			double pref = computeWidth((n) -> n.prefWidth(-1), true);
			double dw = getWidth() - pref;
			if(dw < 0)
			{
				double min = computeWidth((n) -> n.minWidth(-1), true);
				dw = getWidth() - min;
				if(dw < 0)
				{
					squeeze(dw);
				}
				else
				{
					contract(dw);
				}
			}
			else if(dw > 0)
			{
				expand(dw);
			}

			sizeComponents();
		}
	}
}
