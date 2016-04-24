// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.table;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.Log;
import java.util.List;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public abstract class CTableSelector
	implements ListSelectionListener
{
	public abstract void tableSelectionChangeDetected();
	
	//

	private static final int[] NONE = new int[0];
	public final JTable table;
	private int[] selected = NONE;
	private boolean handleEvents = true;
	private Selection selection;


	// single selector
	public CTableSelector(JTable t)
	{
		this(t, false, false, false);
	}
	
	
	// multiple
	public CTableSelector(JTable t, boolean allowColumnSelection)
	{
		this(t, true, allowColumnSelection, false);
	}
	
	
	// multiple, discontinuous
	public CTableSelector(JTable t, boolean allowColumnSelection, boolean allowDiscontinuousIntervals)
	{
		this(t, true, allowColumnSelection, allowDiscontinuousIntervals);
	}
	
	
	// most flexible
	public CTableSelector(JTable t, boolean multiple, boolean allowColumnSelection, boolean allowDiscontinuousIntervals)
	{
		this.table = t;
		
		t.getSelectionModel().addListSelectionListener(this);
		
		// FIX need to listen to column selection changes!
		t.setColumnSelectionAllowed(allowColumnSelection);
		if(allowColumnSelection)
		{
			t.getColumnModel().getSelectionModel().addListSelectionListener(this);
		}
		
		t.getSelectionModel().setSelectionMode(selectionMode(multiple, allowDiscontinuousIntervals));
	}
	
	
	public void setAllowMultipleSelection(boolean on)
	{
		table.getSelectionModel().setSelectionMode(selectionMode(on, isAllowDiscontinuousIntervals()));
	}
	
	
	public boolean isAllowMultipleSelection()
	{
		int m = table.getSelectionModel().getSelectionMode();
		switch(m)
		{
		case ListSelectionModel.SINGLE_INTERVAL_SELECTION:
		case ListSelectionModel.MULTIPLE_INTERVAL_SELECTION:
			return true;
		default:
			return false;
		}
	}
	
	
	public void setAllowDiscontinuousIntervals(boolean on)
	{
		table.getSelectionModel().setSelectionMode(selectionMode(isAllowMultipleSelection(), on));
	}
	
	
	public boolean isAllowDiscontinuousIntervals()
	{
		int m = table.getSelectionModel().getSelectionMode();
		switch(m)
		{
		case ListSelectionModel.MULTIPLE_INTERVAL_SELECTION:
			return true;
		default:
			return false;
		}
	}


	private int selectionMode(boolean multiple, boolean allowDiscontinuousIntervals)
	{
		if(multiple)
		{
			if(allowDiscontinuousIntervals)
			{
				return ListSelectionModel.MULTIPLE_INTERVAL_SELECTION;
			}
			else
			{
				return ListSelectionModel.SINGLE_INTERVAL_SELECTION;
			}
		}
		else
		{
			return ListSelectionModel.SINGLE_SELECTION;
		}
	}


	public void valueChanged(ListSelectionEvent ev)
	{
		if(handleEvents)
		{
			if(!ev.getValueIsAdjusting())
			{
				// selection model may contain rows even when the table is empty
				if(table.getRowCount() == 0)
				{
					selected = NONE;
				}
				else
				{
					selected = table.getSelectedRows();
				}

				try
				{
					tableSelectionChangeDetected();
				}
				catch(Exception e)
				{
					Log.err(e);
				}
			}
		}
	}
	
	
	public int size()
	{
		return selected.length;
	}
	
	
	public boolean isTableEmpty()
	{
		return table.getModel().getRowCount() == 0;
	}
	
	
	public boolean isTableNotEmpty()
	{
		return !isTableEmpty();
	}
	
	
	public boolean isEmpty()
	{
		return selected.length == 0;
	}
	
	
	public boolean isNotEmpty()
	{
		return selected.length != 0;
	}
	
	
	public boolean isSingle()
	{
		return selected.length == 1;
	}
	
	
	public boolean isDouble()
	{
		return selected.length == 2;
	}
	
	
	public boolean isMultiple()
	{
		return selected.length > 1;
	}
	
	
	public int[] getSelectedViewRows()
	{
		return selected;
	}
	
	
	public int[] getSelectedModelRows()
	{
		int sz = size();
		int[] mr = new int[sz];
		for(int i=0; i<sz; i++)
		{
			mr[i] = table.convertRowIndexToModel(selected[i]);
		}
		return mr;
	}
	
	
	public int getSelectedViewRow()
	{
		if(isNotEmpty())
		{
			return selected[0];
		}
		else
		{
			return -1;
		}
	}
	
	
	public int[] getSelectedViewColumns()
	{
		return table.getSelectedColumns();
	}
	
	
	public int getSelectedModelRow()
	{
		if(isNotEmpty())
		{
			return table.convertRowIndexToModel(selected[0]);
		}
		else
		{
			return -1;
		}
	}
	
	
	public int getSelectedModelColumn()
	{
		if(isNotEmpty())
		{
			return table.convertColumnIndexToModel(table.getSelectedColumn());
		}
		else
		{
			return -1;
		}
	}
	
	
	public int[] getSelectedModelColumns()
	{
		int[] cols = table.getSelectedColumns();
		for(int i=0; i<cols.length; i++)
		{
			cols[i] = table.convertColumnIndexToModel(cols[i]);
		}
		return cols;
	}
	
	
	public void clearSelection()
	{
		table.clearSelection();
	}
	
	
	public void addSelectedModelRow(int ix)
	{
		int r = table.convertRowIndexToView(ix);
		table.changeSelection(r, 0, false, true);
	}
	
	
	public boolean setSelectedRow(int viewRow)
	{
		if((viewRow >= 0) && (viewRow < table.getRowCount()))
		{
			table.changeSelection(viewRow, 0, false, false);
			return true;
		}
		return false;
	}
	
	
	public void selectFirstRow()
	{
		if(table.getRowCount() > 0)
		{
			setSelectedRow(0);
		}
	}
	
	
	public void selectLastRow()
	{
		int sz = table.getRowCount();
		if(sz > 0)
		{
			setSelectedRow(sz - 1);
		}
	}
	
	
	public void ensureSelection()
	{
		if(isEmpty())
		{
			selectFirstRow();
		}
	}
	
	
	public void setSelectedViewRows(int[] viewRows)
	{
		boolean first = true;
		for(int i: viewRows)
		{
			if(first)
			{
				setSelectedRow(i);
				first = false;
			}
			else
			{
				addSelectedModelRow(i);
			}
		}
	}
	
	
	public void setSelectedModelRows(int[] modelRows)
	{
		boolean first = true;
		for(int i: modelRows)
		{
			int ix = table.convertRowIndexToView(i);
			if(ix >= 0)
			{
				if(first)
				{
					setSelectedRow(ix);
					first = false;
				}
				else
				{
					addSelectedModelRow(ix);
				}
			}
		}
	}
	
	
	public void setSelectedModelRows(List<Integer> modelRows)
	{
		boolean first = true;
		for(int i: modelRows)
		{
			int ix = table.convertRowIndexToView(i);
			if(ix >= 0)
			{
				if(first)
				{
					setSelectedRow(ix);
					first = false;
				}
				else
				{
					addSelectedModelRow(ix);
				}
			}
		}
	}
	
	
	public boolean setSelectedModelRow(int modelRow)
	{
		if(modelRow >= 0)
		{
			int ix = table.convertRowIndexToView(modelRow);
			if(ix >= 0)
			{
				if(ix >= table.getRowCount())
				{
					ix = table.getRowCount() - 1;
					if(ix < 0)
					{
						return false;
					}
				}
				return setSelectedRow(ix);
			}
		}
		return false;
	}
	
	
	public String toString()
	{
		return "CTableSelector(" + size() + ")";
	}


	/** save current selection by remembering values in the selected cell(s) of the specified column(s) */
	public void saveSelection(int modelColumnIndex)
	{
		int col = table.convertColumnIndexToView(modelColumnIndex);
		int[] indexes = getSelectedViewRows();
		int sz = indexes.length;
		Object[] sel = new Object[sz];
		
		for(int i=0; i<sz; i++)
		{
			Object v = table.getValueAt(indexes[i], col);
			sel[i] = v;
		}
		
		selection = new Selection(col, sel);
	}
	
	
	protected int indexOf(int viewColumn, Object val)
	{
		int sz = table.getRowCount();
		for(int i=0; i<sz; i++)
		{
			Object v = table.getValueAt(i, viewColumn);
			if(CKit.isNotBlank(v))
			{
				if(CKit.equals(v, val))
				{
					return i;
				}
			}
		}
		
		return -1;
	}
	
	
	/** restore previous selection or select first row */
	public void restoreSelection()
	{
		try
		{
			if(table.getRowCount() > 0)
			{
				if(selection != null)
				{
					CList<Integer> rows = new CList();
					
					for(int i=0; i<selection.sel.length; i++)
					{
						Object v = selection.sel[i];
						int ix = indexOf(selection.viewColumn, v);
						if(ix >= 0)
						{
							rows.add(ix);
						}
					}
					
					if(rows.size() > 0)
					{
						setSelectedModelRows(rows);
						return;
					}
				}
				
				selectFirstRow();
			}
		}
		finally
		{
			selection = null;
		}
	}
	
	
	//
	
	
	public static class Selection
	{
		public final int viewColumn;
		public final Object[] sel;
		
		
		public Selection(int viewColumn, Object[] sel)
		{
			this.viewColumn = viewColumn;
			this.sel = sel;
		}
	}
}
