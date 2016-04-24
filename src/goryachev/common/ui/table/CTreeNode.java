// Copyright (c) 2007-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.table;
import goryachev.common.util.CList;


/**
 * Represent data source for tree.
 */
public abstract class CTreeNode
{
	/** also determines whether getChildren() will be called */
	public abstract boolean isLeaf();

	/** 
	 * children are created once
	 */
	public abstract Object[] getChildren();
	
	public boolean isEditable() { return false; }
	
	/** returns true if node has been modified */
	public boolean removeChild(CTreeNode p) { throw new UnsupportedOperationException(); }
	
	// TODO this is weird, remove later
	public void editingStarted() { }
	public void editingStopped() { }
	
	//
	
	private String key;
	private CTreeNode parent;
	
	
	public CTreeNode(String key)
	{
		this.key = key;
	}
	
	
	public CTreeNode getParent()
	{
		return parent;
	}
	
	
	public void setParent(CTreeNode p)
	{
		if(p == null)
		{
			// special case
			parent = p;
		}
		else if(parent != p)
		{
			if(parent != null)
			{
				parent.removeChild(this);
			}
			this.parent = p;
		}
	}
	

	/** used to discard internal caches upon refresh */
	protected void forceLoadChildren()
	{
	}


	public String[] getPathKeys()
	{
		CList<String> rv = new CList();
		treeKeysRecursive(rv);
		return rv.toArray(new String[rv.size()]);
	}


	protected void treeKeysRecursive(CList<String> list)
	{
		if(parent != null)
		{
			parent.treeKeysRecursive(list);
		}
		
		list.add(getTreeNodeKey());
	}
	

	/** 
	 * Returns a key used to detemine equality.  The key must be unique between the siblings.
	 */
	public final String getTreeNodeKey()
	{
		return key;
	}
	
	
	public int indexOf(CTreeNode nd)
	{
		if(nd != null)
		{
			String key = nd.getTreeNodeKey();
			
			Object[] cs = getChildren();
			if(cs != null)
			{
				int sz = cs.length;
				for(int i=0; i<sz; i++)
				{
					Object ch = cs[i];
					String nodeKey = ((CTreeNode)ch).getTreeNodeKey();
					if(key.equals(nodeKey))
					{
						return i;
					}
				}
			}
		}
		return -1;
	}
}
