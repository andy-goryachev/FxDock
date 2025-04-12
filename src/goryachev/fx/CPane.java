// Copyright © 2016-2025 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.log.Log;
import goryachev.common.util.CKit; // FIX remove!
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
 * TODO snap to pixel!
 */
public class CPane
	extends Pane
{
	private static final Log log = Log.get("CPane");
	public static final CssStyle STYLE = new CssStyle();
	public static final double FILL = -1.0;
	public static final double PREF = -2.0;
	
	public static final CC TOP = new CC(true);
	public static final CC BOTTOM = new CC(true);
	public static final CC LEFT = new CC(true);
	public static final CC RIGHT = new CC(true);
	public static final CC CENTER = new CC(true);
	
	protected CList<Entry> entries = new CList<>();
	protected CList<AC> cols = new CList<>();
	protected CList<AC> rows = new CList<>();
	private static final StyleablePropertyFactory<CPane> SPF = new StyleablePropertyFactory<>(Pane.getClassCssMetaData());
	private final StyleableProperty<Number> hgap = SPF.createStyleableNumberProperty(this, "hgap", "-fx-hgap", s -> s.hgap);
	private final StyleableProperty<Number> vgap = SPF.createStyleableNumberProperty(this, "vgap", "-fx-vgap", s -> s.vgap);
	

	public CPane()
	{
	}
	
	
	public CPane(Node n)
	{
		setCenter(n);
	}
	
	
	/** sets standard padding and gaps */
	public final void setDefaultStyle()
	{
		STYLE.set(this);
	}
	
	
	/** sets horizontal gap for the table layout portion of the layout */
	public final void setHGap(double gap)
	{
		hgap.setValue(gap);
	}


	/** returns horizontal gap */
	public final double getHGap()
	{
		return hgap.getValue().doubleValue();
	}
	
	
	public final ObservableValue<Number> hgapProperty()
	{
		return (ObservableValue<Number>)hgap;
	}
	

	/** sets vertical gap for the table layout portion of the layout */
	public final void setVGap(double gap)
	{
		vgap.setValue(gap);
	}


	/** returns vertical gap */
	public final double getVGap()
	{
		return vgap.getValue().doubleValue();
	}
	
	
	public final ObservableValue<Number> vgapProperty()
	{
		return (ObservableValue<Number>)vgap;
	}


	@Override
	public List<CssMetaData<? extends Styleable,?>> getCssMetaData()
	{
		return SPF.getCssMetaData();
	}

	
	/** sets horizontal and vertical gaps. */
	public final void setGaps(double horizontal, double vertical)
	{
		setHGap(horizontal);
		setVGap(vertical);
	}
	
	
	/** sets horizontal and vertical gaps. */
	public final void setGaps(double gaps)
	{
		setHGap(gaps);
		setVGap(gaps);
	}
	
	
	/** a shortcut to set padding on the panel */
	public final void setPadding(double gap)
	{
		setPadding(FX.insets(gap));
	}
	
	
	/** a shortcut to set padding on the panel */
	public final void setPadding(double ver, double hor)
	{
		setPadding(FX.insets(ver, hor));
	}
	
	
	/** a shortcut to set padding on the panel */
	public final void setPadding(double top, double right, double bottom, double left)
	{
		setPadding(FX.insets(top, right, bottom, left));
	}
	
	
	/** convenience method creates a right-aligned Label */
	public final static Label rlabel(String text)
	{
		return FX.label(text, Pos.BASELINE_RIGHT);
	}


	/** returns number of columns for the table portion of the layout (ignoring border components) */
	public final int getCenterColumnCount()
	{
		return cols.size();
	}
	

	/** returns number of rows for the table portion of the layout (ignoring border components) */
	public final int getCenterRowCount()
	{
		return rows.size();
	}
	
	
	public final void addColumn(double spec)
	{
		cols.add(new AC(spec));
	}
	
	
	public final void addColumns(double ... specs)
	{
		for(double cs: specs)
		{
			addColumn(cs);
		}
	}

	
	public final void insertColumn(int ix, double spec)
	{
		// TODO
		CKit.todo();
	}
	
	
	public final void addRow(double spec)
	{
		rows.add(new AC(spec));
	}
	
	
	public final void addRows(double ... specs)
	{
		for(double rs: specs)
		{
			addRow(rs);
		}
	}

	
	public final void insertRow(int ix, double spec)
	{
		if(ix < 0)
		{
			throw new IllegalArgumentException("negative row: " + ix);
		}
		if(ix >= getCenterRowCount())
		{
			ix = getCenterRowCount();
		}
		
		rows.add(ix, new AC());
		
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
	
	
	private AC getColumnSpec(int col)
	{
		while(getCenterColumnCount() <= col)
		{
			addColumn(PREF);
		}
		return cols.get(col);
	}
	
	
	public final void setColumnMinimumSize(int col, int size)
	{
		AC c = getColumnSpec(col);
		c.min = size;
	}
	
	
	public final void setColumnSpec(int col, double spec)
	{
		AC c = getColumnSpec(col);
		c.width = spec;
	}
	
	
	private AC getRowSpec(int row)
	{
		while(getCenterRowCount() <= row)
		{
			addRow(PREF);
		}
		return rows.get(row);
	}
	
	
	public final void setRowMinimumSize(int row, int size)
	{
		AC c = getRowSpec(row);
		c.min = size;
	}
	
	
	public final void setRow(int row, double spec)
	{
		AC c = getRowSpec(row);
		c.width = spec;
	}
	
	
	public final Node add(Node c)
	{
		setCenter(c);
		return c;
	}
	
	
	public final void addRow(int row, Node ... ns)
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
	
	
	public final void add(int col, int row, Node nd)
	{
		add(col, row, 1, 1, nd);
	}
	
	
	public final void add(int col, int row, int colSpan, int rowSpan, Node nd)
	{
		addPrivate(nd, new CC(col, row, col + colSpan - 1, row + rowSpan - 1));
	}
	
	
	private Entry getEntry(Node c)
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
	
	
	private Node getBorderComponent(CC cc)
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


	private Node set(Node c, CC cc)
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
	

	public final Node setCenter(Node c)
	{
		return set(c, CENTER);
	}


	public final Node getCenter()
	{
		return getBorderComponent(CENTER);
	}

	
	public final Node setRight(Node c)
	{
		return set(c, RIGHT);
	}


	public final Node getRight()
	{
		return getBorderComponent(RIGHT);
	}

	
	public final Node setLeft(Node c)
	{
		return set(c, LEFT);
	}


	public final Node getLeft()
	{
		return getBorderComponent(LEFT);
	}


	public final Node setTop(Node c)
	{
		return set(c, TOP);
	}


	public final Node getTop()
	{
		return getBorderComponent(TOP);
	}

	
	public final Node setBottom(Node c)
	{
		return set(c, BOTTOM);
	}


	public final Node getBottom()
	{
		return getBorderComponent(BOTTOM);
	}
	

	private void addPrivate(Node nd, CC cc)
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
					cols.add(new AC());
				}
				
				int mxr = cc.row2;
				while(rows.size() <= mxr)
				{
					rows.add(new AC());
				}
			}
		}

		en.cc = cc;
		getChildren().add(nd);
	}
	
	
	/** removes all children */
	public final void clear()
	{
		getChildren().clear();
	}


	private void removeLayoutComponent(Node nd)
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

	
	public final void remove(Node c)
	{
		removeLayoutComponent(c);
	}
	
	
	private void setBounds(Node nd, double left, double top, double width, double height)
	{
		layoutInArea(nd, left, top, width, height, 0, HPos.CENTER, VPos.CENTER);
	}
	

	@Override
	protected double computePrefWidth(double height)
	{
		return new Helper().computeWidth(true);
	}
	
	
	@Override
	protected double computePrefHeight(double width)
	{
		return new Helper().computeHeight(true);	
	}


	@Override
	protected double computeMinWidth(double height)
	{
		return new Helper().computeWidth(false);
	}
	
	
	@Override
	protected double computeMinHeight(double width)
	{
		return new Helper().computeHeight(false);
	}
	
	
	@Override
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

	
	/** Row/Column Alignment */
	private static enum AL
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
	
	
	/** Axis Contstraints */
	private static class AC
	{
		public double width; // [0..1[ : percent, >=1 in pixels, <0 special
		public double min;
		public double max;
		public int group;
		public AL align;
		
		
		public AC()
		{
			width = PREF;
			align = AL.FULL;
		}
		
		
		public AC(double width)
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
	
	
	/** cell contstraints */
	private static class CC
	{
		/** starting column */
		public int col;
		/** ending  column */
		public int col2;
		/** starting row */
		public int row;
		/** ending row */
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
	private static class Entry
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
	private abstract class Axis
	{
		public abstract int start(CC cc);
		
		public abstract int end(CC cc);
		
		public abstract double sizingMethod(boolean pref, Node c, double other);
		
		public abstract double otherDimension(Entry en, boolean doingLayout);
		
		public abstract double snap(double v);
		
		//
		
		/** snapped gap */
		public final double gap;
		/** row/column specifications */
		public final CList<AC> specs;
		public Entry left;
		public Entry center;
		public Entry right;
		/** row/column snapped sizes */
		public double[] size;
		public double[] pos;
		public Axis otherAxis;
		
		
		public Axis(CList<AC> specs, double gap)
		{
			this.specs = specs;
			this.gap = snap(gap);
			size = new double[specs.size()];
		}
		
		
		private void computePositions(double start, double gap)
		{
			int sz = size.length;
			pos = new double[sz + 1];
			
			pos[0] = start;
			
			for(int i=0; i<sz; i++)
			{
				start = snap(start + (size[i] + gap));
				pos[i+1] = start;
			}
		}
		
		
		// minimum width if set, 0 otherwise
		private double min(int ix)
		{
			return specs.get(ix).min;
		}
		
		
		// fixed width if set, 0 otherwise
		private double fixed(int ix)
		{
			double w = specs.get(ix).width;
			if(w >= 1.0)
			{
				return w;
			}
			return 0;
		}
		
		
		// max width of the column
		private double max(int ix)
		{
			double max = specs.get(ix).max;
			if(max > 0)
			{
				return max;
			}
			return Double.POSITIVE_INFINITY;
		}
		
		
		// amount of space occupied by columns in the given range, including gaps
		private double aggregateSize(int start, int end, double gap)
		{
			double rv = 0.0;
			
			for(int i=start; i<end; i++)
			{
				rv = snap(rv + size[i]);
			}
			
			int ngaps = end - start;
			if(ngaps > 0)
			{
				rv = snap(rv + ngaps * gap);
			}
			
			return rv;
		}
		
		
		// true if component spans a scaled column
		private boolean spansScaled(int start, int end)
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
		
		
		private double computeSizes(boolean pref, boolean doingLayout)
		{
			// total width
			double total = 0;
			
			// scan rows/columns
			int sz = specs.size();
			for(int i=0; i<sz; i++)
			{
				double w = fixed(i);
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
								double cw = d - aggregateSize(start, i, gap);
								if(cw > w)
								{
									w = cw;
								}
								
								double mx = max(i);
								if(w > mx)
								{
									w = mx;
									break;
								}
							}
						}						
					}
					
					double min = min(i);
					if(w < min)
					{
						w = min;
					}
				}
				
				size[i] = snap(w);
				
				total = snap(total + w);
			}
			
			if(sz > 1)
			{
				// FIX might be incorrect due to multiple snapping
				total = snap(total + (gap * (sz - 1)));
			}
			
			return total;
		}
		
		
		private void adjust(double delta)
		{
			// space available for FILL/PERCENT columns
			double available = delta;
			// ratio of columns with percentage explicitly set
			double percent = 0;
			// number of FILL columns
			int fillsCount = 0;
			
			int sz = specs.size();
			for(int i=0; i<sz; i++)
			{
				AC lc = specs.get(i);
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
			double remaining = available;
			
			// PERCENT sizes first
			for(int i=0; i<sz; i++)
			{
				AC lc = specs.get(i);
				if(lc.isPercent())
				{
					double w;
					if(remaining > 0)
					{
						w = snap(lc.width * available * percentFactor);
					}
					else
					{
						w = 0.0;
					}
					
					double d = w;
					size[i] = d;
					remaining -= d;
				}
			}
			
			// FILL sizes after PERCENT
			if(fillsCount > 0)
			{
				double cw = remaining / fillsCount;
				
				for(int i=0; i<sz; i++)
				{
					AC lc = specs.get(i);
					if(lc.isFill())
					{
						double w;
						if(remaining >= 0)
						{
							w = snap(Math.min(cw, remaining));
						}
						else
						{
							w = 0.0;
						}
						
						double d = w;
						size[i] = d;
						remaining -= d;
					}
				}
			}
		}
	}
	
	
	//
	
	
	private class Helper
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
		

		private void scanBorderComponents()
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
		
		
		private double sizeHeight(boolean pref, Node n)
		{
			double d = n.minHeight(-1);
			if(pref)
			{
				d = Math.max(d, n.prefHeight(-1));
			}
			return d;
		}
		
		
		private double sizeWidth(boolean pref, Node n)
		{
			double d = n.minWidth(-1);
			if(pref)
			{
				d = Math.max(d, n.prefWidth(-1));
			}
			return d;
		}
		
		
		private int computeBorderHeight(boolean pref)
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
		
		
		private int computeBorderWidth(boolean pref)
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
		
		
		private Axis createHorAxis()
		{
			double gap = getHGap();
			return new Axis(cols, gap)
			{
				@Override
				public int start(CC cc)
				{
					return cc.col;
				}


				@Override
				public int end(CC cc)
				{
					return cc.col2;
				}


				@Override
				public double sizingMethod(boolean pref, Node n, double other)
				{
					double d = n.minWidth(other);
					if(pref)
					{
						d = Math.max(n.prefWidth(other), d);
					}
					return d;
				}
				
				
				@Override
				public double otherDimension(Entry en, boolean doingLayout)
				{
					// asymmetry: horizontal layout is first, and no other dimension is available
					return -1;
				}

				
				@Override
				public double snap(double v)
				{
					return snapPositionX(v);
				}
			};
		}
		
		
		private Axis createVerAxis()
		{
			double gap = getVGap();
			return new Axis(rows, gap)
			{
				@Override
				public int start(CC cc)
				{
					return cc.row;
				}


				@Override
				public int end(CC cc)
				{
					return cc.row2;
				}


				@Override
				public double sizingMethod(boolean pref, Node n, double other)
				{
					double d = n.minHeight(other);
					if(pref)
					{
						d = snap(Math.max(n.prefHeight(other), d));
					}
					return d;
				}
				
				
				@Override
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
							other = snap(other + otherAxis.size[i]);
						}
						
						other = snap(other + (gap * (end - start)));
						return other;
					}
					else
					{
						return -1;
					}
				}
				
				@Override
				public double snap(double v)
				{
					return snapPositionY(v);
				}
			};
		}
		
			
		// similar to border layout
		private void layoutBorderComponents()
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
			double w = hor.computeSizes(pref, false);
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
					double x = hor.pos[cc.col];
					double w = hor.pos[cc.col2 + 1] - x - getHGap();
	
					double y = ver.pos[cc.row];
					double h = ver.pos[cc.row2 + 1] - y - getVGap();

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
			double w = hor.computeSizes(true, true);

			double dw = tableRight - tableLeft - w;
			if(dw != 0.0) // TODO snap?
			{
				hor.adjust(dw);
			}

			Axis ver = createVerAxis();
			ver.otherAxis = hor;
			double h = ver.computeSizes(true, true);
			
			double dh = tableBottom - tableTop - h;
			if(dh != 0.0) // TODO snap?
			{
				ver.adjust(dh);
			}

			sizeComponents(hor, ver);
		}
	}
}
