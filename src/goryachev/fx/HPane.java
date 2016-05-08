// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
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
//			Log.fail(e); // FIX
			e.printStackTrace();
		}
	}
	
	
	protected Helper h()
	{
		return new Helper(getManagedChildren(), getInsets());
	}
	
	
	protected int round(double x)
	{
		return (int)Math.round(x);
	}
	
	
	protected int ceil(double x)
	{
		return (int)Math.ceil(x);
	}
	
	
	protected int floor(double x)
	{
		return (int)Math.floor(x);
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
			top = round(m.getTop());
			bottom = round(m.getBottom());
			left = round(m.getLeft());
			right = round(m.getRight());
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
				int w;
				if(isFixed(cc))
				{
					w = ceil(cc);
				}
				else
				{
					if(preferred)
					{
						w = ceil(n.prefWidth(-1));
					}
					else
					{
						w = ceil(n.minWidth(-1));
					}
				}
				
				if(size != null)
				{
					size[i] = w;
				}
				
				sum += w;
			}
			
			return sum + left + right + gaps;
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
					d = ceil(n.prefHeight(width));
				}
				else
				{
					d = ceil(n.minHeight(width));				
				}
				if(d > max)
				{
					max = d;
				}
			}
			
			return max + top + bottom;
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
			
			double extra = (getWidth() - left - right - gaps - sum);
			double percentRatio = (totalPercent > 1.0) ? (1 / totalPercent) : 1.0;
			double fillRatio = (1.0 - totalPercent * percentRatio) / fills;
			
			// keeping a double cumulative value in addition to the integer one
			// in order to avoid accumulating rounding errors
			int isum = gaps + left + right;
			double dsum = isum;
			
			// compute sizes
			int last = sz - 1;
			for(int i=0; i<=last; i++)
			{
				Node n = nodes.get(i);
				double cc = getConstraint(n);
				int w0 = size[i];
				int iw;
				
				if(isFixed(cc))
				{
					iw = w0;
					dsum += iw;
				}
				else if(isFill(cc)) // || (w0 == 0)) // treat zero min/pref size as a fill
				{
					dsum += (extra * fillRatio);
					iw = round(dsum - isum);
				}
				else if(isPercent(cc))
				{
					dsum += (extra * percentRatio * cc);
					iw = round(dsum - isum);
				}
				else
				{
					dsum += w0;
					iw = w0;
				}
				
				if(i == last)
				{
					iw = floor(getWidth() - isum);
				}
				
				size[i] = iw;
				isum += iw;
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
			
			double extra = (getWidth() - left - right - gaps - sum);
			if(extra < 0)
			{
				// do not make it smaller than permitted by the minimum size
				extra = 0;
			}
			
			// keeping a double cumulative value in addition to the integer one
			// in order to avoid accumulating rounding errors
			int isum = gaps + left + right;
			double dsum = isum;
			
			// compute sizes
			int last = sz - 1;
			for(int i=0; i<=last; i++)
			{
				Node n = nodes.get(i);
				double cc = getConstraint(n);
				int w0 = size[i];
				int iw;
				
				if(isFixed(cc))
				{
					iw = ceil(cc);
					dsum += iw;
				}
				else
				{
					// TODO multiply by percent, fill, preferred ratios
					dsum += (w0 + extra / ct);
					iw = round(dsum - isum);
				}
				
				if(i == last)
				{
					iw = floor(getWidth() - isum);
				}
				
				size[i] = iw;
				isum += iw;
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
			
			int h = floor(getHeight() - top - bottom);
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
			double dw = getWidth() - pw;
			if(dw < 0)
			{
				// populate size[] array with minimum sizes
				computeSizes(false);
				
				// contract between min size and pref size
				contract();
			}
			else if(dw > 0)
			{
				expand();
			}

			applySizes();
		}
	}
}
