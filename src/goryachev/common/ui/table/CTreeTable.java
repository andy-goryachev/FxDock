// Copyright (c) 2007-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.table;
import goryachev.common.ui.CSelectionListener;
import goryachev.common.ui.UI;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;
import java.util.EventObject;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;


/** 
 * A JTable where the first unmoveable column acts like a tree.
 */
public class CTreeTable<T extends CTreeNode>
	extends JTable
{
	private static final int TREE_COLUMN = 0;
	protected CTreeTableRendererEditor treeRenderer;
	protected CTreeTableRendererEditor treeEditor;
	private Vector<CTreeTableListener> listeners;
	private Vector<CSelectionListener> selectionListeners;
		
	
	public CTreeTable()
	{
		super(new CTreeTableModel<T>());
		
		getTableHeader().setReorderingAllowed(false);
		treeRenderer = new CTreeTableRendererEditor();
		treeEditor = new CTreeTableRendererEditor();
		
		Handler2 h = new Handler2();
		UI.addFirstMouseListener(this, h);
		UI.addFirstMouseMotionListener(this, h);
	}


	public void setRoot(T root, boolean showRoot)
	{
		getTreeModel().setRoot(root, showRoot);
		if(root instanceof CTreeRootNode)
		{
			((CTreeRootNode)root).setTreeTable(this);
		}
		expandRoot();
	}
	
	
	public void setRoot(T root)
	{
		getTreeModel().setRoot(root);
		if(root instanceof CTreeRootNode)
		{
			((CTreeRootNode)root).setTreeTable(this);
		}
		expandRoot();
	}
	
	
	public T getRoot()
	{
		return getTreeModel().getRoot();
	}
	
	
	public void setIndentColor(Color c)
	{
		treeRenderer.setIndentColor(c);
	}

	
	public void addColumn(CTableColumn c)
	{
		getTreeModel().addColumn(c);
		updateColumns();
	}
	
	
	public void addColumns(CTableColumn[] cs)
	{
		getTreeModel().addColumns(cs);
		updateColumns();
	}
	
	
	public CTableColumn getCTableColumn(int viewCol)
	{
		int modelColumn = convertColumnIndexToModel(viewCol);
		return getTreeModel().getCTableColumn(modelColumn);
	}
	
	
	public CTreeTableModel getCTreeTableModel()
	{
		return (CTreeTableModel)getModel();
	}
	
	
	protected void updateColumns()
	{
		CList<CTableColumn> cs = getTreeModel().getColumns();
		TableColumnModel cm = getColumnModel();
		int sz = cm.getColumnCount();
		for(int i=0; i<sz; i++)
		{
			TableColumn c = cm.getColumn(i);
			CTableColumn ac = cs.get(i);
			
			int w = ac.getMinimumWidth();
			if(w >= 0)
			{
				c.setMinWidth(w);
			}
			
			w = ac.getMaximumWidth();
			if(w >= 0)
			{
				c.setMaxWidth(w);
			}
			
			w = ac.getPreferredWidth();
			if(w >= 0)
			{
				//c.setPreferredWidth(w);
			}
			
			c.setCellRenderer(ac.getRenderer());
			c.setCellEditor(ac.getCellEditor());
			
			ac.configureHeader(c);
		}
	}
	
	
	public void toggle(int row)
	{
		TreeEntry nd = getTreeEntry(row);
		if(nd != null)
		{
			if(!nd.isLeaf())
			{
				if(nd.isExpanded())
				{
					getTreeModel().collapseRow(this, row);
				}
				else
				{
					getTreeModel().expandRow(this, row);
				}
			}
		}
	}
	
	
	public TableCellRenderer getCellRenderer(int row, int col)
	{
		if(col == TREE_COLUMN)
		{
			treeRenderer.setBaseRenderer(super.getCellRenderer(row, col));
			return treeRenderer;
		}
		else
		{
			return super.getCellRenderer(row, col);
		}
	}
	
	
	public TableCellEditor getCellEditor(int row, int col) 
	{
		TableCellEditor ed;
		if(col == TREE_COLUMN)
		{
			treeEditor.setBaseEditor(super.getCellEditor(row, col));
			ed = treeEditor;
		}
		else
		{
			ed = super.getCellEditor(row, col);
		}
		
		// FIX does not help: first typed symbol gets eaten

		// http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4222149
		if(ed instanceof DefaultCellEditor)
		{
			((DefaultCellEditor)ed).setClickCountToStart(0);
		}

		return ed;
	}
	
	
	public boolean isExpanded(T item)
	{
		int ix = rowForNode(item);
		return ix < 0 ? true : isRowExpanded(ix);
	}
	
	
	public boolean isRowExpanded(int viewRow)
	{
		return getTreeModel().isExpanded(viewRow);
	}
	
	
	public void expandRow(int row)
	{
		getTreeModel().expandRow(this, row);
	}
	
	
	public void collapseRow(int row)
	{
		getTreeModel().collapseRow(this, row);
	}

	
	public void setModel(TableModel m)
	{
		// check for null because this method is called before constructor
		if(listeners == null)
		{
			listeners = new Vector();
		}
		
		CTreeTableModel<T> old = getTreeModel();
		if(old != null)
		{
			for(CTreeTableListener li: listeners)
			{
				old.removeTreeTableListener(li);
			}
		}

		for(CTreeTableListener li: listeners)
		{
			((CTreeTableModel<T>)m).addTreeTableListener(li);
		}
		
		super.setModel(m);
	}
	

	public T getFirstSelectedNode()
	{
		try
		{
			return getTreeModel().get(getSelectionModel().getMinSelectionIndex());
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	
	public void updateRoot(T root)
	{
		getTreeModel().updateRoot(root);
	}
	

	public void expandRoot()
	{
		getTreeModel().expandRoot(this);
	}
	
	
	public void collapseRoot()
	{
		getTreeModel().collapseRoot(this);
	}
	
	
	public CTreeTableModel<T> getTreeModel()
	{
		TableModel m = getModel();
		if(m instanceof CTreeTableModel)
		{
			return (CTreeTableModel<T>)m;
		}
		return null;
	}
	
	
	public TreeEntry getTreeEntry(int viewRow)
	{
		return getTreeModel().getNode(viewRow);
	}
	

	public void fireRowUpdated(int viewRow)
	{
		int ix = convertRowIndexToModel(viewRow);
		if(ix >= 0)
		{
			getTreeModel().fireTableRowsUpdated(ix,ix);
		}
	}
	
	
	public T getNode(int viewRow)
	{
		TreeEntry<T> n = getTreeEntry(viewRow);
		if(n != null)
		{
			return n.getItem();
		}
		return null;
	}

	
	public T getSelectedNode()
    {
		int ix = getSelectionModel().getLeadSelectionIndex();
		if(ix >= 0)
		{
			return getNode(ix);
		}
		return null;
    }
	
	
	public void addSelectionListener(CSelectionListener li)
	{
		if(selectionListeners == null)
		{
			selectionListeners = new Vector();
			getSelectionModel().addListSelectionListener(new ListSelectionListener()
			{
				public void valueChanged(ListSelectionEvent ev)
				{
					if(!ev.getValueIsAdjusting())
					{
						fireSelectionEvent();
					}
				}
			});
		}
		selectionListeners.add(li);
	}
	
	
	public void removeSelectionListener(CSelectionListener li)
	{
		if(selectionListeners != null)
		{
			selectionListeners.remove(li);
		}
	}
	
	
	protected void fireSelectionEvent()
	{
		for(CSelectionListener li: selectionListeners)
		{
			li.selectionChanged();
		}
	}
	
	
	public void expandAll()
	{
		for(int i=0; i<getRowCount(); i++)
		{
			expandRow(i);
		}
	}
	
	
	public CList<T> getSelectedNodes()
	{
		int[] rows = getSelectedRows();
		int sz = rows.length;
		CList<T> rv = new CList(sz);
		for(int i=0; i<sz; i++)
		{
			rv.add(getNode(rows[i]));
		}
		return rv;
	}
	
	
	public int findNodeRow(CTreeNode item)
	{
		String[] path = item.getPathKeys();
		
		int sz = getRowCount();
		for(int i=0; i<sz; i++)
		{
			T en = getNode(i);
			if(CKit.equals(path, en.getPathKeys()))
			{
				return i;
			}
		}
		return -1;
	}
	
	
	public void refresh(CTreeNode item)
	{
		int ix = findNodeRow(item);
		if(ix < 0)
		{
			if(item != null)
			{
				if(CKit.equals(getRoot().getTreeNodeKey(), item.getTreeNodeKey()))
				{
					getTreeModel().refreshRoot();
				}
			}
		}
		else
		{
			getTreeModel().refresh(ix);
		}
	}


	/** returns path string (object keys delimited with NUL symbols) */
//	public String getPathString(int viewRow)
//	{
//		int row = convertRowIndexToModel(viewRow);
//		TreeEntry nd = getTreeModel().getNode(row);
//		if(nd != null)
//		{
//			SB sb = new SB();
//			getPathString(nd, sb);
//			return sb.toString();
//		}
//		return null;
//	}
	
	
//	protected void getPathString(TreeEntry nd, SB sb)
//	{
//		if(nd != null)
//		{
//			getPathString(nd.getParent(), sb);
//			
//			if(sb.length() > 0)
//			{
//				sb.a(DELIMITER);
//			}
//			
//			String key = nd.getKey();
//			if(key == null)
//			{
//				key = "";
//			}
//			sb.a(key);
//		}
//	}
	
	
	public int findRowByPath(String[] fullPath)
	{
		for(int ix=0; ix<getRowCount(); ix++)
		{
			T item = getNode(ix);
			String[] path = item.getPathKeys();
			if(Arrays.equals(path, fullPath))
			{
				return ix;
			}
		}
		return -1;
	}
	
	
	/** expands the path and returns row index of the item identified by path or -1 if not found */
    public int expandPath(String[] fullPath)
	{
    	// FIX this is wrong, needs to go from the root!
		if(fullPath != null)
		{
			int start = 0;
			for(String k: fullPath)
			{
				boolean found = false;
				
				for(int ix=start; ix<getRowCount(); ix++)
				{
					T item = getNode(ix);
					String[] path = item.getPathKeys();
					
					if(isPrefix(path, fullPath))
					{
						found = true;
						
						expandRow(ix);
						start = ix + 1;
						if(isRowExpanded(ix))
						{
							break;
						}
						else
						{
							return ix;
						}
					}
				}
				
				if(!found)
				{
					return -1;
				}
			}
		}
		
		return -1;
	}


	protected boolean isPrefix(String[] prefix, String[] path)
	{
		if(prefix.length <= path.length)
		{
			for(int i=0; i<prefix.length; i++)
			{
				if(CKit.notEquals(prefix[i], path[i]))
				{
					return false;
				}
			}
			return true;
		}
		return false;
	}


	public void selectRow(int row)
	{
		changeSelection(row, 0, false, false);
	}


	public void selectRows(int[] rows)
	{
		clearSelection();
		
		for(int i=0; i<rows.length; i++)
		{
			boolean extend = (i != 0);
			changeSelection(rows[i], 0, false, extend);
		}
	}
	
	
	public void repaintRow(int viewRow)
	{
		if(viewRow >= 0)
		{
			int ix = convertRowIndexToModel(viewRow);
			getCTreeTableModel().fireTableRowsUpdated(ix, ix);
		}
	}


	public int rowForNode(T item)
	{
		if(item != null)
		{
			String[] itemPath = item.getPathKeys();
			int sz = getRowCount();
			for(int i=0; i<sz; i++)
			{
				if(CKit.equals(itemPath, getNode(i).getPathKeys()))
				{
					return i;
				}
			}
		}
		return -1;
	}
	
	
	public void selectNode(T item)
	{
		int ix = rowForNode(item);
		if(ix >= 0)
		{
			selectRow(ix);
		}
	}
	
	
	public void expandNode(CTreeNode item)
	{
		String[] keys = item.getPathKeys();
		expandPath(keys);
	}
	
	
	// http://www.java-forums.org/awt-swing/28508-start-editing-cell-jtable-gaining-focus.html
	public boolean editCellAt(int row, int column, EventObject ev)
	{
		boolean rv = super.editCellAt(row, column, ev);
		if(rv)
		{
			final Component ed = getEditorComponent();
			UI.later(new Runnable()
			{
				public void run()
				{
//					if(ed instanceof JTextComponent)
//					{
//						((JTextComponent)ed).selectAll();
//					}
					ed.requestFocusInWindow();
				}
			});
		}
		return rv;
	}


	public String dump()
	{
		return getCTreeTableModel().dump();
	}


	public void stopEditing()
	{
		UI.stopEditing(this);
	}
	
	
	public int getColumnsPreferredWidth()
	{
		int w = 0;
		for(int i=0; i<getColumnCount(); i++)
		{
			int cw = getColumnPreferredWidth(i);
			w += cw;
		}
		return w;
	}


	public int getColumnPreferredWidth(int col)
	{
		int w = 0;
		
		for(int row=0; row<getRowCount(); row++)
		{
			int cw = getCellRect(row, col, true).width;
			
			if(w < cw)
			{
				w = cw;
			}
		}
		
		return w;
	}

	
	//
	
	
	public class Handler2 
		implements MouseListener, MouseMotionListener
	{
		private boolean ignoreDrag;
		
		
		public void mousePressed(MouseEvent ev)
		{
			Point p = ev.getPoint();
			int col = columnAtPoint(p);
			if(col == TREE_COLUMN)
			{
				int row = rowAtPoint(p);
				TreeEntry nd = getTreeEntry(row);
				if(nd != null)
				{
					// check if within the expand/collapse border 
					if(treeRenderer.isExpandClick(getTreeModel().getIndent(nd), p.x))
					{
						toggle(row);
						ignoreDrag = true;
						ev.consume();
					}
				}
			}
		}
		
		public void mouseClicked(MouseEvent ev)
		{
			if(ev.getClickCount() == 2)
			{
				Point p = ev.getPoint();					
				int row = rowAtPoint(p);
				TreeEntry nd = getTreeEntry(row);
				if(nd != null)
				{
					// check if within the expand/collapse border 
					if(!treeRenderer.isExpandClick(getTreeModel().getIndent(nd), p.x))
					{
						toggle(row);
						ignoreDrag = true;
						ev.consume();
					}
				}
			}
		}


		public void mouseDragged(MouseEvent ev)
		{
			if(ignoreDrag)
			{
				ev.consume();
			}
		}


		public void mouseReleased(MouseEvent ev)
		{
			ignoreDrag = false;
		}

		
		public void mouseEntered(MouseEvent e) { }
		public void mouseExited(MouseEvent e) { }
		public void mouseMoved(MouseEvent e) { }
	}
}
