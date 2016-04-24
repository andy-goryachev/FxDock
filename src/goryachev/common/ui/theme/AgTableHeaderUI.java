// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.table.CTableHeaderRenderer;
import java.awt.Graphics;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.UIDefaults;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;


public class AgTableHeaderUI
	extends BasicTableHeaderUI 
	implements ListSelectionListener
{
	private JTable table;
	
	
	public AgTableHeaderUI()
	{
	}
	
	
	public static void init(UIDefaults d)
	{
		d.put("TableHeaderUI", AgTableHeaderUI.class.getName());
	}
	
	
	// gets called by reflection from UIManager.getUI()
	public static ComponentUI createUI(JComponent h)
	{
		return new AgTableHeaderUI();
	}


	public void installUI(JComponent c)
	{
		super.installUI(c);
		header.setDefaultRenderer(createHeaderRenderer());
	}


	public void uninstallUI(JComponent c)
	{
		super.uninstallUI(c);
		table = null;
	}
	
	
	protected TableCellRenderer createHeaderRenderer()
	{
		return new CTableHeaderRenderer();
	}


	protected void rolloverColumnUpdated(int oldColumn, int newColumn)
	{
		header.repaint(header.getHeaderRect(oldColumn));
		header.repaint(header.getHeaderRect(newColumn));
	}


	public static SortOrder getColumnSortOrder(JTable table, int column)
	{
		if(table.getRowSorter() != null)
		{
			List<? extends RowSorter.SortKey> sortKeys = table.getRowSorter().getSortKeys();
			if((sortKeys.size() > 0) && (sortKeys.get(0).getColumn() == table.convertColumnIndexToModel(column)))
			{
				return sortKeys.get(0).getSortOrder();
			}
		}
		return null;
	}
	
	
	protected JTableHeader header()
	{
		return header;
	}


	protected int viewIndexForColumn(TableColumn aColumn)
	{
		if(aColumn != null)
		{
			return header.getTable().convertColumnIndexToView(aColumn.getModelIndex());
		}
		return -1;
	}
	
	
	protected boolean hasRollover(int col)
	{
		return col == getRolloverColumn();
	}


	public void valueChanged(ListSelectionEvent e)
	{
		if(header != null)
		{
			header.repaint();
		}
	}


	public void paint(Graphics g, JComponent c)
	{
		if(table == null)
		{
			table = header.getTable();
			if(table != null)
			{
				table.getSelectionModel().addListSelectionListener(this);
			}
		}

		super.paint(g, c);
	}
}
