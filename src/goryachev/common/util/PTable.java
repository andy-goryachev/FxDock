// Copyright © 2011-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


/** 
 * A relatively small (in-memory) elastic data table.
 * This class does not allow for any changes to column structure.
 */
public class PTable
{
	private CList<Object> columns = new CList();
	private CList<Object[]> rows = new CList();
	
	
	public PTable()
	{
	}
	
	
	public PTable(Object[] columns)
	{
		for(Object c: columns)
		{
			addColumn(c);
		}
	}
	
	
	public int getColumnCount()
	{
		return columns.size();
	}
	
	
	public void addColumn(Object name)
	{
		columns.add(name);
	}
	
	
	public void addColumn()
	{
		addColumn(null);
	}
	
	
	public String getColumnName(int ix)
	{
		return Parsers.parseStringNotNull(columns.get(ix));
	}
	
	
	public Object getColumn(int ix)
	{
		if((ix >= 0) && (ix < columns.size()))
		{
			return columns.get(ix);
		}
		return null;
	}
	
	
	public void setColumnName(int ix, String name)
	{
		ensureColumn(ix);
		columns.set(ix, name);
	}
	
	
	protected void ensureColumn(int col)
	{
		while(getColumnCount() <= col)
		{
			addColumn();
		}
	}
	
	
	public Object[] getColumns()
	{
		return columns.toArray();
	}
	
	
	public int indexOfColumn(Object x)
	{
		return columns.indexOf(x);
	}
	
	
	public int getRowCount()
	{
		return rows.size();
	}
	
	
	public void addCells(Object ... cells)
	{
		int r = getRowCount();
		for(int c=0; c<cells.length; c++)
		{
			Object v = cells[c];
			setValueAt(r, c, v);
		}
	}
	
	
	public void addRow(Object[] cells)
	{
		int r = getRowCount();
		for(int c=0; c<cells.length; c++)
		{
			Object v = cells[c];
			setValueAt(r, c, v);
		}
	}
	
	
	public Object setValueAt(int row, int col, Object value)
	{
		ensureColumn(col);
		
		Object[] r = ensureRow(row);
		Object old = r[col];
		r[col] = value;
		return old;
	}
	
	
	public Object getValueAt(int row, int col)
	{
		if(row < getRowCount())
		{
			Object[] r = rows.get(row);
			if(r != null)
			{
				if(col < r.length)
				{
					return r[col];
				}
			}
		}
		return null;
	}
		
	
	protected Object[] ensureRow(int row)
	{
		while(getRowCount() <= row)
		{
			rows.add(null);
		}
		
		Object[] r = rows.get(row);
		if(r == null)
		{
			r = new Object[getColumnCount()];
			rows.set(row, r);
		}
		else
		{
			if(r.length < getColumnCount())
			{
				Object[] old = r;
				r = new Object[getColumnCount()];
				System.arraycopy(old, 0, r, 0, old.length);
				rows.set(row, r);
			}
		}
		
		return r;
	}
	
	
	public void insertRow(int row)
	{
		if(getRowCount() <= row)
		{
			ensureRow(row);
		}
		else
		{
			rows.add(row, null);
		}
	}
	
	
	public void removeRow(int row)
	{
		rows.remove(row);
	}
	
	
	public void removeRows(int startInclusive, int endExclusive)
	{
		rows.removeRange(startInclusive, endExclusive);
	}
	
	
	public void addAll(Object[][] vs)
	{
		if(vs != null)
		{
			int start = getRowCount();
			for(int r=0; r<vs.length; r++)
			{
				Object[] vss = vs[r];
				int ix = r + start;
				//ensureRow(ix);
				
				if(vss != null)
				{
					for(int c=0; c<vss.length; c++)
					{
						setValueAt(ix, c, vss[c]);
					}
				}
			}
		}
	}
	
	
	public boolean equals(Object x)
	{
		if(x == this)
		{
			return true;
		}
		else if(x instanceof PTable)
		{
			PTable p = (PTable)x;
			if(getRowCount() == p.getRowCount())
			{
				if(getColumnCount() == p.getColumnCount())
				{
					for(int c=getColumnCount()-1; c>=0; --c)
					{
						Object a = getColumn(c);
						Object b = p.getColumn(c);
						if(CKit.notEquals(a, b))
						{
							return false;
						}
					}
					
					for(int r=getRowCount()-1; r>=0; --r)
					{
						for(int c=getColumnCount()-1; c>=0; --c)
						{
							Object a = getValueAt(r, c);
							Object b = p.getValueAt(r, c);
							if(CKit.notEquals(a, b))
							{
								return false;
							}
						}
					}
					return true;
				}
			}
		}

		return false;
	}
	
	
	public int hashCode()
	{
		int h = PTable.class.hashCode();
		
		for(int c=getColumnCount()-1; c>=0; --c)
		{
			Object a = getColumn(c);
			h = FH.hash(h, a);
		}
		
		for(int r=getRowCount()-1; r>=0; --r)
		{
			for(int c=getColumnCount()-1; c>=0; --c)
			{
				Object a = getValueAt(r, c);
				h = FH.hash(h, a);
			}
		}
		
		return h;
	}
}
