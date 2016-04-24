// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.table;
import goryachev.common.ui.Theme;
import goryachev.common.util.Parsers;
import java.awt.Graphics;
import javax.swing.Icon;


// performs per-column data processing
public class ElasticColumnHandler
{
	public ElasticColumnHandler()
	{
	}


	/** converts binary value in a table cell to text */
	public String getText(Object x)
	{
		if(x instanceof Icon)
		{
			return null;
		}
		return Parsers.parseString(x);
	}
	

	/* decorates cell: icon, background, etc. */
	public void decorate(Object x, ZTableRenderer r)
	{
	}
	
	
	/** makes changes to the table header renderer after it has been configured by the regular code */
	public void decorateHeader(CTableHeaderRenderer r)
	{
	}
	
	
	public String getToolTip(Object val)
	{
		return null;
	}


	public void paintBackground(Object val, ZTableRenderer r, Graphics g)
	{
	}
	
	
	/** convenience method */
	public String formatNumber(Object x)
	{
		return Theme.formatNumber(x);
	}
	
	
	/** convenience method */
	public String formatDateTime(Object x)
	{
		return Theme.formatDateTime(x);
	}
	
	
	/** convenience method */
	public String formatTime(Object x)
	{
		return Theme.formatTime(x);
	}
	
	
	/** convenience method */
	public String formatDate(Object x)
	{
		return Theme.formatDate(x);
	}
	
	
	/** convenience method */
	public String formatPercent(Object x)
	{
		return Theme.formatPercent(x);
	}
	
	
	public String formatString(Object x)
	{
		if(x instanceof Icon)
		{
			return null;
		}
		else if(x != null)
		{
			return x.toString();
		}
		return null;
	}
}
