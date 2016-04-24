// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.table;
import java.awt.Graphics;


/** provides per-column data processing for row-based table model */
public abstract class ZColumnHandler<T>
	extends ElasticColumnHandler
{
	public ZColumnHandler()
	{
	}
	
	
	/** converts binary value in a table cell to text */
	public String getText(Object x)
	{
		return formatString(x);
	}


	/* extracts cell binary value from a table row object */
	public abstract Object getCellValue(T x);
	
	
	/* override to supply a different search string */
	public String getSearchString(T x, Object cell) { return formatString(cell); }
	public final String getSearchString(T x) { return null; }
	
	
	/** override to provide fine-grained control, also implement setCellValue() */
	public boolean isCellEditable(T x) { return false; }
	
	
	/** override to provide editing capability */
	public void setCellValue(T x, Object value) { }
	
	
	/* decorates cell: icon, background, etc. */
	public void decorate(Object x, ZTableRenderer r) { }
	
	
	/** override to paint custom background (static) */
	public void paintBackground(Object val, ZTableRenderer r, Graphics g) { }
}
