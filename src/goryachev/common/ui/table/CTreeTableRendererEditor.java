// Copyright (c) 2007-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.table;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.Theme;
import goryachev.common.ui.icons.CIcons;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.util.EventObject;
import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;


public class CTreeTableRendererEditor
	extends CPanel
	implements TableCellRenderer, TableCellEditor
{
	public final static int LEFT_GAP = 2;
	public final static int RIGHT_GAP = 4;
	public final static int INDENT = CIcons.TreeExpanded.getIconWidth() + LEFT_GAP + RIGHT_GAP;
	
	public final IndentBorder border;
	public final JLabel expanIconField;
	private TableCellRenderer baseRenderer;
	private TableCellEditor baseEditor;
	private transient TreeEntry node;
	private transient Component component;
	private Color indentColor = Theme.FIELD_BG;
	
	
	public CTreeTableRendererEditor()
	{
		border = new IndentBorder();
		
		expanIconField = new JLabel();
		expanIconField.setBorder(border);

		setWest(expanIconField);
		setOpaque(true);
	}
	
	
	public void setIndentColor(Color c)
	{
		indentColor = c;
	}


	public boolean isExpandClick(int indent, int x)
	{
		return (x < ((indent + 1) * INDENT));
	}


	public void setBaseRenderer(TableCellRenderer r)
	{
		baseRenderer = r;
	}


	public void setBaseEditor(TableCellEditor ed)
	{
		baseEditor = ed;
	}


	public Object getCellEditorValue()
	{
		return baseEditor.getCellEditorValue();
	}


	public boolean isCellEditable(EventObject ev)
	{
		return baseEditor.isCellEditable(ev);
	}


	public boolean shouldSelectCell(EventObject ev)
	{
		return baseEditor.shouldSelectCell(ev);
	}


	public boolean stopCellEditing()
	{
		return baseEditor.stopCellEditing();
	}


	public void cancelCellEditing()
	{
		baseEditor.cancelCellEditing();
	}


	public void addCellEditorListener(CellEditorListener li)
	{
		// FIX too late
		if(node != null)
		{
			CTreeNode nd = node.getItem();
			nd.editingStarted();
		}
		
		baseEditor.addCellEditorListener(li);
	}


	public void removeCellEditorListener(CellEditorListener li)
	{
		baseEditor.removeCellEditorListener(li);
		
		// end of editing
		if(node != null)
		{
			CTreeNode nd = node.getItem();
			nd.editingStopped();
		}
	}
	
	
	protected CTreeTableModel model(JTable t)
	{
		return (CTreeTableModel)t.getModel();
	}


	public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean focus, int row, int column)
	{
		node = ((CTreeTable)t).getTreeEntry(row);
		if(node.isLeaf())
		{
			expanIconField.setIcon(CIcons.TreeBlank);
		}
		else
		{
			expanIconField.setIcon(node.isExpanded() ? CIcons.TreeExpanded : CIcons.TreeCollapsed);
		}
		// use transparent color?
		//border.set(model.getIndent(node), table.getGridColor());
		border.set(model(t).getIndent(node), indentColor);

		component = baseRenderer.getTableCellRendererComponent(t, val, sel, focus, row, column);
		if(component != getCenter())
		{
			setCenter(component);
		}
		setBackground(component.getBackground());
		return this;
	}


	public Component getTableCellEditorComponent(JTable t, Object val, boolean sel, int row, int col)
	{
		node = ((CTreeTable)t).getTreeEntry(row);
		if(node.isLeaf())
		{
			expanIconField.setIcon(CIcons.TreeBlank);
		}
		else
		{
			expanIconField.setIcon(node.isExpanded() ? CIcons.TreeExpanded : CIcons.TreeCollapsed);
		}
		
		// use transparent color?
		//border.set(model.getIndent(node), table.getGridColor());
		border.set(model(t).getIndent(node), indentColor);

		component = baseEditor.getTableCellEditorComponent(t, val, sel, row, col);
		if(component != getCenter())
		{
			setCenter(component);
		}
		setBackground(component.getBackground());
		return this;
	}
	
	
	public boolean requestFocusInWindow()
	{
		if(component != null)
		{
			component.requestFocus();
			component.requestFocusInWindow();
		}
		return true;
	}
	
	
	// see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4222149
	public void setClickCountToStart(int n)
	{
		if(baseEditor instanceof DefaultCellEditor)
		{
			((DefaultCellEditor)baseEditor).setClickCountToStart(n);
		}
	}


	//
	
	
	public static class IndentBorder 
		extends EmptyBorder
	{
		private Color color;
		private int indent;


		public IndentBorder()
		{
			super(0, 0, 0, RIGHT_GAP);
		}


		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
		{
			g.setColor(color);
			g.fillRect(0, 0, indent, height);
		}


		protected void set(int ind, Color c)
		{
			indent = ind * INDENT;
			left = LEFT_GAP + indent;
			color = c;
		}
	}
}
