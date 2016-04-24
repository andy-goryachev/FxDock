// Copyright (c) 2008-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.table;
import goryachev.common.util.CKit;
import goryachev.common.util.Parsers;
import goryachev.common.util.text.ZQuery;
import javax.swing.Icon;
import javax.swing.table.TableModel;


public class ZStringRowFilter
	implements ZRowFilter
{
	private ZQuery query; 
	
	
	public ZStringRowFilter()
	{
		query = null;
	}
	
	
	public ZStringRowFilter(ZStringRowFilter x)
	{
		query = x.query;
	}
	
	
	public ZStringRowFilter copy()
	{
		return new ZStringRowFilter(this);
	}
	
	
	public void setExpression(String s)
	{
		if(CKit.isBlank(s))
		{
			query = null;
		}
		else
		{
			query = new ZQuery(s);
		}
	}
	
	
	protected ZQuery getQuery()
	{
		return query;
	}
	

	public boolean include(TableModel m, int row)
	{
		if(query == null)
		{
			return true;
		}
		
		int sz = m.getColumnCount();
		for(int c=0; c<sz; c++)
		{
			Object v = m.getValueAt(row, c);
			
			ElasticColumnHandler h = null;
			if(m instanceof ZTableModelCommon)
			{
				h = ((ZTableModelCommon)m).getColumnInfo(c).handler;
			}
			
			String s;
			if(h == null)
			{
				if(v instanceof Icon)
				{
					s = null;
				}
				else
				{
					s = Parsers.parseString(v);
				}
			}
			else
			{
				if((h instanceof ZColumnHandler) && (m instanceof ZModel))
				{
					// FIX
					Object x = ((ZModel)m).getItem(row);
					s = ((ZColumnHandler)h).getSearchString(x, v);
				}
				else
				{
					s = h.getText(v);
				}
			}
			
			if(s != null)
			{
				// exclude in any column removes the row from consideration
				if(query.isExcluded(s))
				{
					return false;
				}
				
				// include if any column yields a match
				if(query.isIncluded(s))
				{
					return true;
				}
			}
		}
		return false;
	}
}
