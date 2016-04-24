// Copyright (c) 2015-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.util.CList;
import goryachev.common.util.Log;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;


/** 
 * Same logic as CTableLayout3, except all components are lined up in one row or column.
 * 
 * Column spec:
 *   float 0.0 ... 1.0: preferred size as a fraction of total size
 *   float > 1.0 fixed size in pixels
 *   PREFERRED, FILL
 *   
 *   TODO SPEC(minw [pixels,%,dlu], prefw, maxw, group)
 */
public class LinearLayout
	implements LayoutManager2
{
	//public static final float MINIMUM = -1.0f;
	public static final float PREFERRED = -2.0f;
	public static final float FILL = -3.0f;
	
	protected final boolean horizontal;
	protected int gap;
	protected CList<Entry> cells = new CList();


	public LinearLayout(boolean horizontal)
	{
		this.horizontal = horizontal;
	}
	
	
	public void setGap(int gap)
	{
		this.gap = gap;
	}


	public int getGap()
	{
		return gap;
	}


	/** returns number of cells */
	public int getCellCount()
	{
		return cells.size();
	}
	
	
	public void addCell(float spec)
	{
		cells.add(new Entry(spec));
	}

	
	public Entry getCellSpec(int col)
	{
		while(getCellCount() <= col)
		{
			addCell(PREFERRED);
		}
		return cells.get(col);
	}
	
	
	public void setCellSpec(int col, float spec)
	{
		Entry c = getCellSpec(col);
		c.spec = spec;
	}
	
	
	public void setCellMinimumSize(int col, int size)
	{
		Entry c = getCellSpec(col);
		c.min = size;
	}
	
	
	public void setCellMaximumSize(int col, int size)
	{
		Entry c = getCellSpec(col);
		c.max = size;
	}
	

	public void addLayoutComponent(String name, Component comp)
	{
		throw new UnsupportedOperationException();
	}
	

	public void addLayoutComponent(Component comp, Object constraints)
	{
		synchronized(comp.getTreeLock())
		{
			Entry en = new Entry();
			en.component = comp;
			
			if(constraints instanceof Number)
			{
				en.spec = ((Number)constraints).floatValue();
			}
			else
			{
				en.spec = PREFERRED;
			}

			cells.add(en);
		}
	}


	public void removeLayoutComponent(Component comp)
	{
		synchronized(comp.getTreeLock())
		{
			for(int i=cells.size()-1; i>=0; i--)
			{
				Entry en = cells.get(i);
				if(en.component == comp)
				{
					cells.remove(i);
					break;
				}
			}
		}
	}


	public float getLayoutAlignmentX(Container target)
	{
		return 0.5f;
	}


	public float getLayoutAlignmentY(Container target)
	{
		return 0.5f;
	}


	public void invalidateLayout(Container target)
	{
	}


	public Dimension maximumLayoutSize(Container target)
	{
		return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}


	public Dimension preferredLayoutSize(Container parent)
	{
		synchronized(parent.getTreeLock())
		{
			Helper h = new Helper(parent);
			return h.computePreferredSize();
		}
	}


	public Dimension minimumLayoutSize(Container parent)
	{
		synchronized(parent.getTreeLock())
		{
			Helper h = new Helper(parent);
			return h.computeMinimumSize();
		}
	}
	
	
	public void layoutContainer(Container parent)
	{
		synchronized(parent.getTreeLock())
		{
			try
			{
				Helper h = new Helper(parent);
				h.layout();
			}
			catch(Exception e)
			{
				Log.err(e);
			}
		}
	}


	//

	
	/** layout axis contstraints */
	public static class Entry
	{
		public Component component;
		public float spec; // [0..1[ : percent, >=1 in pixels, <0 special
		public int min;
		public int max;
		public int group;
		
		
		public Entry()
		{
			spec = PREFERRED;
		}
		
		
		public Entry(float width)
		{
			this.spec = width;
		}
		
		
		public boolean isPercent()
		{
			return (spec >= 0.0f) && (spec < 1.0f);
		}
		

		public boolean isFill()
		{
			return (spec == FILL);
		}


		public boolean isScaled()
		{
			return isFill() || isPercent();
		}
	}
	
	
	//
	
	
	/** 
	 * axis helper.  
	 * even though the comments talk about columns, the logic is used for both columns and rows:
	 * just replace whe words columns/width with rows/heights
	 */
	public final class Axis
	{
		public int[] size;
		public int[] pos;
		
		
		public Axis()
		{
			size = new int[cells.size()];
		}
		
		
		protected void computePositions(int start, int gap)
		{
			int sz = size.length;
			pos = new int[sz + 1];
			
			pos[0] = start;
			
			for(int i=0; i<sz; i++)
			{
				start += (size[i] + gap);
				pos[i+1] = start;
			}
		}
		
		
		// minimum width if set, 0 otherwise
		protected int min(int ix)
		{
			return cells.get(ix).min;
		}
		
		
		// fixed width if set, 0 otherwise
		protected int fixed(int ix)
		{
			float w = cells.get(ix).spec;
			if(w >= 1.0f)
			{
				return (int)w;
			}
			return 0;
		}
		
		
		// max width of the column
		protected int max(int ix)
		{
			int max = cells.get(ix).max;
			if(max > 0)
			{
				return max;
			}
			return Integer.MAX_VALUE;
		}
		
		
		// true if component spans scaled column
		protected boolean isScaled(int ix)
		{
			return cells.get(ix).isScaled();
		}
		
		
		protected Dimension computeSizes(boolean preferred, boolean doingLayout)
		{
			// total width
			int total = 0;
			int other = 0;
			
			// scan cells
			int sz = cells.size();
			for(int i=0; i<sz; i++)
			{
				Entry en = cells.get(i);	
				Dimension d = null;
				
				Component c = en.component;
				if(c != null)
				{
					d = preferred ? c.getPreferredSize() : c.getMinimumSize();
					
					int or = horizontal ? d.height : d.width;
					if(or > other)
					{
						other = or;
					}
				}

				int w = fixed(i);
				if(w == 0)
				{
					if(d != null)
					{
						// layout does not need preferred sizes of components in scaled cells
						boolean skip = doingLayout && isScaled(i);
						if(!skip)
						{
							// amount of space component occupies in this column
							int cw = horizontal ? d.width : d.height;
							if(cw > w)
							{
								w = cw;
							}
							
							int mx = max(i);
							if(w > mx)
							{
								w = mx;
								break;
							}
						}
					}
				}
				
				size[i] = w;
				
				total += w;
			}
			
			
			if(sz > 1)
			{
				total += (gap * (sz - 1));
			}
			
			if(horizontal)
			{
				return new Dimension(total, other);
			}
			else
			{
				return new Dimension(other, total);
			}
		}
		
		
		protected void adjust(int delta)
		{
			// space available for FILL/PERCENT columns
			int available = delta;
			// ratio of columns with percentage explicitly set
			float percent = 0;
			// number of FILL columns
			int fillCount = 0;
			
			int sz = cells.size();
			for(int i=0; i<sz; i++)
			{
				Entry en = cells.get(i);
				if(en.isPercent())
				{
					// percent
					percent += en.spec;
					available += size[i];
				}
				else if(en.isFill())
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
				Entry en = cells.get(i);
				if(en.isPercent())
				{
					int w;
					if(remaining > 0)
					{
						w = Math.round(en.spec * available);
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
					Entry en = cells.get(i);
					if(en.isFill())
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
		}
	}
	
	
	//
	
	
	public class Helper
	{
		public final Container parent;
		private final boolean ltr;
		private int left;
		private int right;
		private int top;
		private int bottom;
		private int mright;
		private int mbottom;


		public Helper(Container parent)
		{
			this.parent = parent;
			
			ltr = parent.getComponentOrientation().isLeftToRight();
				
			Insets m = parent.getInsets();
			
			mright = m.right;
			mbottom = m.bottom;
			
			left = m.left;
			right = parent.getWidth() - mright;
			top = m.top;
			bottom = parent.getHeight() - mbottom;
		}
		
		
		public Dimension computePreferredSize()
		{
			Axis a = new Axis();
			Dimension d = a.computeSizes(true, false);
			return addInsets(d);
		}
		
		
		public Dimension computeMinimumSize()
		{
			Axis a = new Axis();
			Dimension d =  a.computeSizes(false, false);
			return addInsets(d);
		}
		
		
		protected Dimension addInsets(Dimension d)
		{
			d.width += (left + mright);
			d.height += (top + mbottom);
			return d;
		}
		
		
		public void sizeComponents(Axis axis)
		{
			axis.computePositions(horizontal ? left : top, gap);
			
			int xr;
			if(horizontal)
			{
				xr = 0;
			}
			else
			{
				xr = ltr ? 0 : parent.getWidth();
			}
			
			int sz = cells.size();
			for(int i=0; i<sz; i++)
			{
				Entry en = cells.get(i);
				Component c = en.component;
				if(c != null)
				{
					int pos = axis.pos[i];
					int ext = axis.pos[i + 1] - pos - gap;
	
					if(horizontal)
					{
						int y = top;
						int h = bottom - top;
						
						if(ltr)
						{
							c.setBounds(pos, y, ext, h);
						}
						else
						{
							c.setBounds(xr - pos - ext, y, ext, h);
						}
					}
					else
					{
						int x = left;
						int w = right - left;
						c.setBounds(x, pos, w, ext);
					}
				}
			}
		}
		

		public void layout()
		{
			Axis a = new Axis();
			Dimension d = a.computeSizes(true, true);

			if(horizontal)
			{
				int dw = right - left - d.width;
				if(dw != 0)
				{
					a.adjust(dw);
				}
			}
			else
			{
				int dh = bottom - top - d.height;
				if(dh != 0)
				{
					a.adjust(dh);
				}
			}

			sizeComponents(a);
		}
	}
}
