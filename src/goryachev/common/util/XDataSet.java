// Copyright © 2009-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.util.Arrays;


public class XDataSet
{
	protected CList<Object[]> data = new CList();
	private int width;
	
	
	public XDataSet()
	{
	}
	
	
	public XDataSet(XDataSet x)
	{
		width = x.width;
		int sz = x.getRowCount();
		for(int i=0; i<sz; i++)
		{
			Object[] rs = x.data.get(i);
			data.add(rs == null ? null : Arrays.copyOf(rs, width));
		}
	}
	
	
	public int getColumnCount()
	{
		return width;
	}
	
	
	public int getRowCount()
	{
		return data.size();
	}
	
	
	public void addRow(Object ... xs)
	{
		if(width < xs.length)
		{
			width = xs.length;
		}
		data.add(Arrays.copyOf(xs, xs.length));
	}

	
	public Object get(int row, int col)
	{
		if(row < data.size())
		{
			Object[] r = data.get(row);
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
	
	
	public void set(int row, int col, Object value)
	{
		while(row >= data.size())
		{
			data.add(null);
		}
		
		Object[] r = data.get(row);
		if(r == null)
		{
			r = new Object[col+8];
			data.set(row, r);
		}
		else if(col >= r.length)
		{
			r = Arrays.copyOf(r, col+8);
			data.set(row, r);
		}
		
		r[col] = value;
		
		if(col >= width)
		{
			width = col+1;
		}
	}
	
	
	public String dump()
	{
		SB sb = new SB();
		for(int r=0; r<getRowCount(); r++)
		{
			for(int c=0; c<getColumnCount(); c++)
			{
				if(c > 0)
				{
					sb.a(',');
				}
				sb.a(get(r,c));
			}
			sb.a('\n');
		}
		return sb.toString();
	}
}
