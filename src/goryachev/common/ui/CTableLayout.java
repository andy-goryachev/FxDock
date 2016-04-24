// Copyright (c) 2015-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.util.CList;
import goryachev.common.util.Log;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;


/** 
 * A new table layout, combination of BorderLayout and TableLayout.
 * 
 * Column spec:
 *   float 0.0 ... 1.0: preferred size as a fraction of total size
 *   float > 1.0 fixed size in pixels
 *   PREFERRED, FILL
 *   
 *   TODO SPEC(minw [pixels,%,dlu], prefw, maxw, group)
 *   
 * unless explicitly set, row or column spec defaults to PREFERRED.
 */
public class CTableLayout
	implements LayoutManager2
{
	//public static final float MINIMUM = -1.0f;
	public static final float PREFERRED = -2.0f;
	public static final float FILL = -3.0f;
	
	public static final CC TOP = new CC(true);
	public static final CC BOTTOM = new CC(true);
	public static final CC LEADING = new CC(true);
	public static final CC TRAILING = new CC(true);
	public static final CC CENTER = new CC(true);
	
	protected int hgap;
	protected int vgap;
	protected CList<Entry> entries = new CList();
	protected CList<LC> cols = new CList();
	protected CList<LC> rows = new CList();


	public CTableLayout()
	{
	}
	
	
	public void setHGap(int gap)
	{
		hgap = gap;
	}


	public void setVGap(int gap)
	{
		vgap = gap;
	}


	public int getHGap()
	{
		return hgap;
	}


	public int getVGap()
	{
		return vgap;
	}


	/** returns number of columns for the table portion of the layout (ignoring border components) */
	public int getColumnCount()
	{
		return cols.size();
	}
	

	/** returns number of rows for the table portion of the layout (ignoring border components) */
	public int getRowCount()
	{
		return rows.size();
	}
	
	
	public void addColumn(float spec)
	{
		cols.add(new LC(spec));
	}

	
	public void insertColumn(int ix, float spec)
	{
		// TODO
	}
	
	
	public void addRow(float spec)
	{
		rows.add(new LC(spec));
	}

	
	public void insertRow(int ix, float spec)
	{
		// TODO
	}
	
	
	public LC getColumnSpec(int col)
	{
		while(getColumnCount() <= col)
		{
			addColumn(PREFERRED);
		}
		return cols.get(col);
	}
	
	
	public void setColumnMinimumSize(int col, int size)
	{
		LC c = getColumnSpec(col);
		c.min = size;
	}
	
	
	public void setColumnSpec(int col, float spec)
	{
		LC c = getColumnSpec(col);
		c.width = spec;
	}
	
	
	public LC getRowSpec(int row)
	{
		while(getRowCount() <= row)
		{
			addRow(PREFERRED);
		}
		return rows.get(row);
	}
	
	
	public void setRowMinimumSize(int row, int size)
	{
		LC c = getRowSpec(row);
		c.min = size;
	}
	
	
	public void setRow(int row, float spec)
	{
		LC c = getRowSpec(row);
		c.width = spec;
	}
	
	
	protected Entry getEntry(Component c)
	{
		for(int i=entries.size()-1; i>=0; i--)
		{
			Entry en = entries.get(i);
			if(en.component == c)
			{
				return en;
			}
		}
		return null;
	}
	
	
	public Component getBorderComponent(CC cc)
	{
		for(int i=entries.size()-1; i>=0; i--)
		{
			Entry en = entries.get(i);
			if(en.cc.border)
			{
				if(en.cc == cc)
				{
					return en.component;
				}
			}
		}
		return null;
	}


	public void addLayoutComponent(String name, Component comp)
	{
		throw new UnsupportedOperationException();
	}
	

	public void addLayoutComponent(Component comp, Object constraints)
	{
		CC cc;
		if(constraints instanceof CC)
		{
			cc = (CC)constraints;
		}
		else
		{
			throw new IllegalArgumentException("illegal component constraint: " + constraints);
		}
		
		synchronized(comp.getTreeLock())
		{
			Entry en = getEntry(comp);
			if(en == null)
			{
				en = new Entry();
				en.component = comp;
				
				entries.add(en);
				
				int mxc = cc.col2;
				while(cols.size() <= mxc)
				{
					cols.add(new LC());
				}
				
				int mxr = cc.row2;
				while(rows.size() <= mxr)
				{
					rows.add(new LC());
				}
			}

			en.cc = cc;
		}
	}


	public void removeLayoutComponent(Component comp)
	{
		synchronized(comp.getTreeLock())
		{
			for(int i=entries.size()-1; i>=0; i--)
			{
				Entry en = entries.get(i);
				if(en.component == comp)
				{
					entries.remove(i);
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

	
	/** row/column alignment specification */
	public static enum AL
	{
		TOP,
		BOTTOM,
		LEFT,
		RIGHT,
		LEADING,
		TRAILING,
		FULL
	}
	
	
	//
	
	
	/** layout axis contstraints */
	public static class LC
	{
		public float width; // [0..1[ : percent, >=1 in pixels, <0 special
		public int min;
		public int max;
		public int group;
		public AL align;
		
		
		public LC()
		{
			width = PREFERRED;
			align = AL.FULL;
		}
		
		
		public LC(float width)
		{
			this.width = width;
			align = AL.FULL;
		}
		
		
		public boolean isPercent()
		{
			return (width >= 0.0f) && (width < 1.0f);
		}
		

		public boolean isFill()
		{
			return (width == FILL);
		}


		public boolean isScaled()
		{
			return isFill() || isPercent();
		}
	}
	
	
	//
	
	
	/** component contstraints */
	public static class CC
	{
		public int col;
		public int row;
		public int col2;
		public int row2;
		public AL horAlign;
		public AL verAlign;
		public boolean border;
		
		
		public CC(int col, int row)
		{
			this(col, row, col, row);
		}
		
		
		public CC(int col, int row, int col2, int row2)
		{
			this(col, row, col2, row2, AL.FULL, AL.FULL);
		}
		
		
		public CC(boolean border)
		{
			this.border = border;
		}
		
		
		public CC(int col, int row, int col2, int row2, AL horAlign, AL verAlign)
		{
			this.col = col;
			this.row = row;
			this.col2 = col2;
			this.row2 = row2;
			this.horAlign = horAlign;
			this.verAlign = verAlign;
		}
		
		
		public boolean isTable()
		{
			return !border;
		}
	}
	
	
	//
	
	
	/** component-constraint pair */
	public static class Entry
	{
		public Component component;
		public CC cc;
	}
	
	
	//
	
	
	/** 
	 * axis helper.  
	 * even though the comments talk about columns, the logic is used for both columns and rows:
	 * just replace whe words columns/width with rows/heights
	 */
	public abstract class Axis
	{
		public abstract int start(CC cc);
		
		public abstract int end(CC cc);
		
		public abstract int size(Dimension d);
		
		//
		
		public final int gap;
		public final CList<LC> specs;
		public Entry leading;
		public Entry center;
		public Entry trailing;
		public int[] size;
		public int[] pos;
		
		
		public Axis(CList<LC> specs, int gap)
		{
			this.specs = specs;
			this.gap = gap;
			
			size = new int[specs.size()];
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
			return specs.get(ix).min;
		}
		
		
		// fixed width if set, 0 otherwise
		protected int fixed(int ix)
		{
			float w = specs.get(ix).width;
			if(w >= 1.0f)
			{
				return (int)w;
			}
			return 0;
		}
		
		
		// max width of the column
		protected int max(int ix)
		{
			int max = specs.get(ix).max;
			if(max > 0)
			{
				return max;
			}
			return Integer.MAX_VALUE;
		}
		
		
		// need to set proper width before asking for preferred size
		protected void setProperWidth(Component c, CC cc, Axis hor)
		{
			int start = hor.start(cc);
			int end = hor.end(cc);
			
			int w;
			int sz = end - start;
			if(sz == 0)
			{
				// component occupies a single column
				w = hor.size[start];
			}
			else
			{
				w = 0;
				
				for(int i=0; i<sz; i++)
				{
					w += hor.size[start + i];
				}
				
				w += ((sz - 1) * hgap);
			}
			
			c.setSize(w, 10);
		}
		
		
		// amount of space occupied by columns in the given range, including gaps
		protected int aggregateSize(int start, int end, int gap)
		{
			int rv = 0;
			
			for(int i=start; i<end; i++)
			{
				rv += size[i];
			}
			
			int ngaps = end - start;
			if(ngaps > 0)
			{
				rv += (ngaps * gap);
			}
			
			return rv;
		}
		
		
		// true if component spans scaled column
		protected boolean spansScaled(int start, int end)
		{
			for(int i=start; i<=end; i++)
			{
				if(specs.get(i).isScaled())
				{
					return true;
				}
			}
			return false;
		}
		
		
		protected int computeSizes(boolean preferred, boolean doingLayout)
		{
			// total width
			int total = 0;
			
			// scan rows/columns
			int sz = specs.size();
			for(int i=0; i<sz; i++)
			{
				int w = fixed(i);
				if(w == 0)
				{
					// scan entries to determine which ones ends at this column
					// pick the largest
					int n = entries.size();
					for(int j=0; j<n; j++)
					{
						Entry en = entries.get(j);
						CC cc = en.cc;
						int end = end(cc);
						
						// only if the component ends on this row/col
						if(cc.isTable() && (end == i))
						{
							int start = start(cc);
							
							// layout does not need preferred sizes of components that span scaled columns
							// FIX but needs minimum
							boolean skip = doingLayout && spansScaled(start, end);
							if(!skip)
							{
								Component c = en.component;
								Dimension d = preferred ? c.getPreferredSize() : c.getMinimumSize();
								
								// amount of space component occupies in this column
								int cw = size(d) - aggregateSize(start, i, gap);
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
					
					int min = min(i);
					if(w < min)
					{
						w = min;
					}
				}
				
				size[i] = w;
				
				total += w;
			}
			
			if(sz > 1)
			{
				total += (gap * (sz - 1));
			}
			
			return total;
		}
		
		
		protected void adjust(int delta)
		{
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
						w = Math.round(lc.width * available);
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
		}
	}
	
	
	//
	
	
	public class Helper
	{
		public final Container parent;
		private final boolean ltr;
		public int mtop;
		public int mbottom;
		public int mleft;
		public int mright;
		public Component centerComp;
		public Component topComp;
		public Component bottomComp;
		public Component leadingComp;
		public Component trailingComp;
		private int tableLeft;
		private int tableRight;
		private int tableTop;
		private int tableBottom;
		private int midHeight;


		public Helper(Container parent)
		{
			this.parent = parent;
			ltr = parent.getComponentOrientation().isLeftToRight();
				
			Insets m = parent.getInsets();
			mtop = m.top;
			mbottom = m.bottom;
			mleft = m.left;
			mright = m.right;
		}
		

		protected void scanBorderComponents()
		{		
			for(int i=entries.size()-1; i>=0; i--)
			{
				Entry en = entries.get(i);
				if(en.cc.border)
				{
					CC cc = en.cc;
					
					if(cc == CENTER)
					{
						centerComp = en.component;
					}
					else if(cc == TOP)
					{
						topComp = en.component;
					}
					else if(cc == BOTTOM)
					{
						bottomComp = en.component;
					}
					else if(cc == LEADING)
					{
						leadingComp = en.component;
					}
					else if(cc == TRAILING)
					{
						trailingComp = en.component;
					}
				}
			}
		}
		
		
		protected Dimension computeBorderSize(boolean preferred)
		{
			int w = 0;
			int h = 0;
			Component c;
			
			if((c = ltr ? leadingComp : trailingComp) != null)
			{
				Dimension d = preferred ? c.getPreferredSize() : c.getMinimumSize();
				w += d.width + hgap;
				h = Math.max(d.height, h);
			}
			
			if((c = ltr ? trailingComp : leadingComp) != null)
			{
				Dimension d = preferred ? c.getPreferredSize() : c.getMinimumSize();
				w += d.width + hgap;
				h = Math.max(d.height, h);
			}
			
			midHeight = h;
			
			if(centerComp != null)
			{
				Dimension d = preferred ? centerComp.getPreferredSize() : centerComp.getMinimumSize();
				w += d.width;
				h = Math.max(d.height, h);
			}
			
			if(topComp != null)
			{
				Dimension d = preferred ? topComp.getPreferredSize() : topComp.getMinimumSize();
				w = Math.max(d.width, w);
				h += (d.height + vgap);
			}
			
			if(bottomComp != null)
			{
				Dimension d = preferred ? bottomComp.getPreferredSize() : bottomComp.getMinimumSize();
				w = Math.max(d.width, w);
				h += (d.height + vgap);
			}

			w += mleft + mright;
			h += mtop + mbottom;
			return new Dimension(w, h);
		}
		
		
		protected Axis createHorAxis()
		{
			return new Axis(cols, hgap)
			{
				public int start(CC cc) { return cc.col; }
				public int end(CC cc) { return cc.col2; }
				public int size(Dimension d) { return d.width; }
			};
		}
		
		
		protected Axis createVerAxis()
		{
			return new Axis(rows, vgap)
			{
				public int start(CC cc) { return cc.row; }
				public int end(CC cc) { return cc.row2; }
				public int size(Dimension d) { return d.height; }
			};
		}
		
			
		// similar to border layout
		protected void layoutBorderComponents()
		{	
			int top = mtop;
			int bottom = parent.getHeight() - mbottom;
			int left = mleft;
			int right = parent.getWidth() - mright;

			Component c;
			if(topComp != null)
			{
				c = topComp;
				c.setSize(right - left, c.getHeight());
				Dimension d = c.getPreferredSize();
				c.setBounds(left, top, right - left, d.height);
				top += d.height + vgap;
			}
			
			if(bottomComp != null)
			{
				c = bottomComp;
				c.setSize(right - left, c.getHeight());
				Dimension d = c.getPreferredSize();
				c.setBounds(left, bottom - d.height, right - left, d.height);
				bottom -= d.height + vgap;
			}
			
			if((c = (ltr ? trailingComp : leadingComp)) != null)
			{
				c.setSize(c.getWidth(), bottom - top);
				Dimension d = c.getPreferredSize();
				c.setBounds(right - d.width, top, d.width, bottom - top);
				right -= d.width + hgap;
			}
			
			if((c = (ltr ? leadingComp : trailingComp)) != null)
			{
				c.setSize(c.getWidth(), bottom - top);
				Dimension d = c.getPreferredSize();
				c.setBounds(left, top, d.width, bottom - top);
				left += d.width + hgap;
			}

			if(centerComp != null)
			{
				centerComp.setBounds(left, top, right - left, bottom - top);
				right = left;
				bottom = top;
			}
			
			// space available for table layout components
			tableLeft = left;
			tableRight = right;
			tableTop = top;
			tableBottom = bottom;
		}
		
		
		public Dimension computePreferredSize()
		{
			scanBorderComponents();
			
			Dimension d = computeBorderSize(true);
			
			if(centerComp != null)
			{
				// center component overrides any table layout components
				return d;
			}
			
			Axis hor = createHorAxis();
			int w = hor.computeSizes(true, false);
			
			Axis ver = createVerAxis();
			int h = ver.computeSizes(true, false);

			// height is maximum of border midsection or table section
			h = Math.max(h, midHeight) + (d.height - midHeight);
			
			return new Dimension(w + d.width, h);
		}
		
		
		public Dimension computeMinimumSize()
		{
			scanBorderComponents();
			
			Dimension d = computeBorderSize(false);
			
			if(centerComp != null)
			{
				// center component overrides any table layout components
				return d;
			}
			
			Axis hor = createHorAxis();
			int w = hor.computeSizes(false, false);
			
			Axis ver = createVerAxis();
			int h = ver.computeSizes(false, false);
			
			return new Dimension(w, h);
		}
		
		
		public void sizeComponents(Axis hor, Axis ver)
		{
			hor.computePositions(tableLeft, hgap);
			ver.computePositions(tableTop, vgap);
			
			int xr = ltr ? 0 : tableRight + mright;
			
			int sz = entries.size();
			for(int i=0; i<sz; i++)
			{
				Entry en = entries.get(i);
				CC cc = en.cc;
				
				if(cc.isTable())
				{
					int x = hor.pos[cc.col];
					int w = hor.pos[cc.col2 + 1] - x - hgap;
	
					int y = ver.pos[cc.row];
					int h = ver.pos[cc.row2 + 1] - y - vgap;

					if(ltr)
					{
						en.component.setBounds(x, y, w, h);
					}
					else
					{
						en.component.setBounds(xr - x - w, y, w, h);
					}
				}
			}
		}
		

		public void layout()
		{
			scanBorderComponents();
			layoutBorderComponents();

			Axis hor = createHorAxis();
			int w = hor.computeSizes(true, true);

			int dw = tableRight - tableLeft - w;
			if(dw != 0)
			{
				hor.adjust(dw);
			}

			Axis ver = createVerAxis();
			int h = ver.computeSizes(true, true);
			
			int dh = tableBottom - tableTop - h;
			if(dh != 0)
			{
				ver.adjust(dh);
			}

			sizeComponents(hor, ver);
		}
	}
}
