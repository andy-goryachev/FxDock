// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.table;
import goryachev.common.ui.CAlignment;


// elastic datatype-agnostic table model
public class ElasticTableModel
	extends ZBaseTableModelCommon
{
	public ElasticTableModel()
	{
	}
	
	
	public void addColumn(String name)
	{
		ZColumnInfo c = addColumnPrivate();
		c.name = name;
	}
	
	
	public void addColumn(int fixedWidth, String name)
	{
		ZColumnInfo c = addColumnPrivate();
		c.name = name;
		c.min = fixedWidth;
		c.max = fixedWidth;
	}
	
	
	public void addColumn(String name, CAlignment align)
	{
		ZColumnInfo c = addColumnPrivate();
		c.name = name;
		c.align = align;
	}
	
	
	public void addColumn(String name, CAlignment align, int preferredWidth)
	{
		ZColumnInfo c = addColumnPrivate();
		c.name = name;
		c.align = align;
		c.pref = preferredWidth;
	}
	
	
	public void addColumn(String name, int preferredWidth)
	{
		ZColumnInfo c = addColumnPrivate();
		c.name = name;
		c.pref = preferredWidth;
	}
	
	
	public void addColumn(String name, int minWidth, int maxWidth)
	{
		addColumn(name, null, (minWidth + maxWidth)/2, minWidth, maxWidth);
	}
	
	
	public void addColumn(String name, CAlignment align, int prefWidth, int minWidth, int maxWidth)
	{
		ZColumnInfo c = addColumnPrivate();
		c.name = name;
		c.align = align;
		c.pref = prefWidth;
		c.min = minWidth;
		c.max = maxWidth;
	}
	
	
	public void insertColumn(int ix, String name)
	{
		// FIX
	}
	
	
	public void setHandler(ElasticColumnHandler h)
	{
		lastColumn().handler = h;
	}

	
	public void setValueAt(Object x, int row, int col)
	{
		ensureRow(row);
		
		int sz = Math.max(col + 1, getColumnCount());
		Object[] cells = rows().get(row);
		if(cells == null)
		{
			cells = new Object[sz];
			rows().set(row, cells);
		}
		else
		{
			if(cells.length < sz)
			{
				Object[] r2 = new Object[sz];
				System.arraycopy(cells, 0, r2, 0, sz);
				rows().set(row, r2);
				cells = r2;
			}
		}
		
		cells[col] = x;
	}
	
	
	protected void ensureRow(int row)
	{
		int sz = row - size();
		if(sz >= 0)
		{
			sz++;
			int start = size();
			
			for(int i=0; i<sz; i++)
			{
				rows().add(null);
			}
			
			fireTableRowsInserted(start, size()-1);
		}
	}


	protected void setValuesNoEvent(int row, Object[] data)
	{
		for(int c=0; c<data.length; c++)
		{
			setValueAt(data[c], row, c);
		}
	}


	public void setValues(int row, Object... data)
	{
		setValuesNoEvent(row, data);
		fireTableRowsUpdated(row, row);
	}


	public void addValues(Object... data)
	{
		int row = size();
		setValuesNoEvent(getRowCount(), data);
		fireTableRowsInserted(row, row);
	}
	
	
	public Object getSelectedCell(CTableSelector sel)
	{
		int r = sel.getSelectedModelRow();
		if(r >= 0)
		{
			int c = sel.getSelectedModelColumn();
			if(c >= 0)
			{
				return getValueAt(r, c);
			}
		}
		return null;
	}
}
