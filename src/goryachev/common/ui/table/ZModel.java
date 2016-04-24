// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.table;
import goryachev.common.util.CList;
import java.util.Collection;


/**
 * Simple item-based table model similar to old CTableModel
 */
public class ZModel<V>
	extends ZBaseModel<V>
{
	private CList<V> rows = new CList();
	
	
	public ZModel()
	{
	}
	
	
	public void addItem(V item)
	{
		insertItem(getRowCount(), item);
	}


	public void insertItem(int ix, V item)
	{
		insertItemNoEvent(ix, item);
		fireTableRowsInserted(ix, ix);
	}
	
	
	public void insertItems(int start, Collection<? extends V> items)
	{
		if(items != null)
		{
			int sz = items.size();
			if(sz > 0)
			{
				int ix = start;
				for(V item: items)
				{
					insertItemNoEvent(ix, item);
					ix++;
				}
			
				fireTableRowsInserted(start, start + sz - 1);
			}
		}
	}
	
	
	public void insertItems(int start, V[] items)
	{
		if(items != null)
		{
			int sz = items.length;
			if(sz > 0)
			{
				int ix = start;
				for(V item: items)
				{
					insertItemNoEvent(ix, item);
					ix++;
				}
			
				fireTableRowsInserted(start, start + sz - 1);
			}
		}
	}


	protected void insertItemNoEvent(int ix, V val)
	{
		// TODO elastic?
		rows.add(ix, val);
	}
	

	protected void clearNoEvents()
	{
		rows.clear();
	}


	protected void removeNoEvents(int ix)
	{
		if((ix >= 0) && (ix < size()))
		{
			rows.remove(ix);
		}
	}


	protected void removeNoEvents(int first, int lastExclusive)
	{
		for(int i=lastExclusive-1; i>=first; i--)
		{
			rows.remove(i);
		}
	}


	public int getRowCount()
	{
		return rows.size();
	}
	
	
	public boolean isCellEditable(int row, int col)
	{
		V item = getItem(row);
		return columnHandler(col).isCellEditable(item); // && super.isCellEditable(row, col);
	}

	
	public V getItem(int row)
	{
		if((row >= 0) && (row < size()))
		{
			return rows.get(row);
		}		
		return null;
	}
	
	
	public void setItem(int row, V item)
	{
		setItemNoEvent(row, item);
		fireTableRowUpdated(row);
	}
	
	
	protected void setItemNoEvent(int row, V item)
	{
		rows.set(row, item);
	}
	

	public void replaceAll(Collection<? extends V> list)
	{
		clearNoEvents();
		addAllNoEvent(list);
		fireTableDataChanged();
	}


	public void replaceAll(V[] a)
	{
		clearNoEvents();
		addAllNoEvent(a);
		fireTableDataChanged();
	}
	
	
	public void addAll(Collection<? extends V> items)
	{
		insertItems(size(), items);
	}
	
	
	public void addAll(V ... items)
	{
		insertItems(size(), items);
	}


	protected void addAllNoEvent(Collection<? extends V> items)
	{
		if(items != null)
		{
			for(V item: items)
			{
				insertItemNoEvent(size(), item);
			}
		}
	}


	protected void addAllNoEvent(V[] items)
	{
		if(items != null)
		{
			for(V item: items)
			{
				insertItemNoEvent(size(), item);
			}
		}
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
	
	
	public CList<V> asList()
	{
		return new CList(rows);
	}
}
