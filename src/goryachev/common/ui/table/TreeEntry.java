// Copyright (c) 2007-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.table;


/** 
 * Container object which corresponds to a CTreeTableMode row, 
 * holding a pointer to the payload item data and expanded state.
 */ 
public class TreeEntry<T extends CTreeNode>
{
	public final TreeEntry<T> parent;
	private T item;
	private boolean expanded;
	private TreeEntry<T>[] children;


	public TreeEntry(TreeEntry<T> parent, T d)
	{
		this.parent = parent;
		this.item = d;
	}
	
	
	public TreeEntry<T> getParent()
	{
		return parent;
	}


	public TreeEntry<T>[] getChildren()
	{
		if(children == null)
		{
			children = createChildEntries(item == null ? null : item.getChildren());
		}
		return children;
	}


	public int indent()
	{
		return (parent == null) ? 0 : parent.indent() + 1;
	}


	public boolean isLeaf()
	{
		return item == null ? true : item.isLeaf();
	}


	public TreeEntry[] createChildEntries(Object[] ch)
	{
		if(ch == null)
		{
			children = new TreeEntry[0];
		}
		else
		{
			TreeEntry[] nch = new TreeEntry[ch.length];
			for(int i=0; i<ch.length; i++)
			{
				nch[i] = new TreeEntry(this, (CTreeNode)ch[i]);
			}
			children = nch;
		}
		return children;
	}


	public String toString()
	{
		return indent() + " " + item.toString();
	}


	public boolean isExpanded()
	{
		return expanded;
	}


	public boolean isCollapsed()
	{
		return !expanded;
	}


	void setExpanded(boolean on)
	{
		expanded = on;
	}


	void setItem(CTreeTableModel model, T d)
	{
		item = d;
		children = createChildEntries(d.getChildren());
	}


	public T getItem()
	{
		return item;
	}


	public String getKey()
	{
		return item.getTreeNodeKey();
	}
	
	
	public void clearCachedChildren()
	{
		children = null;
	}
	
	
	/** returns the number of rows corresponding to expanded children, or 0 if collapsed */
	public int getExpandedBranchHeight()
	{
		int rv = 1;
		if(isExpanded())
		{
			for(TreeEntry<T> ch: children)
			{
				rv += ch.getExpandedBranchHeight();
			}
			return rv;
		}
		
		return rv;
	}
}