// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.table;
import goryachev.common.util.CList;
import java.util.Collection;
import java.util.Iterator;


/**
 * Table model without backing storage.
 */
public abstract class ZBaseModel<V>
	extends ZTableModelCommon
	implements Iterable<V>
{
	public abstract int getRowCount();
	
	public abstract V getItem(int row);

	
	//
	
	
	public ZBaseModel()
	{
	}
	
	
	public ZColumnInfo addColumn(String name, ZColumnHandler<? extends V> h)
	{
		ZColumnInfo zi = addColumnPrivate();
		zi.name = name;
		zi.handler = h;
		return zi;
	}
	
	
	public ZColumnInfo addColumn(String name, int preferredWidth, ZColumnHandler<? extends V> h)
	{
		ZColumnInfo zi = addColumnPrivate();
		zi.name = name;
		zi.handler = h;
		zi.pref = preferredWidth;
		return zi;
	}
	

	public Iterator<V> iterator()
	{
		return new Iterator<V>()
		{
			int ix;
			
			public boolean hasNext()
			{
				return (ix < size());
			}

			public V next()
			{
				return getItem(ix++);
			}

			public void remove()
			{
				throw new UnsupportedOperationException();
			}
		};
	}

	
	public boolean isCellEditable(int row, int col)
	{
		V item = getItem(row);
		return columnHandler(col).isCellEditable(item); // && super.isCellEditable(row, col);
	}

	
	protected ZColumnHandler<V> columnHandler(int col)
	{
		return (ZColumnHandler<V>)getColumnInfo(col).handler;
	}
	

	public Object getValueAt(int row, int col)
	{
		if(row < 0)
		{
			return null;
		}
		else if(col < 0)
		{
			return null;
		}
		
		V item = getItem(row);
		return columnHandler(col).getCellValue(item);
	}


	public void setValueAt(Object val, int row, int col)
	{
		V item = getItem(row);
		columnHandler(col).setCellValue(item, val);
	}

	
	public CList<V> getSelectedEntries(CTableSelector sel)
	{
		CList<V> list = new CList();
		for(int ix: sel.getSelectedModelRows())
		{
			V v = getItem(ix);
			if(v != null)
			{
				list.add(v);
			}
		}
		return list;
	}
	
	
	public void setSelectedEntries(CTableSelector sel, CList<V> items)
	{
		if(items != null)
		{
			int sz = items.size();
			int[] rows = new int[sz];
			for(int i=0; i<sz; i++)
			{
				V item = items.get(i);
				int ix = indexOfKey(item);
				rows[i] = ix;
			}
			
			sel.setSelectedModelRows(rows);
		}
	}
	
	
	public void setSelectedEntry(CTableSelector sel, V item)
	{
		int ix = indexOfKey(item);
		sel.setSelectedModelRow(ix);
	}
	
	
	public V getSelectedEntry(CTableSelector sel)
	{
		int ix = sel.getSelectedModelRow();
		if(ix < 0)
		{
			return null;
		}
		else
		{
			return getItem(ix);
		}
	}
	
	
	public void refreshAll()
	{
		int last = size() - 1;
		if(last >= 0)
		{
			fireTableRowsUpdated(0, last);
		}
	}
	
	
	public void refreshRow(int modelRow)
	{
		fireTableRowsUpdated(modelRow, modelRow);
	}
	
	
	public int indexOfKey(V item)
	{
		if(item == null)
		{
			return -1;
		}
		
		int sz = getRowCount();
		for(int i=0; i<sz; i++)
		{
			if(item.equals(getItem(i)))
			{
				return i;
			}
		}
		return -1;
	}
	
	
	public V removeItem(V item)
	{
		int ix = indexOfKey(item);
		if(ix >= 0)
		{
			item = getItem(ix);
			remove(ix);
			return item;
		}
		else
		{
			return null;
		}
	}
	
	
	public void removeItems(Iterable<V> items)
	{
		if(items != null)
		{
			for(V item: items)
			{
				removeItem(item);
			}
		}
	}
	

	public void fireTableRowUpdated(int row)
	{
		fireTableRowsUpdated(row, row);
	}


	public void fireAllRowsUpdated()
	{
		int sz = size();
		if(sz > 0)
		{
			fireTableRowsUpdated(0, sz - 1);
		}
	}
	
	
	public void fireItemUpdated(V item)
	{
		int ix = indexOfKey(item);
		if(ix >= 0)
		{
			fireTableRowUpdated(ix);
		}
	}
}
