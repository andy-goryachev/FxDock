// Copyright (c) 2010-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.table;
import goryachev.common.util.CList;
import goryachev.common.util.CSorter;
import goryachev.common.util.Log;
import java.awt.Rectangle;
import java.util.Comparator;
import java.util.List;
import javax.swing.SortOrder;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.table.TableStringConverter;


public class ZTableRowSorter
	extends TableRowSorter
{
	private final ZTable table;
	private Comparator[] comparators;
	private static Comparator<Object> betterComparator = CSorter.comparator();


	public ZTableRowSorter(ZTable t, int maxSortKeys)
	{
		super(t.getModel());
		this.table = t;
		setMaxSortKeys(maxSortKeys);

//		CTable.CModelAdapter m = table.getModelAdapter();
//		int sz = m.getColumnCount();
//		comparators = new Comparator[sz];
//		for(int i = 0; i < sz; i++)
//		{
//			comparators[i] = m.getCTableColumn(i).getSorter();
//		}
	}
	
	
	public Comparator<?> getComparator(int col)
	{
		Comparator<?> c = (comparators == null ? null : comparators[col]);
		if(c == null)
		{
			// standard DefaultRowSorter can't sort primitive types
			c = betterComparator;
		}
		return c;
	}


	protected boolean useToString(int column)
	{
		return (getComparator(column) == null);
	}


	public void rowsInserted(int firstRow, int endRow)
	{
		try
		{
			super.rowsInserted(firstRow, endRow);
		}
		catch(IndexOutOfBoundsException e)
		{ 
			// weird error
			Log.err(e);
		}
		catch(Exception e)
		{
			Log.err(e);
		}
	}


	public void rowsUpdated(int firstRow, int endRow)
	{
		try
		{
			super.rowsUpdated(firstRow, endRow);
		}
		catch(IndexOutOfBoundsException e)
		{ 
			// weird error
		}
		catch(Exception e)
		{
			Log.err(e);
		}
	}


	public void setModel(TableModel m)
	{
		super.setModel(m);
		setModelWrapper(new Wrapper(m));
	}
	
	
	public void toggleSortOrder(int col)
	{
		TableModel m = table.getModel();
		if(m instanceof ZTableModelCommon)
		{
			//ZColumnInfo tc = ((ZTableModelCommon)m).getColumnInfo(col);
			//if(tc.sortable)
			{
				List<SortKey> keys = new CList<SortKey>(getSortKeys());
				SortKey sortKey;
				int sortIndex;
				for(sortIndex = keys.size() - 1; sortIndex >= 0; sortIndex--)
				{
					if(keys.get(sortIndex).getColumn() == col)
					{
						break;
					}
				}
	
				if(sortIndex == -1)
				{
					// key doesn't exist
					sortKey = new SortKey(col, SortOrder.ASCENDING);
					keys.add(0, sortKey);
				}
				else if(sortIndex == 0)
				{
					// it's the primary sorting key, toggle it: none -> ASCENDING -> DESCENDING -> none
					SortKey key = keys.get(0);
					switch(key.getSortOrder())
					{
					case ASCENDING:
						keys.set(0, new SortKey(key.getColumn(), SortOrder.DESCENDING));
						break;
					default:
						keys.remove(0);
						break;
					}
				}
				else
				{
					// it's not the first, but was sorted on, remove old entry, insert as first with ascending.
					keys.remove(sortIndex);
					keys.add(0, new SortKey(col, SortOrder.ASCENDING));
				}
	
				if(keys.size() > getMaxSortKeys())
				{
					keys = keys.subList(0, getMaxSortKeys());
				}
	
				setSortKeys(keys);
	
				// scroll to rect if single line is selected
				int row = table.getSelectedRow();
				if(row >= 0)
				{
					Rectangle r = table.getCellRect(row, col, true);
					table.scrollRectToVisible(r);
				}
			}
		}
	}
	
	
	//
	
	
	// replaces TableRowSorter.TableRowSorterModelWrapper
	public class Wrapper
		extends ModelWrapper<TableModel,Integer>
	{
		TableModel model;
		
		
		public Wrapper(TableModel m)
		{
			model = m;
		}
		
		
		public TableModel getModel()
		{
			return model;
		}


		public int getColumnCount()
		{
			return (model == null) ? 0 : model.getColumnCount();
		}


		public int getRowCount()
		{
			return (model == null) ? 0 : model.getRowCount();
		}


		public Object getValueAt(int row, int col)
		{
			return model.getValueAt(row, col);
		}


		public String getStringValueAt(int row, int column)
		{
			TableStringConverter converter = getStringConverter();
			if(converter != null)
			{
				// Use the converter
				String value = converter.toString(model, row, column);
				if(value != null)
				{
					return value;
				}
				return "";
			}

			// No converter, use getValueAt followed by toString
			Object o = getValueAt(row, column);
			if(o == null)
			{
				return "";
			}
			String string = o.toString();
			if(string == null)
			{
				return "";
			}
			return string;
		}


		public Integer getIdentifier(int index)
		{
			return index;
		}
	}
}