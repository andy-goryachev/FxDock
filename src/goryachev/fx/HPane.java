// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.util.D;
import goryachev.common.util.Parsers;
import java.util.Arrays;
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
	protected int gap;
	protected static final Object KEY_CONSTRAINT = new Object();
	
	
	public HPane(int hgap)
	{
		this.gap = hgap;
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
		return h().computeSizes((nd) -> nd.prefWidth(height));
	}
	
	
	protected double computePrefHeight(double width)
	{
		return h().computeHeight((nd) -> nd.prefHeight(width));
	}


	protected double computeMinWidth(double height)
	{
		return h().computeSizes((nd) -> nd.minWidth(height));
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
		public double[] size;
		public double[] pref;
		public double[] pos;


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

		
		protected boolean isFixed(double x)
		{
			return (x > 1.0);
		}
		
		
		protected boolean isPercent(double x)
		{
			return (x < 1.0) && (x >= 0);
		}
		
		
		protected boolean isFill(double x)
		{
			return (x == FILL);
		}
		
		
		protected double computeSizes(Function<Node,Double> sizingMethod)
		{
			double total = 0;
			for(int i=0; i<sz; i++)
			{
				Node n = nodes.get(i);
				double w = getConstraint(n);
				if(!isFixed(w))
				{
					w = snapsize(sizingMethod.apply(n));
				}
				
				if(size != null)
				{
					size[i] = w;
				}
				
				total += w;
			}
			
			if(sz > 1)
			{
				total += (gap * (sz - 1));
			}
			
			return total + left + right;
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
		
		
		/** size to allocate extra space between FILL and PERCENT cells */
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
		
		
		/** keep minimum sizes, redistributing extra space between PERCENT and FILL components */
		protected void contract()
		{
			double minSizes = 0;
			int totalPercent = 0;
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
				
				minSizes += size[i];
			}
			
			double w = getWidth();
			double factor = (w - left - right - minSizes) / w;
			
			double percentRatio = 1.0;
			double fillRatio;
			if(factor < 0)
			{
				fillRatio = 0;
			}
			else
			{
				if(totalPercent > 1.0)
				{
					percentRatio = 1.0 / totalPercent;
				}
				
				fillRatio = (1.0 - percentRatio) / fills;
			}
			
			D.print("factor", factor);
			
			// compute sizes
			for(int i=0; i<sz; i++)
			{
				Node n = nodes.get(i);
				double cc = getConstraint(n);
				if(isFixed(cc))
				{
					size[i] = cc;
				}
				else if(isPercent(cc))
				{
					double w0 = size[i];
					size[i] = w0 + (pref[i] - w0) * percentRatio * factor;
				}
				else if(isFill(cc))
				{
					double w0 = size[i];
					size[i] = w0 + (pref[i] - w0) * fillRatio * factor;
				}
				else
				{
					double w0 = size[i];
					size[i] = w0 + (pref[i] - w0) * factor;
				}
			}
		}
		
		
		protected void computePositions()
		{
			double start = left;
			pos = new double[sz + 1];
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
			
			double h = getHeight() - top - bottom;
			for(int i=0; i<sz; i++)
			{
				Node n = nodes.get(i);
				double x = pos[i];
				double w = size[i];
				
				setBounds(n, x, top, w, h);
			}
			
			D.list(size);
		}
		

		public void layout()
		{
			size = new double[sz];
			
			// populate size[] with preferred sizes
			double pw = computeSizes((n) -> n.prefWidth(-1));
			double dw = getWidth() - pw;
			if(dw < 0)
			{
				// save preferred sizes
				pref = size;
				size = new double[sz];
				
				// populate size[] array with minimum sizes
				computeSizes((n) -> n.minWidth(-1));
				contract();
			}
			else if(dw > 0)
			{
				expand(dw);
			}

			applySizes();
		}
	}
}
