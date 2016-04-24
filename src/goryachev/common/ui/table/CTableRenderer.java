// Copyright (c) 2006-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.table;
import goryachev.common.ui.UI;
import goryachev.common.util.Log;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


// renderer for CTable
public class CTableRenderer<T>
	extends DefaultTableCellRenderer
{
	private JTable table;
	private double selectedMix = 0.28125;
	private double selectedFocusedMix = 0.8203125;
	protected int row;
	protected int column;
	public final CTableRendererBorder BORDER = new CTableRendererBorder();
	
	
	public CTableRenderer()
	{
		setHorizontalAlignment(LEFT);
		setBorder(BORDER);
		UI.disableHtml(this);
	}
	
	
	public void setIndent(int x)
	{
		BORDER.setGapLeft(x);
	}
	
	
	public void renderCell(T item, Object value)
	{
		if(value instanceof Icon)
		{
			setIcon((Icon)value);
			setText(null);
		}
		else
		{
			setText(value == null ? null : value.toString());
		}
	}
	
	
	protected void renderCellComplete(T item, boolean selected, boolean focus)
	{
		// TODO color
		// TODO before cell or after cell?
		JTable.DropLocation d = table.getDropLocation();
		if(d != null && !d.isInsertRow() && !d.isInsertColumn() && d.getRow() == row && d.getColumn() == column)
		{
//			fg = DefaultLookup.getColor(this, ui, "Table.dropCellForeground");
//			bg = DefaultLookup.getColor(this, ui, "Table.dropCellBackground");

			selected = true;
		}

		BORDER.setFocused(focus);
		
		setToolTipText(null);
		setBackground(table.getBackground());
		setForeground(null);
		setFont(table.getFont());
		setIcon(null);
		
//		renderCell(item, value);
	}
	
	
	public Component getTableCellRendererComponent(JTable t, Object value, boolean selected, boolean focus, int row, int col)
	{
		try
		{
//			if(t instanceof CTable)
//			{
//				this.row = row;
//				this.column = col;
//				this.table = t;
//				
//				CTable<T> ta = (CTable<T>)t;
//				T item = ta.getItem(row);
//				
//				renderCellComplete(item, selected, focus);
//				
//				int mcol = table.convertColumnIndexToModel(col);
//				CTableColumn tc = ta.getCModelAdapter().getCTableColumn(mcol);
//				setHorizontalAlignment(tc.getHorizontalAlignment());
//				
//				if(selected)
//				{
//					setForeground(table.getSelectionForeground());
//					Color bg = isBackgroundSet() ? getBackground() : table.getBackground();
//					if(focus)
//					{
//						setBackground(CKit.mix(selectedFocusedMix, table.getSelectionBackground(), bg));
//					}
//					else
//					{
//						setBackground(CKit.mix(selectedMix, table.getSelectionBackground(), bg));
//					}
//				}
//				else
//				{
//					setBackground(table.getBackground());
//					setForeground(table.getForeground());
//				}
//				
//				tc.configureRenderer(item);
//				
//				renderCell(item, value);
//				
//				// TODO highlight
//	//			CFilterLogic filter = CFilterLogic.get(table);
//	//			if(filter != null)
//	//			{
//					// which column?
//					// highlight depends on expression and options 
//	//			}
//				
//				table = null;
//			}
//			else 
			if(t instanceof CTreeTable)
			{
				this.row = row;
				this.column = col;
				this.table = t;
				
				JTable.DropLocation d = table.getDropLocation();
				if(d != null && !d.isInsertRow() && !d.isInsertColumn() && (d.getRow() == row) && (d.getColumn() == col))
				{
					mixBackground(Color.black, 0.1875);
					selected = true;
					// fg = DefaultLookup.getColor(this, ui, "Table.dropCellForeground");
					// bg = DefaultLookup.getColor(this, ui, "Table.dropCellBackground");
				}
				
				CTreeTable ta = (CTreeTable)t;
				T item = (T)ta.getTreeEntry(row).getItem();
								
				renderCellComplete(item, selected, focus);
								
				if(selected)
				{
					Color bg = isBackgroundSet() ? getBackground() : table.getBackground();
					setForeground(table.getSelectionForeground());
					if(focus)
					{
						setBackground(UI.mix(table.getSelectionBackground(), selectedFocusedMix, bg));
					}
					else
					{
						setBackground(UI.mix(table.getSelectionBackground(), selectedMix, bg));
					}
				}
				else
				{
					setBackground(table.getBackground());
					setForeground(table.getForeground());
				}
				
				CTableColumn tc = ta.getCTableColumn(col);
				setHorizontalAlignment(tc.getHorizontalAlignment());
				tc.configureRenderer(item);

				renderCell(item, value);
				
				// TODO highlight
	//			CFilterLogic filter = CFilterLogic.get(table);
	//			if(filter != null)
	//			{
					// which column?
					// highlight depends on expression and options 
	//			}
				
				table = null;
			}
			else
			{
				super.getTableCellRendererComponent(t, value, selected, focus, row, col);
			}
		}
		catch(Exception e)
		{
			Log.err(e);
		}
		return this;
	}
	
	
	protected void mixForeground(Color c, double fraction)
	{
		if(c != null)
		{
			setForeground(UI.mix(c, fraction, getForeground()));
		}
	}
	
	
	protected void mixBackground(Color c, double fraction)
	{
		if(c != null)
		{
			setBackground(UI.mix(c, fraction, getBackground()));
		}
	}
	
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		paintOverlay((Graphics2D)g);
	}
	
	
	// override to paint over
	public void paintOverlay(Graphics2D g)
	{
	}
	
	
	public void setHtmlEnabled(boolean on)
	{
		UI.setHtmlEnabled(this, on);
	}
	

	@Deprecated // replaced by new method
	public final void setCell(Object value)
	{
	}
	
	
	public void setValue(Object x)
	{
		setText(x == null ? null : x.toString());
	}
	
	
	public void alignLeft()
	{
		setHorizontalAlignment(LEFT);
	}
	
	
	public void alignRight()
	{
		setHorizontalAlignment(RIGHT);
	}
	
	
	public void alignCenter()
	{
		setHorizontalAlignment(CENTER);
	}
	
	
	public void setDrawFocus(boolean on)
	{
		BORDER.setFocusEnabled(on);
	}
	
	
	// valid only inside of certain methods
	protected int getRow()
	{
		return row;
	}
}
