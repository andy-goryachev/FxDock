// Copyright © 2016-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.log.Log;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.StyleablePropertyFactory;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;


/**
 * CPane is like my Swing CPanel, lays out nodes with an outer border layout and an inner table layout.
 * 
 * TODO ignore unmanaged components
 */
public class CPane
	extends Pane
{
	protected static final Log log = Log.get("CPane");
	public static final CssStyle STYLE = new CssStyle("CPane_PANE");
	public static final double FILL = -1.0;
	public static final double PREF = -2.0;
	
	public static final CC TOP = new CC(true);
	public static final CC BOTTOM = new CC(true);
	public static final CC LEFT = new CC(true);
	public static final CC RIGHT = new CC(true);
	public static final CC CENTER = new CC(true);
	
	protected CList<Entry> entries = new CList<>();
	protected CList<LC> cols = new CList<>();
	protected CList<LC> rows = new CList<>();
	private static final StyleablePropertyFactory<CPane> SPF = new StyleablePropertyFactory<>(Pane.getClassCssMetaData());
	private final StyleableProperty<Number> hgap = SPF.createStyleableNumberProperty(this, "hgap", "-ag-hgap", s -> s.hgap);
	private final StyleableProperty<Number> vgap = SPF.createStyleableNumberProperty(this, "vgap", "-ag-vgap", s -> s.vgap);
	

	public CPane()
	{
	}
	
	
	public CPane(Node n)
	{
		setCenter(n);
	}
	
	
	/** sets standard padding and gaps */
	public void setDefaultStyle()
	{
		FX.style(this, STYLE);
	}
	
	
	/** sets horizontal gap for the table layout portion of the layout */
	public void setHGap(int gap)
	{
		hgap.setValue(gap);
	}


	/** returns horizontal gap */
	public int getHGap()
	{
		return hgap.getValue().intValue();
	}
	
	
	public ObservableValue<Number> hgapProperty()
	{
		return (ObservableValue<Number>)hgap;
	}
	

	/** sets vertical gap for the table layout portion of the layout */
	public void setVGap(int gap)
	{
		vgap.setValue(gap);
	}


	/** returns vertical gap */
	public int getVGap()
	{
		return vgap.getValue().intValue();
	}
	
	
	public ObservableValue<Number> vgapProperty()
	{
		return (ObservableValue<Number>)vgap;
	}


	public static List<CssMetaData<? extends Styleable,?>> getClassCssMetaData()
	{
		return SPF.getCssMetaData();
	}


	public List<CssMetaData<? extends Styleable,?>> getCssMetaData()
	{
		return SPF.getCssMetaData();
	}

	
	/** sets horizontal and vertical gaps. */
	public void setGaps(int horizontal, int vertical)
	{
		setHGap(horizontal);
		setVGap(vertical);
	}
	
	
	/** sets horizontal and vertical gaps. */
	public void setGaps(int gaps)
	{
		setHGap(gaps);
		setVGap(gaps);
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
	
	
	/** convenience method creates a right-aligned Label */
	public static Label rlabel(String text)
	{
		return FX.label(text, Pos.BASELINE_RIGHT);
	}


	/** returns number of columns for the table portion of the layout (ignoring border components) */
	public int getCenterColumnCount()
	{
		return cols.size();
	}
	

	/** returns number of rows for the table portion of the layout (ignoring border components) */
	public int getCenterRowCount()
	{
		return rows.size();
	}
	
	
	public void addColumn(double spec)
	{
		cols.add(new LC(spec));
	}
	
	
	public void addColumns(double ... specs)
	{
		for(double cs: specs)
		{
			addColumn(cs);
		}
	}

	
	public void insertColumn(int ix, double spec)
	{
		// TODO
		CKit.todo();
	}
	
	
	public void addRow(double spec)
	{
		rows.add(new LC(spec));
	}
	
	
	public void addRows(double ... specs)
	{
		for(double rs: specs)
		{
			addRow(rs);
		}
	}

	
	public void insertRow(int ix, double spec)
	{
		if(ix < 0)
		{
			throw new IllegalArgumentException("negative row: " + ix);
		}
		if(ix >= getCenterRowCount())
		{
			ix = getCenterRowCount();
		}
		
		rows.add(ix, new LC());
		
		for(Entry en: entries)
		{
			if(en.cc.row >= ix)
			{
				en.cc.row++;
				en.cc.row2++;
			}
			else if(en.cc.row2 >= ix)
			{
				en.cc.row2++;
			}
		}
	}
	
	
	protected LC getColumnSpec(int col)
	{
		while(getCenterColumnCount() <= col)
		{
			addColumn(PREF);
		}
		return cols.get(col);
	}
	
	
	public void setColumnMinimumSize(int col, int size)
	{
		LC c = getColumnSpec(col);
		c.min = size;
	}
	
	
	public void setColumnSpec(int col, double spec)
	{
		LC c = getColumnSpec(col);
		c.width = spec;
	}
	
	
	protected LC getRowSpec(int row)
	{
		while(getCenterRowCount() <= row)
		{
			addRow(PREF);
		}
		return rows.get(row);
	}
	
	
	public void setRowMinimumSize(int row, int size)
	{
		LC c = getRowSpec(row);
		c.min = size;
	}
	
	
	public void setRow(int row, double spec)
	{
		LC c = getRowSpec(row);
		c.width = spec;
	}
	
	
	public Node add(Node c)
	{
		setCenter(c);
		return c;
	}
	
	
	public void addRow(int row, Node ... ns)
	{
		for(int i=0; i<ns.length; i++)
		{
			Node nd = ns[i];
			if(nd != null)
			{
				add(i, row, nd);
			}
		}
	}
	
	
	public void add(int col, int row, Node nd)
	{
		add(col, row, 1, 1, nd);
	}
	
	
	public void add(int col, int row, int colSpan, int rowSpan, Node nd)
	{
		addPrivate(nd, new CC(col, row, col + colSpan - 1, row + rowSpan - 1));
	}
	
	
	protected Entry getEntry(Node c)
	{
		for(int i=entries.size()-1; i>=0; i--)
		{
			Entry en = entries.get(i);
			if(en.node == c)
			{
				return en;
			}
		}
		return null;
	}
	
	
	protected Node getBorderComponent(CC cc)
	{
		int sz = entries.size();
		for(int i=0; i<sz; i++)
		{
			Entry en = entries.get(i);
			if(en.cc.border)
			{
				if(en.cc == cc)
				{
					return en.node;
				}
			}
		}
		return null;
	}


	protected Node set(Node c, CC cc)
	{
		Node old = getBorderComponent(cc);
		if(old != c)
		{
			if(old != null)
			{
				removeLayoutComponent(old);
			}
			
			if(c != null)
			{
				addPrivate(c, cc);
			}
		}
		return old;
	}
	

	public Node setCenter(Node c)
	{
		return set(c, CENTER);
	}


	public Node getCenter()
	{
		return getBorderComponent(CENTER);
	}

	
	public Node setRight(Node c)
	{
		return set(c, RIGHT);
	}


	public Node getRight()
	{
		return getBorderComponent(RIGHT);
	}

	
	public Node setLeft(Node c)
	{
		return set(c, LEFT);
	}


	public Node getLeft()
	{
		return getBorderComponent(LEFT);
	}


	public Node setTop(Node c)
	{
		return set(c, TOP);
	}


	public Node getTop()
	{
		return getBorderComponent(TOP);
	}

	
	public Node setBottom(Node c)
	{
		return set(c, BOTTOM);
	}


	public Node getBottom()
	{
		return getBorderComponent(BOTTOM);
	}
	

	protected void addPrivate(Node nd, CC cc)
	{
		Entry en = getEntry(nd);
		if(en == null)
		{
			// once in a CPane, surrender your limitations!
			if(nd instanceof Region)
			{
				Region r = (Region)nd;
				r.setMaxWidth(Double.MAX_VALUE);
				r.setMaxHeight(Double.MAX_VALUE);
			}
			
			en = new Entry();
			en.node = nd;
			
			entries.add(en);
			
			if(!cc.border)
			{
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
		}

		en.cc = cc;
		getChildren().add(nd);
	}
	
	
	/** removes all children */
	public void clear()
	{
		getChildren().clear();
	}


	protected void removeLayoutComponent(Node nd)
	{
		for(int i=entries.size()-1; i>=0; i--)
		{
			Entry en = entries.get(i);
			if(en.node == nd)
			{
				entries.remove(i);
				getChildren().remove(nd);
				return;
			}
		}
	}

	
	public void remove(Node c)
	{
		removeLayoutComponent(c);
	}
	
	
	protected void setBounds(Node nd, int left, int top, int width, int height)
	{
		layoutInArea(nd, left, top, width, height, 0, HPos.CENTER, VPos.CENTER);
	}
	

	protected double computePrefWidth(double height)
	{
		return new Helper().computeWidth(true);
	}
	
	
	protected double computePrefHeight(double width)
	{
		return new Helper().computeHeight(true);	
	}


	protected double computeMinWidth(double height)
	{
		return new Helper().computeWidth(false);
	}
	
	
	protected double computeMinHeight(double width)
	{
		return new Helper().computeHeight(false);
	}
	
	
	protected void layoutChildren()
	{
		try
		{
			new Helper().layout();
		}
		catch(Exception e)
		{
			log.error(e);
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
		public double width; // [0..1[ : percent, >=1 in pixels, <0 special
		public int min;
		public int max;
		public int group;
		public AL align;
		
		
		public LC()
		{
			width = PREF;
			align = AL.FULL;
		}
		
		
		public LC(double width)
		{
			this.width = width;
			align = AL.FULL;
		}
		
		
		public boolean isPercent()
		{
			return (width >= 0.0) && (width < 1.0);
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
	}
	
	
	//
	
	
	/** component-constraint pair */
	protected static class Entry
	{
		public Node node;
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
		
		public abstract double sizingMethod(boolean pref, Node c, double other);
		
		public abstract double otherDimension(Entry en, boolean doingLayout);
		
		//
		
		public final int gap;
		/** row/column specifications */
		public final CList<LC> specs;
		public Entry left;
		public Entry center;
		public Entry right;
		/** row/column sizes */
		public int[] size;
		public int[] pos;
		public Axis otherAxis;
		
		
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
			double w = specs.get(ix).width;
			if(w >= 1.0)
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
		
		
		// true if component spans a scaled column
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
		
		
		protected int computeSizes(boolean pref, boolean doingLayout)
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
						if((!cc.border) && (end == i))
						{
							int start = start(cc);
							
							// layout does not need preferred sizes of components that span scaled columns
							// FIX but needs minimum
							boolean skip = doingLayout && spansScaled(start, end);
							if(!skip)
							{
								double other = otherDimension(en, doingLayout);
								int d = CKit.ceil(sizingMethod(pref, en.node, other));
								
								// amount of space component occupies in this column
								int cw = d - aggregateSize(start, i, gap);
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
			double percent = 0;
			// number of FILL columns
			int fillsCount = 0;
			
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
				LC lc = specs.get(i);
				if(lc.isPercent())
				{
					double w;
					if(remaining > 0)
					{
						w = lc.width * available * percentFactor;
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
					LC lc = specs.get(i);
					if(lc.isFill())
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
	}
	
	
	//
	
	
	public class Helper
	{
		private final boolean ltr;
		public int mtop;
		public int mbottom;
		public int mleft;
		public int mright;
		public Node centerComp;
		public Node topComp;
		public Node bottomComp;
		public Node leftComp;
		public Node rightComp;
		private int tableLeft;
		private int tableRight;
		private int tableTop;
		private int tableBottom;
		private int midHeight;


		public Helper()
		{
			ltr = true; // FIX (getNodeOrientation() == NodeOrientation.LEFT_TO_RIGHT);
				
			Insets m = getInsets();
			mtop = CKit.round(m.getTop());
			mbottom = CKit.round(m.getBottom());
			mleft = CKit.round(m.getLeft());
			mright = CKit.round(m.getRight());
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
						centerComp = en.node;
					}
					else if(cc == TOP)
					{
						topComp = en.node;
					}
					else if(cc == BOTTOM)
					{
						bottomComp = en.node;
					}
					else if(cc == LEFT)
					{
						leftComp = en.node;
					}
					else if(cc == RIGHT)
					{
						rightComp = en.node;
					}
				}
			}
		}
		
		
		protected double sizeHeight(boolean pref, Node n)
		{
			double d = n.minHeight(-1);
			if(pref)
			{
				d = Math.max(d, n.prefHeight(-1));
			}
			return d;
		}
		
		
		protected double sizeWidth(boolean pref, Node n)
		{
			double d = n.minWidth(-1);
			if(pref)
			{
				d = Math.max(d, n.prefWidth(-1));
			}
			return d;
		}
		
		
		protected int computeBorderHeight(boolean pref)
		{
			int h = 0;
			Node c;
			
			if((c = ltr ? leftComp : rightComp) != null)
			{
				int d = CKit.ceil(sizeHeight(pref, c));
				h = Math.max(d, h);
			}
			
			if((c = ltr ? rightComp : leftComp) != null)
			{
				int d = CKit.ceil(sizeHeight(pref, c));
				h = Math.max(d, h);
			}
			
			midHeight = h;
			
			if(centerComp != null)
			{
				int d = CKit.ceil(sizeHeight(pref, centerComp));
				h = Math.max(d, h);
			}
			
			if(topComp != null)
			{
				int d = CKit.ceil(sizeHeight(pref, topComp));
				h += (d + getVGap());
			}
			
			if(bottomComp != null)
			{
				int d = CKit.ceil(sizeHeight(pref, bottomComp));
				h += (d + getVGap());
			}

			h += (mtop + mbottom);
			return h;
		}
		
		
		protected int computeBorderWidth(boolean pref)
		{
			int w = 0;
			Node c;
			
			if((c = ltr ? leftComp : rightComp) != null)
			{
				int d = CKit.ceil(sizeWidth(pref, c));
				w += (d + getHGap());
			}
			
			if((c = ltr ? rightComp : leftComp) != null)
			{
				int d = CKit.ceil(sizeWidth(pref, c));
				w += (d + getHGap());
			}
			
			if(centerComp != null)
			{
				int d = CKit.ceil(sizeWidth(pref, centerComp));
				w += d;
			}
			
			if(topComp != null)
			{
				int d = CKit.ceil(sizeWidth(pref, topComp));
				w = Math.max(d, w);
			}
			
			if(bottomComp != null)
			{
				int d = CKit.ceil(sizeWidth(pref, bottomComp));
				w = Math.max(d, w);
			}

			w += (mleft + mright);
			return w;
		}
		
		
		protected Axis createHorAxis()
		{
			return new Axis(cols, getHGap())
			{
				public int start(CC cc) { return cc.col; }
				public int end(CC cc) { return cc.col2; }
				
				public double sizingMethod(boolean pref, Node n, double other)
				{
					double d = n.minWidth(other);
					if(pref)
					{
						d = Math.max(n.prefWidth(other), d);
					}
					return d;
				}
				
				public double otherDimension(Entry en, boolean doingLayout)
				{
					// asymmetry: horizontal layout is first, and no other dimension is available
					return -1;
				}
			};
		}
		
		
		protected Axis createVerAxis()
		{
			return new Axis(rows, getVGap())
			{
				public int start(CC cc) { return cc.row; }
				public int end(CC cc) { return cc.row2; }
				
				public double sizingMethod(boolean pref, Node n, double other)
				{
					double d = n.minHeight(other);
					if(pref)
					{
						d = Math.max(n.prefHeight(other), d);
					}
					return d;
				}
				
				public double otherDimension(Entry en, boolean doingLayout)
				{
					if(doingLayout)
					{
						// needs other dimension to compute sizes properly
						int start = otherAxis.start(en.cc);
						int end = otherAxis.end(en.cc);
						double other = 0;
						for(int i=start; i<=end; i++)
						{
							other += otherAxis.size[i];
						}
						
						other += (gap * (end - start));
						
						return other;
					}
					else
					{
						return -1;
					}
				}
			};
		}
		
			
		// similar to border layout
		protected void layoutBorderComponents()
		{	
			int top = mtop;
			int bottom = CKit.round(getHeight()) - mbottom;
			int left = mleft;
			int right = CKit.round(getWidth()) - mright;

			Node c;
			if(topComp != null)
			{
				c = topComp;
				int h = CKit.ceil(c.prefHeight(right - left));
				setBounds(c, left, top, right - left, h);
				top += (h + getVGap());
			}
			
			if(bottomComp != null)
			{
				c = bottomComp;
				int h = CKit.ceil(c.prefHeight(right - left));
				setBounds(c, left, bottom - h, right - left, h);
				bottom -= (h + getVGap());
			}
			
			if((c = (ltr ? rightComp : leftComp)) != null)
			{
				int w = CKit.ceil(c.prefWidth(bottom - top));
				setBounds(c, right - w, top, w, bottom - top);
				right -= (w + getHGap());
			}
			
			if((c = (ltr ? leftComp : rightComp)) != null)
			{
				int w = CKit.ceil(c.prefWidth(bottom - top));
				setBounds(c, left, top, w, bottom - top);
				left += (w + getHGap());
			}

			if(centerComp != null)
			{
				setBounds(centerComp, left, top, right - left, bottom - top);
				right = left;
				bottom = top;
			}
			
			// space available for table layout components
			tableLeft = left;
			tableRight = right;
			tableTop = top;
			tableBottom = bottom;
		}
		
		
		public double computeWidth(boolean pref)
		{
			scanBorderComponents();
			
			int d = computeBorderWidth(pref);
			
			if(centerComp != null)
			{
				// center component overrides any table layout components
				return d;
			}
			
			Axis hor = createHorAxis();
			int w = hor.computeSizes(pref, false);
			return w + d;
		}
		
		
		public double computeHeight(boolean pref)
		{
			scanBorderComponents();
			
			double d = computeBorderHeight(pref);
			
			if(centerComp != null)
			{
				// center component overrides any table layout components
				return d;
			}
			
			Axis ver = createVerAxis();
			double h = ver.computeSizes(pref, false);

			// height is maximum of border midsection or table section
			h = Math.max(h, midHeight) + (d - midHeight);
			return h;
		}
		
		
		public void sizeComponents(Axis hor, Axis ver)
		{
			hor.computePositions(tableLeft, getHGap());
			ver.computePositions(tableTop, getVGap());
			
			int xr = ltr ? 0 : tableRight + mright;
			
			int sz = entries.size();
			for(int i=0; i<sz; i++)
			{
				Entry en = entries.get(i);
				CC cc = en.cc;
				
				if(!cc.border)
				{
					int x = hor.pos[cc.col];
					int w = hor.pos[cc.col2 + 1] - x - getHGap();
	
					int y = ver.pos[cc.row];
					int h = ver.pos[cc.row2 + 1] - y - getVGap();

					if(ltr)
					{
						setBounds(en.node, x, y, w, h);
					}
					else
					{
						setBounds(en.node, xr - x - w, y, w, h);
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
			ver.otherAxis = hor;
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
