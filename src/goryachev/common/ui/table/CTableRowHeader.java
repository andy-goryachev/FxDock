// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.table;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.Theme;
import goryachev.common.ui.ThemeKey;
import goryachev.common.ui.UI;
import goryachev.common.ui.theme.ThemeColor;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;


public class CTableRowHeader
	extends JPanel 
	implements ListSelectionListener
{
	private JTable table;
	private Color highlight;
	private static JLabel renderer;
	public static final Border BORDER = new CBorder(1,3);
	public static final Color BG = ThemeColor.shadow(ThemeKey.TEXT_BG, 0.2);


	public CTableRowHeader(JTable t)
	{
		this.table = t;
		t.getSelectionModel().addListSelectionListener(this);
		highlight = UI.getTableHeaderHighlight(t);
		setBackground(BG);
	}
	
	
	public Dimension getPreferredSize()
	{
		if(table == null)
		{
			return new Dimension(20, 10);
		}
		else
		{
			setupRenderer();

			// compute preferred size roughly based on the number of table rows
			String tx;
			int sz = table.getRowCount();
			if(sz < 10000)
			{
				tx = "0000";
			}
			else if(sz < 100000000)
			{
				tx = "00000000";
			}
			else
			{
				tx = "0000000000";
			}
			
			renderer.setText(tx);
			int w = renderer.getPreferredSize().width + 1;
			int h = table.getPreferredSize().height;
			return new Dimension(w, h);
		}
	}
	
	
	protected String getTextForRow(int row)
	{
		return Theme.formatNumber(row+1);
	}


	protected JLabel createRenderer()
	{
		JLabel r = new DefaultTableCellRenderer();
		r.setOpaque(false);
		r.setBorder(BORDER);
		r.setHorizontalAlignment(JLabel.RIGHT);
		return r;
	}
	
	
	protected void setupRenderer()
	{
		if(renderer == null)
		{
			renderer = createRenderer();
		}
		
		if(table != null)
		{
			// I don't think this is correct
			renderer.setFont(table.getFont());
			renderer.setForeground(table.getForeground());
		}
	}


	public void paintComponent(Graphics gr)
	{
		Graphics2D g = (Graphics2D) gr;
		Rectangle clip = g.getClipBounds();
		
		g.setColor(getBackground());
		g.fill(clip);
		
		int row = table.rowAtPoint(new Point(0, clip.y));
		if(row < 0)
		{
			return;
		}

		int max = clip.y + clip.height;
		int rows = table.getRowCount();
		int focus = (highlight == null ? -1 : table.getSelectionModel().getLeadSelectionIndex());

		setupRenderer();

		int y;
		int sep = table.getIntercellSpacing().height;
		Color sepColor = table.getGridColor(); 

		do
		{
			Rectangle r = table.getCellRect(row, 0, false);
			int h = table.getRowHeight(row);
			y = r.y;
			
			renderer.setText(getTextForRow(row));
			renderer.setBounds(0, 0, getWidth(), h-1);
			
			AffineTransform tr = g.getTransform();
			g.translate(0,y);
			if(row == focus)
			{
				g.setColor(highlight);
				g.fillRect(0, 0, getWidth(), h);
			}
			renderer.paint(g);
			g.setTransform(tr);
			
			y += h;
			
			switch(sep)
			{
			case 1:
				g.setColor(sepColor);
				g.drawLine(0,y-1,getWidth(),y-1);
				y += sep;
				break;
			case 0:
				break;
			default:
				g.setColor(sepColor);
				g.fillRect(0,y-1,getWidth(),y+sep-1);
				y += sep;
				break;
			}

			row++;
			if(row >= rows)
			{
				break;
			}
		} while(y < max);
	}


	public void valueChanged(ListSelectionEvent e)
	{
		// TODO only old/new rows
		repaint();
	}
}
