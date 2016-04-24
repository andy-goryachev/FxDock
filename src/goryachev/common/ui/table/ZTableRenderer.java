// Copyright (c) 2006-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.table;
import goryachev.common.ui.CAlignment;
import goryachev.common.ui.UI;
import goryachev.common.util.Log;
import goryachev.common.util.Parsers;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;


public class ZTableRenderer
	extends DefaultTableCellRenderer
{
	protected CTableRendererBorder border = createDefaultBorder();
	protected transient JTable table;
	protected transient ElasticColumnHandler handler;
	protected transient Object value;
	protected transient int row;
	protected transient ZTableModelCommon model;
	private double selectedMix = 0.4;
	private double selectedFocusedMix = 0.9;
	
	
	public ZTableRenderer()
	{
		setHorizontalAlignment(LEADING);
		setVerticalAlignment(TOP);
		UI.disableHtml(this);
	}
	

	public static CTableRendererBorder createDefaultBorder()
	{
		return new CTableRendererBorder();
	}
	

	public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean focus, int row, int col)
	{
		this.row = row;
		this.table = t;
		
		try
		{
			if(t instanceof ZTable)
			{
				this.value = val;

				border.setFocused(focus);
				setBorder(border);

				setFont(table.getFont());
				
				// colors
				
				setBackground(table.getBackground());
				setForeground(null);
				
				JTable.DropLocation d = table.getDropLocation();
				if(d != null && !d.isInsertRow() && !d.isInsertColumn() && (d.getRow() == row) && (d.getColumn() == col))
				{
					mixBackground(Color.black, 0.18823);
					sel = true;
					// fg = DefaultLookup.getColor(this, ui, "Table.dropCellForeground");
					// bg = DefaultLookup.getColor(this, ui, "Table.dropCellBackground");
				}

				if(sel)
				{
					setForeground(table.getSelectionForeground());
					
					Color bg = isBackgroundSet() ? getBackground() : table.getBackground();
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
				
				// icon

				Icon ic = Parsers.parseIcon(val);
				setIcon(ic);
				
				// alignment
				
				setHorizontalAlignment(LEADING);
				setHorizontalTextPosition(TRAILING);
				
				// text
				
				String text;
				String tooltip = null;
				TableModel m = table.getModel();
				if(m instanceof ZTableModelCommon)
				{						
					model = (ZTableModelCommon)m;
					int mcol = table.convertColumnIndexToModel(col);
					ZColumnInfo tc = model.getColumnInfo(mcol);

					CAlignment a = tc.align;
					if(a != null)
					{
						setHorizontalAlignment(a.getAlignment());
					}
					
					handler = tc.handler; 
					if(handler != null)
					{
						text = handler.getText(val);
						handler.decorate(val, this);
						tooltip = handler.getToolTip(val);
					}
					else if(ic != null)
					{
						text = null;
					}
					else
					{
						text = Parsers.parseString(val);
					}
				}
				else if(ic != null)
				{
					text = null;
				}
				else
				{
					text = Parsers.parseString(val);
				}
				
				setText(text);
				
				// tooltip
				
				setToolTipText(tooltip);

				table = null;
			}
			else
			{
				super.getTableCellRendererComponent(t, val, sel, focus, row, col);
			}
		}
		catch(Exception e)
		{
			Log.err(e);
		}
		return this;
	}

	
	public void setIndent(int x)
	{
		border.setGapLeft(x);
	}
	
	
	public void mixForeground(Color c, double fraction)
	{
		if(c != null)
		{
			setForeground(UI.mix(c, fraction, getForeground()));
		}
	}
	
	
	public void mixBackground(Color c, double fraction)
	{
		if(c != null)
		{
			setBackground(UI.mix(c, fraction, getBackground()));
		}
	}
	
	
	public void setHtmlEnabled(boolean on)
	{
		UI.setHtmlEnabled(this, on);
	}
	

	protected void paintComponent(Graphics g)
	{
		if(ui != null)
		{
			Graphics g2 = g.create();
			try
			{
				if(isOpaque())
				{
					g.setColor(getBackground());
					g.fillRect(0, 0, getWidth(), getHeight());
				}
				
				// custom background
				if(handler != null)
				{
					handler.paintBackground(value, this, g);
				}

				ui.paint(g, this);
			}
			finally
			{
				g2.dispose();
			}
		}
	}
	
	
	public int getViewRow()
	{
		return row;
	}
	
	
	public int getModelRow()
	{
		return table.convertRowIndexToModel(row);
	}
}
