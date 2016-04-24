// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.table;
import goryachev.common.util.CList;
import goryachev.common.util.CMap;
import goryachev.common.util.Rex;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;


// item-based model
// ZTableColumn: converst binary to text, configures the renderer for each column
// populateRow() uses the item to populate the actual cells.
public abstract class ZTableModel<K,V>
	extends ZBaseTableModelCommon
	implements Iterable<V>
{
	protected abstract V getRowValue(K key);
	
	//
	
	public ZTableModel()
	{
	}
	
	
	public void addColumn(String name, ZColumnHandler<? extends V> h)
	{
		ZColumnInfo zi = addColumnPrivate();
		zi.name = name;
		zi.handler = h;
	}
	
	
	public void addColumn(String name, int preferredWidth, ZColumnHandler<? extends V> h)
	{
		ZColumnInfo zi = addColumnPrivate();
		zi.name = name;
		zi.handler = h;
		zi.pref = preferredWidth;
	}
	
	
	public void addItem(K item)
	{
		insertItem(getRowCount(), item);
	}
	
	
	public void insertItem(int ix, K key)
	{
		insertItemNoEvent(ix, key);
		fireTableRowsInserted(ix,ix);
	}
	

	public void setItem(int ix, K key)
	{
		Object[] d = constructRowHoldingArray(key);
		rows().set(ix, d);
		populateRow(ix);
		
		fireTableRowsUpdated(ix, ix);
	}

	
	protected void insertItemNoEvent(int ix, K key)
	{
		Object[] d = constructRowHoldingArray(key);
		rows().add(ix, d);
		populateRow(ix);
	}
	
	
	protected Object[] constructRowHoldingArray(K key)
	{
		int sz = getColumnCount();
		Object[] d = new Object[sz + 1];
		d[sz] = key;
		return d;
	}
	
	
	protected void populateRow(int ix)
	{
		K key = getRowKey(ix);
		V val = getRowValue(key);
		
		int sz = getColumnCount();
		for(int i=0; i<sz; i++)
		{
			ZColumnInfo c = columns().get(i);
			ZColumnHandler<V> h = (ZColumnHandler<V>)c.handler;
			Object rv;
			if(h == null)
			{
				rv = null;
			}
			else
			{
				rv = h.getCellValue(val);
			}
			
			setValueNoEvent(rv, ix, i);
		}
	}
	
	
	protected void setValueNoEvent(Object val, int row, int col)
	{
		Object[] cells = rows().get(row);
		cells[col] = val;
	}


	public K getRowKey(int ix)
	{
		if(ix < 0)
		{
			return null;
		}
		else if(ix >= size())
		{
			return null;
		}
		else
		{
			return (K)rows().get(ix)[getColumnCount()];
		}
	}
	
	
	public void addAll(K[] a)
	{
		addAll(Arrays.asList(a));
	}
	
	
	protected void addAllNoEvent(Collection<K> keys)
	{
		if(keys != null)
		{
			for(K key: keys)
			{
				insertItemNoEvent(size(), key);
			}
		}
	}
	
	
	protected void addAllNoEvent(K[] keys)
	{
		if(keys != null)
		{
			for(K key: keys)
			{
				insertItemNoEvent(size(), key);
			}
		}
	}
	

	public void addAll(Collection<K> c)
	{
		int start = size();
		addAllNoEvent(c);
		fireTableRowsInserted(start, size()-1);
	}
	
	
	public int indexOfKey(K item)
	{
		int sz = getRowCount();
		for(int i=0; i<sz; i++)
		{
			if(item.equals(getRowKey(i)))
			{
				return i;
			}
		}
		return -1;
	}
	
	
	public K removeItem(K key)
	{
		int ix = indexOfKey(key);
		if(ix >= 0)
		{
			key = getRowKey(ix);
			remove(ix);
			return key;
		}
		else
		{
			return null;
		}
	}
	
	
	public void replaceAll(Collection<K> list)
	{
		clearNoEvents();
		addAllNoEvent(list);
		
		fireTableDataChanged();
	}


	public void replaceAll(K[] a)
	{
		clearNoEvents();
		addAllNoEvent(a);

		fireTableDataChanged();
	}


	public CList<K> getKeys()
	{
		int sz = size();
		CList<K> list = new CList(sz);
		for(int i=0; i<sz; i++)
		{
			list.add(getRowKey(i));
		}
		return list;
	}

	
	public K getItem(ZTable t, int viewRow)
	{
		if(t.getModel() != this)
		{
			throw new Rex("wrong table");
		}
		
		int ix = t.convertRowIndexToModel(viewRow);
		return getRowKey(ix);
	}
	
	
	public K getItem(int modelRow)
	{
		return getRowKey(modelRow);
	}
	
	
	// refresh by key, return true if updated
	public boolean refresh(K key)
	{
		int ix = indexOfKey(key);
		if(ix >= 0)
		{
			refreshRow(ix);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	public void refreshSelectedRows(CTableSelector sel)
	{
		for(int ix: sel.getSelectedModelRows())
		{
			refreshRow(ix);
		}
	}
	
	
	public void refreshAll(CTableSelector sel)
	{
		refreshAll();
	}
	
	
	public void refreshRow(int ix)
	{
		populateRow(ix);
		fireTableRowsUpdated(ix, ix);
	}
	
	
	public void refreshAll()
	{
		int sz = size();
		if(sz > 0)
		{
			for(int i=0; i<sz; i++)
			{
				populateRow(i);
			}
			
			fireTableRowsUpdated(0, sz-1);
		}
	}
	
	
	public CList<K> getSelectedKeys(CTableSelector sel)
	{
		CList<K> list = new CList();
		for(int ix: sel.getSelectedModelRows())
		{
			list.add(getRowKey(ix));
		}
		return list;
	}
	
	
	public void setSelectedKeys(CTableSelector sel, CList<K> keys)
	{
		CMap<K,Object> m = new CMap(keys.size());
		for(K k: keys)
		{
			m.put(k, null);
		}
		
		sel.clearSelection();
		for(int i=0; i<size(); i++)
		{
			K k = getRowKey(i);
			if(m.containsKey(k))
			{
				sel.addSelectedModelRow(i);
			}
		}
	}
	
	
	public CList<V> getSelectedEntries(CTableSelector sel)
	{
		CList<V> list = new CList();
		for(int ix: sel.getSelectedModelRows())
		{
			K k = getItem(ix);
			list.add(getRowValue(k));
		}
		return list;
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
			K k = getItem(ix);
			return getRowValue(k);
		}
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
				K k = getItem(ix++);
				return getRowValue(k);
			}

			public void remove()
			{
				throw new UnsupportedOperationException();
			}
		};
	}
}
