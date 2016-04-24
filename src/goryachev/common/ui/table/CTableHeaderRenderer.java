// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.table;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import goryachev.common.ui.icons.CIcons;
import goryachev.common.ui.theme.AgTableHeaderUI;
import goryachev.common.ui.theme.GradientPainter;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableCellRenderer;


public class CTableHeaderRenderer 
	extends DefaultTableCellRenderer
{
	public static final CBorder BORDER = new CBorder(2,4,2,4); 	
	private boolean sorted;
	

	public CTableHeaderRenderer()
	{
		setHorizontalAlignment(LEADING);
		setHorizontalTextPosition(LEADING);
		setIconTextGap(1);
		setOpaque(false);
	}


	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col)
	{
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
		
		boolean highlight = (table.getColumnModel().getSelectionModel().getLeadSelectionIndex() == col);
		
		Color c = null;
		if(highlight)
		{
			c = UI.getTableHeaderHighlight(table);
		}
		if(c == null)
		{
			c = Theme.PANEL_BG;
		}
		setBackground(c);

		Icon sortIcon = null;
		sorted = false;
		
		SortOrder sortOrder = AgTableHeaderUI.getColumnSortOrder(table, col);
		if(sortOrder != null)
		{
			switch(sortOrder)
			{
			case ASCENDING:
				sortIcon = CIcons.SortAscending;
				sorted = true;
				break;
			case DESCENDING:
				sortIcon = CIcons.SortDescending;
				sorted = true;
				break;
			}
		}

		setFont(table.getTableHeader().getFont());
		setIcon(sortIcon);
		setBorder(BORDER);
		
//		if(table instanceof CTable)
//		{
//			CTableColumn tc = ((CTable)table).getCTableColumn(col);
//			setHorizontalAlignment(tc.getHorizontalAlignment());
//			tc.configureHeaderRenderer(this);
//		}
//		else 
		if(table instanceof CTreeTable)
		{
			CTableColumn tc = ((CTreeTable)table).getCTableColumn(col);
			setHorizontalAlignment(tc.getHorizontalAlignment());
			tc.configureHeaderRenderer(this);
		}
		else if(table instanceof ZTable)
		{
			ZColumnInfo tc = ((ZTable)table).getColumnInfo(col);
			if(tc != null)
			{
				setHorizontalAlignment(tc.align == null ? LEFT : tc.align.getAlignment());
				ElasticColumnHandler h = tc.handler;
				if(h != null)
				{
					h.decorateHeader(this);
				}
			}
		}
		
		return this;
	}


	public void paint(Graphics g)
	{
		int w = getWidth();
		int h = getHeight();
	
		Color bg = getBackground();
		Color top = Theme.brighter(bg);
		Color bottom = Theme.darker(bg);
		
		if(sorted)
		{
			g.setColor(top);
			g.fillRect(0,0,w,h);
			g.setColor(bg);
			g.drawLine(0,h-1,w-1,h-1);
		}
		else
		{
			paintGradient(g,w,h,bg,top,bottom);
		}
		
		g.setColor(top);
		g.drawLine(0,0,0,h-1);
		g.setColor(bottom);
		g.drawLine(w-1,0,w-1,h-1);

		super.paint(g);
	}
	
	
	protected void paintGradient(Graphics g, int w, int h, Color bg, Color top, Color bottom)
	{
		GradientPainter.paintVertical(g, 0, 0, w, h, bg, 50, top, 0, bottom);
	}
}