// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.table;
import goryachev.common.ui.UI;
import goryachev.common.util.Parsers;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import javax.swing.DefaultRowSorter;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;


public class ZTable
	extends JTable
{
	private int maxSortKeys;
	private static int MINIMUM_TABLE_HEIGHT = 16;
	
	
	public ZTable()
	{
		this(new ElasticTableModel());
	}
	
	
	public ZTable(TableModel m)
	{
		super(m);
	}
	

	public void setFont(Font f)
	{
		super.setFont(f);
		adjustRowHeight();
	}
	
	
	public void adjustRowHeight()
	{
		FontMetrics fm = getFontMetrics(getFont());
		int sz = 
			fm.getMaxAscent() + fm.getMaxDescent() +
			//fm.getHeight() + 
			CTableRendererBorder.getVerticalSpace();
		
		Dimension d = getIntercellSpacing();
		if(d != null)
		{
			sz += d.height;
		}
		
		if(sz < MINIMUM_TABLE_HEIGHT)
		{
			sz = MINIMUM_TABLE_HEIGHT;
		}
		
		setRowHeight(sz);
	}
	
	
	protected JTextField createTextEditor()
	{
		return new JTextField();
	}
	

	public void createDefaultColumnsFromModel()
	{
		TableModel m = getModel();
		if(m instanceof ZTableModelCommon)
		{
			ZTableModelCommon zm = (ZTableModelCommon)m;

			TableColumnModel cm = getColumnModel();
			while(cm.getColumnCount() > 0)
			{
				cm.removeColumn(cm.getColumn(0));
			}

			for(int i=0; i<zm.getColumnCount(); i++)
			{
				ZColumnInfo f = zm.getColumnInfo(i);
				
				// editor
				TableCellEditor editor = createEditor(f.editor);

				// renderer
				TableCellRenderer renderer = createRenderer(f.renderer);
				
				TableColumn c = new TableColumn(i, 75, renderer, editor);
				
				// header renderer
				if(f.headerRenderer != null)
				{
					c.setHeaderRenderer(f.headerRenderer);
				}

				addColumn(c);
				
				if(f.align != null)
				{
					UI.setHorizontalAlignment(editor, f.align.getAlignment());
				}
				
				zm.configureRenderer(i, c, renderer);
			}
		}
		else
		{
			super.createDefaultColumnsFromModel();
		}
	}
	
	
	protected TableCellRenderer createRenderer(Object x)
	{
		if(x instanceof TableCellRenderer)
		{
			return (TableCellRenderer)x;
		}
		else if(x instanceof JCheckBox)
		{
			return new CBooleanRenderer();
		}
		else
		{
			return new ZTableRenderer();
		}
	}
	
	
	protected TableCellEditor createEditor(Object x)
	{
		if(x instanceof TableCellEditor)
		{
			return (TableCellEditor)x;
		}
		else if(x instanceof JTextField)
		{
			return new ZTableCellEditor((JTextField)x);
		}
		else if(x instanceof JCheckBox)
		{
			return new ZTableCellEditor((JCheckBox)x);
		}
		else if(x instanceof JComboBox)
		{
			return new ZTableCellEditor((JComboBox)x);
		}
		else
		{
			JTextField ed = createTextEditor();
			return new ZTableCellEditor(ed);
		}
	}


	public ZColumnInfo getColumnInfo(int viewColumn)
	{
		int mcol = convertColumnIndexToModel(viewColumn);
		TableModel m = getModel();
		if(m instanceof ZTableModelCommon)
		{
			return ((ZTableModelCommon)m).getColumnInfo(mcol);
		}
		else
		{
			return null;
		}
	}
	
	
	public void setSortable(boolean on)
	{
		setMaxSortColumnCount(on ? 1 : 0);
	}
	
	
	public void setMaxSortColumnCount(int maxSortKeys)
	{
		this.maxSortKeys = maxSortKeys;
		updateSorter();
	}


//	public void setAutoCreateRowSorter(boolean autoCreateRowSorter)
//	{
//		boolean oldValue = this.autoCreateRowSorter;
//		this.autoCreateRowSorter = autoCreateRowSorter;
//		if(autoCreateRowSorter)
//		{
//			setRowSorter(new TableRowSorter<TableModel>(getModel()));
//		}
//		firePropertyChange("autoCreateRowSorter", oldValue, autoCreateRowSorter);
//	}
	
	
	protected void updateSorter()
	{
		if(maxSortKeys > 0)
		{
			setRowSorter(new ZTableRowSorter(this, maxSortKeys));
		}
		else
		{
			setRowSorter(null);
		}
	}


	public void setRowFilter(final ZRowFilter f)
	{
		DefaultRowSorter rs = (DefaultRowSorter)getRowSorter();
		rs.setRowFilter(new RowFilter<TableModel,Integer>()
		{
			public boolean include(RowFilter.Entry<? extends TableModel,? extends Integer> en)
			{
				if(f == null)
				{
					return true;
				}
				else
				{
					TableModel m = en.getModel();
					Integer ix = en.getIdentifier();
					return f.include(m, ix);
				}
			}
		});
	}


	public void selectRow(int viewRow)
	{
		if((viewRow >= 0) && (viewRow < getRowCount()))
		{
			changeSelection(viewRow, 0, false, false);
		}
	}
	
	
	public void copy()
	{
		UI.invokeCopyAction(this);
	}


	public String getCellText(int row, int col)
	{
		row = convertRowIndexToModel(row);
		col = convertColumnIndexToModel(col);

		TableModel model = getModel();
		Object x = model.getValueAt(row, col);

		if(model instanceof ZTableModelCommon)
		{
			ZTableModelCommon m = (ZTableModelCommon)model;
			ZColumnInfo ci = m.getColumnInfo(col);
			if(ci.handler != null)
			{
				return ci.handler.getText(x);
			}
		}
		
		return Parsers.parseString(x);
	}


	public void setCellRenderer(int modelColumn, ZTableRenderer r)
	{
		int col = convertColumnIndexToView(modelColumn);
		TableColumn c = getColumnModel().getColumn(col);
		
		TableModel m = getModel();
		if(m instanceof ZTableModelCommon)
		{
			ZTableModelCommon zm = (ZTableModelCommon)m;
			zm.configureRenderer(modelColumn, c, r);
		}
		
		c.setCellRenderer(r);
	}


	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction)
	{
		int d = super.getScrollableBlockIncrement(visibleRect, orientation, direction);
		// it's in the ui somewhere
		//D.print(d);
		return d;
	}
	
	
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction)
	{
		int d = super.getScrollableUnitIncrement(visibleRect, orientation, direction);
		//D.print(d);
		return d;
	}
	
	
	public void stopEditing()
	{
		UI.stopEditing(this);
	}


	public int getColumnsPreferredWidth()
	{
		int w = 0;
		int cols = getColumnCount();
		for(int i=0; i<cols; i++)
		{
			int cw = getColumnPreferredWidth(i);
			w += cw;
		}
		
		// cols - 1 to be exact
		w += (cols * getIntercellSpacing().width);
		
		return w;
	}


	public int getColumnPreferredWidth(int col)
	{
		int w = 0;
		
		for(int row=0; row<getRowCount(); row++)
		{
			int cw = getPreferredCellWidth(row, col);
			if(w < cw)
			{
				w = cw;
			}
		}
		
		return w;
	}


	public void scrollModelRowToVisible(int ix)
	{
		int row = convertRowIndexToView(ix);
		if(row >= 0)
		{
			Rectangle r = getCellRect(row, 0, true);
			scrollRectToVisible(r);
		}
	}
	
	
	public int getPreferredCellWidth(int row, int col)
	{
		Object val = getValueAt(row, col);
		Component c = getCellRenderer(row, col).getTableCellRendererComponent(this, val, false, false, row, col);
		return c.getPreferredSize().width;
	}
}
