// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.table;
import goryachev.common.util.CList;


public class ZBaseTableModelCommon
	extends ZTableModelCommon
{
	private CList<Object[]> rows = new CList();

	
	public ZBaseTableModelCommon()
	{
	}
	
	
	protected void clearNoEvents()
	{
		rows.clear();
	}


	protected void removeNoEvents(int ix)
	{
		rows.remove(ix);
	}


	public void removeNoEvents(int first, int lastExclusive)
	{
		rows.removeRange(first, lastExclusive);
	}


	protected CList<Object[]> rows()
	{
		return rows;
	}
	
	
	public int getRowCount()
	{
		return rows().size();
	}
	
	
	public Object getValueAt(int row, int col)
	{
		Object[] r = rows().get(row);
		if(r != null)
		{
			if(col < r.length)
			{
				return r[col];
			}
		}
		return null;
	}
}
