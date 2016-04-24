// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.table;


public class ZRowModel<T>
	extends ZTableModel<T,T>
{
	public ZRowModel()
	{
	}
	
	
	protected T getRowValue(T x)
	{
		return x;
	}
}
