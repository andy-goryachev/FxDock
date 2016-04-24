// Copyright (c) 2007-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.table;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.CMap;
import goryachev.common.util.SB;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;


public class CTreeTableModel<T extends CTreeNode>
	extends AbstractTableModel
{
	private T root;
	private TreeEntry rootEntry;
	private boolean rootVisible;
	private CList<TreeEntry> nodes = new CList();
	private CList<CTableColumn> columns = new CList();
	private Vector<CTreeTableListener> listeners = new Vector();
	
	
	public CTreeTableModel()
	{
	}
	
	
	public void setRoot(T root, boolean rootVisible)
	{
		this.rootVisible = rootVisible; 
		setRoot(root);
	}
	
	
	public boolean isRootVisible()
	{
		return rootVisible;
	}
	
	
	public T getRoot()
	{
		return root;
	}
	
	
	// completely replaces the tree
	public void setRoot(T root)
	{
		this.root = root;
		this.rootEntry = new TreeEntry(null, root);
		
		nodes.clear();
		if(isRootVisible())
		{
			nodes.add(rootEntry);
		}
		else
		{
			expandChildren(0, rootEntry);
		}
		fireTableDataChanged();
	}
	
	
	public void addColumn(CTableColumn c)
	{
		columns.add(c);
		fireTableStructureChanged();
	}

	
	public void addColumns(CTableColumn[] cs)
	{
		for(CTableColumn c: cs)
		{
			columns.add(c);
		}
		fireTableStructureChanged();
	}


	public CTableColumn getCTableColumn(int ix)
	{
		return columns.get(ix);
	}


	public int getColumnCount()
	{
		return columns.size();
	}
	
	
	public String getColumnName(int col) 
	{
		return columns.get(col).getName();
	}
	
	
	CList<CTableColumn> getColumns()
	{
		return columns;
	}
	
	
	public Class getColumnClass(int column)
	{
		return Object.class;
	}
	

	public int getRowCount()
	{
		return nodes.size();
	}
	
	
	public int size()
	{
		return getRowCount();
	}

	
	public Object getValueAt(int row, int col)
	{
		try
		{
			return columns.get(col).getText(get(row));
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	
	public boolean isCellEditable(int row, int col)
	{
		T item = get(row);
		if(item != null)
		{
			return item.isEditable();
		}
		return false;
	}
	
	
	public void setValueAt(Object val, int row, int col)
	{
		columns.get(col).set(get(row), val);
	}
	
	
	public T get(int row)
	{
		return (T)(nodes.get(row).getItem());
	}
	
	
	public boolean isExpanded(int ix)
	{
		if((ix < 0) || (ix >= nodes.size()))
		{
			return false;
		}
		
		TreeEntry nd = nodes.get(ix);
		return nd.isExpanded();
	}
	

	public void expandRow(CTreeTable tree, int ix)
	{
		if((ix < 0) || (ix >= nodes.size()))
		{
			// ignore event
			return;
		}
		
		TreeEntry nd = nodes.get(ix);
		if(nd.isExpanded())
		{
			return;
		}
		
		if(nd.isLeaf())
		{
			return;
		}
		
		try
		{
			fireTreeWillExpand(tree, ix);
		}
		catch(Exception e)
		{
			// operation blocked
			return;
		}
		
		tree.clearSelection();
		
		nd.setExpanded(true);
		if(ix >= 0)
		{
			fireTableRowsUpdated(ix, ix);
		}

		int firstChild = ix + 1;
		int inserted = expandChildren(firstChild, nd);

		if(inserted > 0)
		{
			fireTableRowsInserted(firstChild, firstChild + inserted - 1); // inclusive

			tree.changeSelection(ix, 0, false, false);
		}
	}
	
	
	public void collapseRow(CTreeTable tree, int ix)
	{
		if((ix < 0) || (ix >= nodes.size()))
		{
			// ignore event
			return;
		}
		
		TreeEntry nd = nodes.get(ix);
		if(nd.isCollapsed())
		{
			return;
		}
		
		try
		{
			fireTreeWillCollapse(tree, ix);
		}
		catch(Exception e)
		{
			// operation blocked
			return;
		}

		removeChildrenEntries(nd, ix);
		
		nd.setExpanded(false);
		
		if(ix > 0)
		{
			fireTableRowsUpdated(ix, ix);
		}
	}
	
	
	// removes all nodes with indent > current
	protected void removeChildrenEntries(TreeEntry nd, int row)
	{
		int indent = getIndent(nd);
		
		int removed = 0;
		int removePoint = row + 1;
		for(int i=removePoint; i<nodes.size(); i++)
		{
			TreeEntry ch = nodes.get(i);
			if(getIndent(ch) <= indent)
			{
				break;
			}
			else
			{
				removed++;
			}
		}
		
		if(removed > 0)
		{	
			nodes.removeRange(removePoint, removePoint + removed); // exclusive
			fireTableRowsDeleted(removePoint, removePoint + removed - 1); // inclusive
		}
	}
	
	
	protected void removeModelRow(TreeEntry<T> ch, int ix)
	{
		removeChildrenEntries(ch, ix);
		
		nodes.remove(ix);
		fireTableRowsDeleted(ix, ix);
	}
	
	
	protected int expandChildren(int insertPoint, TreeEntry node)
	{
		TreeEntry[] children = node.getChildren();
		if(children == null)
		{
			Object[] newch = node.getItem().getChildren();
			children = node.createChildEntries(newch);
		}
			
		int inserted = 0;
		for(int i=0; i<children.length; i++)
		{
			TreeEntry ch = children[i];
			nodes.add(insertPoint + inserted, ch);
			inserted++;
			
			if(ch.isExpanded())
			{
				inserted += expandChildren(insertPoint + inserted, ch);
			}
		}
		
		return inserted;
	}
	
	
	public void fireTreeWillExpand(CTreeTable tree, int row) throws Exception
	{
		for(CTreeTableListener li: listeners)
		{
			li.treeTableWillExpand(tree,row);
		}
	}
	
	
	public void fireTreeWillCollapse(CTreeTable tree, int row) throws Exception
	{
		for(CTreeTableListener li: listeners)
		{
			li.treeTableWillCollapse(tree,row);
		}
	}
	
	
	public void expandRoot(CTreeTable tree)
	{
		if(root != null)
		{
			expandRow(tree, isRootVisible() ? 0 : -1);
		}
	}
	
	
	public void collapseRoot(CTreeTable tree)
	{
		if(root != null)
		{
			collapseRow(tree, isRootVisible() ? 0 : -1);
		}
	}
	

	public int getIndent(TreeEntry nd)
	{
		return nd.indent() - (isRootVisible() ? 0 : 1);
	}
	
	
	public TreeEntry getNode(int ix)
	{
		if(ix < 0)
		{
			return null;
		}
		else if(ix >= nodes.size())
		{
			return null;
		}
		else
		{
			return nodes.get(ix);
		}
	}
	

	public void addTreeTableListener(CTreeTableListener li)
	{
		listeners.add(li);
	}


	public void removeTreeTableListener(CTreeTableListener li)
	{
		listeners.remove(li);
	}
	
	
	// updates the tree by retaining unchanged nodes, deleting deleted nodes
	// and inserting added nodes - for all visible tree nodes
	public void updateRoot(T root)
	{
		this.root = root;
		updateNode(root, 0, false);
	}
	
	
	protected void fireModelUpdated(int ix)
	{
		if(ix >= 0)
		{
			fireTableRowsUpdated(ix,ix);
		}
	}
	
	
	protected void fireModelRowInserted(int ix)
	{
		if(ix >= 0)
		{
			fireTableRowsInserted(ix,ix);
		}
	}
	
	
	protected void fireModelRowDeleted(int ix)
	{
		if(ix >= 0)
		{
			fireTableRowsDeleted(ix,ix);
		}
	}
	
	
	protected int find(Object key, int startRow)
	{
		// passing parent index, children always below
		startRow++;
		
		int sz = nodes.size();
		for(int i=startRow; i<sz; i++)
		{
			// works on both old and new
			if(CKit.equals(nodes.get(i).getKey(), key))
			{
				return i;
			}
		}
		
		return -1;
	}
	
	
	public void refresh(int ix)
	{
		TreeEntry<T> nd = getNode(ix);
		updateNode(nd.getItem(), ix, true);
	}
	
	
	public void refreshRoot()
	{
		// root is always expanded
		updateChildren(root, -1, rootEntry, true);
	}
	
	
	protected void updateNode(T newItem, int index, boolean reload)
	{
		TreeEntry nd = nodes.get(index);
//		if(nd.isCollapsed())
//		{
//			nd.setItem(this, newItem);
//		}
//		else
		{
			updateChildren(newItem, index, nd, reload);
		}
		fireModelUpdated(index);
	}


	public String dump()
	{
		SB sb = new SB();
		sb.nl();
		
		for(int i=0; i<getRowCount(); i++)
		{
			TreeEntry tn = getNode(i);
			int indent = getIndent(tn);
			boolean expanded = isExpanded(i); 
			
			sb.a("  ");
			for(int j=0; j<indent; j++)
			{
				sb.a("   ");
			}

			sb.a(tn.getItem().toString());
			
			if(expanded)
			{
				sb.a(", expanded");
			}
			
			sb.nl();
		}

		// dump real structure
		sb.nl();
		dump(sb, rootEntry, 0);

		return sb.toString();
	}


	protected void dump(SB sb, TreeEntry en, int indent)
	{
		for(int i=0; i<indent; i++)
		{
			sb.a("   ");
		}
		
		if(en == rootEntry)
		{
			sb.a("ROOT");
		}
		else
		{
			sb.a(en.getItem());
		}
		if(en.isExpanded())
		{
			sb.a(" expanded");
		}
		sb.a(", ");
		sb.a(en.getItem().getTreeNodeKey());
		sb.nl();
		
		++indent;
		for(TreeEntry ch: en.getChildren())
		{
			dump(sb, ch, indent);
		}
	}
	
	
	protected void updateChildren(T newNode, int index, TreeEntry<T> en, boolean reload)
	{
		TreeEntry<T>[] oldChildren = en.getChildren();
		if(reload)
		{
			en.clearCachedChildren();
			newNode.forceLoadChildren();
		}
		
		en.setItem(this, newNode);
		
		TreeEntry<T>[] newChildren = en.getChildren();

		// node contains new data and new children,
		// the tree is out of sync

		// synchronize tree structure
		CMap<String,TreeEntry<T>> oldies = new CMap();
		if(oldChildren != null)
		{
			for(TreeEntry<T> ch: oldChildren)
			{
				oldies.put(ch.getKey(), ch);
			}
		}

		CMap<String,TreeEntry<T>> newbies = new CMap();
		for(TreeEntry<T> ch: newChildren)
		{
			newbies.put(ch.getKey(), ch);
		}

		// insert added nodes
		int insertRow = index + 1;
		for(int ix=0; ix<newChildren.length; ix++)
		{
			TreeEntry<T> ch = newChildren[ix];
			String key = ch.getKey();
			
			if(oldies.containsKey(key))
			{
				// refresh unchanged node
				TreeEntry<T> oldn = oldies.get(key);
				
				// use old instance
				newChildren[ix] = oldn;
				
				int row = find(key, index);
				if(row >= 0) // new code
				{
					if(insertRow <= row)
					{
						insertRow = row + oldn.getExpandedBranchHeight();
					}
	
					updateNode(ch.getItem(), row, false);
				}
			}
			else
			{
				nodes.add(insertRow, ch);
				insertRow++;
				
				fireModelRowInserted(insertRow);
			}
		}

		// remove deleted nodes
		if(oldChildren != null)
		{
			for(TreeEntry<T> ch: oldChildren)
			{
				if(!newbies.containsKey(ch.getKey()))
				{
					// collapse and delete
					int row = find(ch.getKey(), index);
					if(row >= 0)
					{
						removeModelRow(ch, row);
					}
				}
			}
		}
	}
}
