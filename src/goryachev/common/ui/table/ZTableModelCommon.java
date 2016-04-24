// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.table;
import goryachev.common.ui.CAlignment;
import goryachev.common.ui.UI;
import goryachev.common.util.CList;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;


public abstract class ZTableModelCommon
	extends AbstractTableModel
{
	protected abstract void clearNoEvents();

	protected abstract void removeNoEvents(int ix);

	protected abstract void removeNoEvents(int first, int lastExclusive);
	
	public abstract int getRowCount();
	
	public abstract Object getValueAt(int row, int col);
	
	//
	
	private CList<ZColumnInfo> columns = new CList();
	
	
	public ZTableModelCommon()
	{
	}
	
	
	protected ZColumnInfo addColumnPrivate()
	{
		ZColumnInfo c = new ZColumnInfo();
		columns.add(c);
		return c;
	}
	
	
	public void setFixedWidth(int x)
	{
		setMinWidth(x);
		setMaxWidth(x);
	}
	
	
	public void setRightAlignment()
	{
		setAlignment(CAlignment.TRAILING);
	}
	
	
	public void setLeftAlignment()
	{
		setAlignment(CAlignment.LEADING);
	}
	
	
	public void setCenterAlignment()
	{
		setAlignment(CAlignment.CENTER);
	}
	
	
	public void setAlignment(CAlignment a)
	{
		lastColumn().align = a;
	}
	
	
	public void setMinWidth(int x)
	{
		lastColumn().min = x;
	}

	
	public void setMaxWidth(int x)
	{
		lastColumn().max = x;
	}
	
	
	public void setPreferredWidth(int x)
	{
		lastColumn().pref = x;
	}
	
	
	public void setCellEditor(TableCellEditor ed)
	{
		lastColumn().editor = ed;
		lastColumn().setEditable(ed != null);
	}
	
	
	public void setCellEditor(JTextField ed)
	{
		lastColumn().editor = ed;
		lastColumn().setEditable(ed != null);
	}
	
	
	public void setCellEditor(JCheckBox ed)
	{
		lastColumn().editor = ed;
		lastColumn().setEditable(ed != null);
	}
	
	
	public void setCellEditor(JComboBox ed)
	{
		lastColumn().editor = ed;
		lastColumn().setEditable(ed != null);
	}
	
	
	public void setCellRenderer(TableCellRenderer r)
	{
		lastColumn().renderer = r;
	}
	
	
	public void setCellRenderer(JCheckBox r)
	{
		lastColumn().renderer = r;
	}
	
	
	public void setHeaderRenderer(TableCellRenderer r)
	{
		lastColumn().headerRenderer = r;
	}
	
	
	public void setToolTip(String s)
	{
		lastColumn().tooltip = s;
	}
	
	
	protected ZColumnInfo lastColumn()
	{
		return columns.get(columns.size() - 1);
	}
	
	
	public boolean isColumnEditable(int col)
	{
		return columns().get(col).isEditable();
	}


	public boolean isCellEditable(int row, int col)
	{
		return isColumnEditable(col);
	}


	public int getColumnCount()
	{
		return columns().size();
	}
	
	
	public String getColumnName(int col)
	{
		try
		{
			return columns().get(col).name;
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	
	protected CList<ZColumnInfo> columns()
	{
		return columns;
	}
	
	
	public ZColumnInfo getColumnInfo(int ix)
	{
		return columns.get(ix);
	}
	
	
	public int size()
	{
		return getRowCount();
	}

	
	public void clear()
	{
		int sz = size();
		if(sz > 0)
		{
			clearNoEvents();
			// causes IndexOutOfBoundsException: Invalid range
			//fireTableRowsDeleted(0, sz-1);
			fireTableDataChanged();
		}
	}
	
	
	public int columnIndexByName(String name)
	{
		int ix = 0;
		for(ZColumnInfo c: columns())
		{
			if(name.equals(c.name))
			{
				return ix; 
			}
			
			ix++;
		}
		return -1;
	}
	
	
	public void setColumnName(int ix, String name)
	{
		getColumnInfo(ix).name = name;
		fireTableStructureChanged();
	}
	

	public void remove(int ix)
	{
		removeNoEvents(ix);
		fireTableRowsDeleted(ix, ix);
	}
	
	
	public void remove(int first, int lastExclusive)
	{
		removeNoEvents(first, lastExclusive);
		fireTableRowsDeleted(first, lastExclusive);
	}


	public void configureRenderer(int col, TableColumn c, TableCellRenderer r)
	{
		ZColumnInfo f = getColumnInfo(col);
		if(f.min >= 0)
		{
			c.setMinWidth(f.min);
		}
		if(f.max >= 0)
		{
			c.setMaxWidth(f.max);
		}
		if(f.pref >= 0)
		{
			c.setPreferredWidth(f.pref);
		}
		
		if(f.align != null)
		{
			UI.setHorizontalAlignment(r, f.align.getAlignment());
		}
		
		if(r instanceof JComponent)
		{
			((JComponent)r).setToolTipText(f.tooltip);
		}
	}

	
//	public void setColumnTitle(ZColumnInfo zi, String title)
//	{
//		int ix = columns.indexOf(zi);
//		if(ix >= 0)
//		{
//			zi.name = title;
//			fireTableStructureChanged();
//		}
//	}
}
