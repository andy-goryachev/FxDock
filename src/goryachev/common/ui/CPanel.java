// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.util.Rex;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;


/** A convenient JPanel with CTableLayout3, a hybrid of BorderLayout and TableLayout. */
public class CPanel
	extends JPanel
	implements Scrollable
{
	public static final float FILL = CTableLayout.FILL;
	//public static final float MINIMUM = CTableLayout3.MINIMUM;
	public static final float PREFERRED = CTableLayout.PREFERRED;
	
	private boolean trackWidth = true;
	private boolean trackHeight;
	private int currentRow;
	
	
	public CPanel()
	{
		super(new CTableLayout());
	}
	
	
	public CPanel(Component c)
	{
		super(new CTableLayout());
		setCenter(c);
	}
	
	
	public CPanel(int hgap, int vgap)
	{
		super(new CTableLayout());
		setGaps(hgap, vgap);
	}
	
	
	public CPanel(boolean opaque)
	{
		super(new CTableLayout());
		setOpaque(opaque);
	}
	
	
	public CPanel(int hgap, int vgap, boolean opaque)
	{
		super(new CTableLayout());
		setGaps(hgap, vgap);
		setOpaque(opaque);
	}
	
	
	public void noBorder()
	{
		setBorder(CBorder.NONE);
	}
	
	
	public void borderNoBottomGap()
	{
		// TODO use theme
		setBorder(new CBorder(10, 10, 0, 10));
	}
	
	
	public void setLayout(LayoutManager m)
	{
		if(m instanceof CTableLayout)
		{
			super.setLayout(m);
		}
		else
		{
			throw new Rex();
		}
	}
	
	
	private Component set(Component c, CTableLayout.CC cc)
	{
		Component old = tableLayout().getBorderComponent(cc);
		if(old != null)
		{
			remove(old);
		}
		
		if(c != null)
		{
			add(c, cc);
		}
		return old;
	}
	

	public Component setCenter(Component c)
	{
		return set(c, CTableLayout.CENTER);
	}


	public Component getCenter()
	{
		return tableLayout().getBorderComponent(CTableLayout.CENTER);
	}


	public Component setEast(Component c)
	{
		return setTrailing(c);
	}
	
	
	public Component setTrailing(Component c)
	{
		return set(c, CTableLayout.TRAILING);
	}


	public Component getTrailing()
	{
		return tableLayout().getBorderComponent(CTableLayout.TRAILING);
	}


	public Component setWest(Component c)
	{
		return setLeading(c);
	}
	
	
	public Component setLeading(Component c)
	{
		return set(c, CTableLayout.LEADING);
	}


	public Component getLeading()
	{
		return tableLayout().getBorderComponent(CTableLayout.LEADING);
	}


	public Component setNorth(Component c)
	{
		return setTop(c);
	}


	public Component setTop(Component c)
	{
		return set(c, CTableLayout.TOP);
	}


	public Component getTop()
	{
		return tableLayout().getBorderComponent(CTableLayout.TOP);
	}


	public Component setSouth(Component c)
	{
		return setBottom(c);
	}
	
	
	public Component setBottom(Component c)
	{
		return set(c, CTableLayout.BOTTOM);
	}


	public Component getBottom()
	{
		return tableLayout().getBorderComponent(CTableLayout.BOTTOM);
	}


	public Dimension getPreferredScrollableViewportSize()
	{
		return null;
	}


	public int getScrollableUnitIncrement(Rectangle r, int orientation, int direction)
	{
		return 10;
	}
	

	public int getScrollableBlockIncrement(Rectangle r, int orientation, int direction)
	{
		switch(orientation)
		{
		case SwingConstants.HORIZONTAL: return (r.width * 80 / 100);
		case SwingConstants.VERTICAL:   return (r.height * 80 / 100);
		}
		return 10;
	}
	

	public boolean getScrollableTracksViewportWidth()
	{
		return trackWidth;
	}
	
	
	public void setScrollableTracksViewportWidth(boolean on)
	{
		trackWidth = on;
	}
	

	public boolean getScrollableTracksViewportHeight()
	{
		return trackHeight;
	}
	
	
	public void setScrollableTracksViewportHeight(boolean on)
	{
		trackHeight = on;
	}


	public void setPreferredSize(int w, int h)
	{
		setPreferredSize(new Dimension(w, h));
	}


	public void setMinimumSize(int w, int h)
	{
		setMinimumSize(new Dimension(w, h));
	}


	public JLabel label(String s)
	{
		JLabel t = new JLabel(s);
		t.setHorizontalAlignment(JLabel.TRAILING);
		return t;
	}


	public JLabel labelTopAligned(String s)
	{
		JLabel t = label(s);
		t.setVerticalAlignment(JLabel.TOP);
		return t;
	}
	
	
	public JLabel labelBottomAligned(String s)
	{
		JLabel t = label(s);
		t.setVerticalAlignment(JLabel.BOTTOM);
		return t;
	}


	public JLabel heading(String s)
	{
		JLabel t = new JLabel(s);
		t.setFont(t.getFont().deriveFont(Font.BOLD));
		return t;
	}


	public InfoField info(String text)
	{
		InfoField t = new InfoField(text);
		return t;
	}
	
	
	public InfoField text(String text)
	{
		InfoField t = new InfoField(text);
		t.setForeground(Theme.TEXT_FG);
		return t;
	}
	
	
	public JLabel icon(Icon ic)
	{
		JLabel t = new JLabel(ic);
		t.setVerticalAlignment(JLabel.TOP);
		return t;
	}


	/** creates standard 10-pixel border */
	public void border()
	{
		setBorder();
	}


	/** creates standard 10-pixel border */
	public void setBorder()
	{
		setBorder(Theme.border10());
	}


	public CBorder setBorder(int gap)
	{
		CBorder b = new CBorder(gap);
		setBorder(b);
		return b;
	}
	
	
	public CBorder setBorder(int vertGap, int horGap)
	{
		CBorder b = new CBorder(vertGap, horGap); 
		setBorder(b);
		return b;
	}
	
	
	public CBorder setBorder(int top, int left, int bottom, int right)
	{
		CBorder b = new CBorder(top, left, bottom, right);
		setBorder(b);
		return b;
	}
	
	
	public CTableLayout tableLayout()
	{
		return (CTableLayout)getLayout();
	}
	
	
	public int getTableLayoutRowCount()
	{
		return tableLayout().getRowCount();
	}
	
	
	public int getTableLayoutColumnCount()
	{
		return tableLayout().getColumnCount();
	}
	

	public void setGaps(int gap)
	{
		setGaps(gap, gap);
	}


	public void setGaps(int horizontal, int vertical)
	{
		setHGap(horizontal);
		setVGap(vertical);
	}


	public void setHGap(int gap)
	{
		tableLayout().setHGap(gap);
	}


	public void setVGap(int gap)
	{
		tableLayout().setVGap(gap);
	}


	public int getHGap()
	{
		return tableLayout().getHGap();
	}


	public int getVGap()
	{
		return tableLayout().getVGap();
	}
	
	
	public void setColumnMinimumSize(int col, int size)
	{
		tableLayout().setColumnMinimumSize(col, size);
	}
	
	
	public void addRow()
	{
		addRow(PREFERRED);
	}
	
	
	public void addFillRow()
	{
		addRow(FILL);
	}
	
	
	public void addRow(float spec)
	{
		tableLayout().addRow(spec);
	}
	
	
	public void addRows(float ... specs)
	{
		for(float rs: specs)
		{
			addRow(rs);
		}
	}
	
	
	public void nextRow()
	{
		nextRow(PREFERRED);
	}
	
	
	public void nextFillRow()
	{
		nextRow(FILL);
	}
	
	
	public void nextRow(float spec)
	{
		addRow(spec);
		currentRow++;
	}
	
	
	public void skipRow()
	{
		currentRow++;
	}
	
	
	public void addColumn(float spec)
	{
		tableLayout().addColumn(spec);
	}
	
	
	public void addColumn()
	{
		addColumn(PREFERRED);
	}
	
	
	public void addFillColumn()
	{
		addColumn(FILL);
	}
	
	
	public void addColumns(float ... specs)
	{
		for(float cs: specs)
		{
			addColumn(cs);
		}
	}
	
	
	protected int row()
	{
		while(currentRow >= getTableLayoutRowCount())
		{
			addRow();
		}
		return currentRow;
	}
	
	
	public void row(int col, Component c)
	{
		int r = row();
		add(c, new CTableLayout.CC(col, r));
	}
	
	
	public void row(int col, int colSpan, Component c)
	{
		int r = row();
		add(c, new CTableLayout.CC(col, r, col + colSpan - 1, r));
	}
	
	
	public void row(int col, int colSpan, int rowSpan, Component c)
	{
		int r = row();
		add(c, new CTableLayout.CC(col, r, col + colSpan - 1, r + rowSpan - 1));
	}
	
	
	public Component add(Component c)
	{
		setCenter(c);
		return c;
	}
	
	
	public void add(int col, int row, Component c)
	{
		add(col, row, 1, 1, c);
	}
	
	
	public void add(int col, int row, int colSpan, int rowSpan, Component c)
	{
		add(c, new CTableLayout.CC(col, row, col + colSpan - 1, row + rowSpan - 1));
	}


	public CToolBar toolbar()
	{
		Component c = getTop();
		if(c instanceof CToolBar)
		{
			return (CToolBar)c;
		}
		else
		{
			CToolBar t = Theme.toolbar();
			setTop(t);
			UI.validateAndRepaint(this);
			return t;
		}
	}


	public CButtonPanel buttonPanel()
	{
		Component c = getBottom();
		if(c instanceof CButtonPanel)
		{
			return (CButtonPanel)c;
		}
		else
		{
			CButtonPanel p = new CButtonPanel();
			setBottom(p);
			UI.validateAndRepaint(this);
			return p;
		}
	}
	
	
	public boolean hasButtonPanel()
	{
		Component c = getBottom();
		return (c instanceof CButtonPanel);
	}
}

