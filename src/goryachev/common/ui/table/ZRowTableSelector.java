// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.table;
import goryachev.common.util.CList;
import goryachev.common.util.Rex;


/** 
 * This selector works only with tables backed by ZTableModel.
 * For other models use CTableSelector.
 */
public abstract class ZRowTableSelector<T>
	extends CTableSelector
{
	public abstract void tableSelectionChangeDetected();
	
	//
	
	// single selector
	public ZRowTableSelector(ZTable t)
	{
		this(t, false, false, false);
	}
	
	
	// multiple
	public ZRowTableSelector(ZTable t, boolean allowColumnSelection)
	{
		this(t, true, allowColumnSelection, false);
	}
	
	
	// multiple, discontinuous
	public ZRowTableSelector(ZTable t, boolean allowColumnSelection, boolean allowDiscontinuousIntervals)
	{
		this(t, true, allowColumnSelection, allowDiscontinuousIntervals);
	}
	
	
	// most flexible
	public ZRowTableSelector(ZTable t, boolean multiple, boolean allowColumnSelection, boolean allowDiscontinuousIntervals)
	{
		super(t, multiple, allowColumnSelection, allowDiscontinuousIntervals);
		
		if((t.getModel() instanceof ZTableModel) == false)
		{
			throw new Rex("needs ZTableModel");
		}
	}
	
	
	protected ZTableModel<T,?> getZTableModel()
	{
		return (ZTableModel<T,?>)table.getModel();
	}
	
	
	public T getSelectedItem()
	{
		int ix = getSelectedModelRow();
		if(ix >= 0)
		{
			return getZTableModel().getRowKey(ix);
		}
		return null;
	}
	
	
	public CList<T> getSelectedItems()
	{
		int[] ixs = getSelectedModelRows();
		int sz = ixs.length;
		
		CList<T> list = new CList(sz);
		for(int i=0; i<sz; i++)
		{
			list.add(getZTableModel().getRowKey(ixs[i]));
		}
		return list;
	}
}
