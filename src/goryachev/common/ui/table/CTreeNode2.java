// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.table;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;


/** 
 * A CTreeNode which holds the list of its children.
 * This class normally provides a cached mode of operation when the children
 * are added once.  For a non-caching mode, override loadChildren() method to reload 
 * children each time getChildren() is called (make sure to call clearChildren());
 */
public abstract class CTreeNode2
	extends CTreeNode
{
	/** override in non-caching mode */
	protected void loadChildren() { }
	
	//
	
	private CList<CTreeNode> children;

	
	public CTreeNode2(String key)
	{
		super(key);
	}
	

	public final Object[] getChildren()
	{
		return children().toArray();
	}
	
	
	protected CList<CTreeNode> children()
	{
		if(children == null)
		{
			children = new CList();
			
			loadChildren();
		}
		return children;
	}
	
	
	protected final void forceLoadChildren()
	{
		children = null;
	}
	
	
	protected void clearChildren()
	{
		children = new CList();
	}
	
	
	public boolean isLeaf()
	{
		return children().size() == 0;
	}
	
	
	public int getChildrenCount()
	{
		return children().size();
	}

	
	/** returns index of the added child */
	public int addChild(int index, CTreeNode ch)
	{
		CList<CTreeNode> cs = children();
		int ix;
		
		if(index < 0)
		{
			ix = cs.size();
			cs.add(ch);
		}
		else 
		{
			cs.add(index, ch);
			ix = index;
		}
		
		ch.setParent(this);
		
		return ix;
	}
	
	
	public int addChild(CTreeNode ch)
	{
		return addChild(-1, ch);
	}
	
	
	public boolean removeChild(CTreeNode p)
	{
		int ix = indexOfChild(p);
		if(ix >= 0)
		{
			children().remove(ix);
			return true;
		}
		return false;
	}
	
	
	public int indexOfChild(CTreeNode p)
	{
		CList<CTreeNode> cs = children();
		
		for(int i=0; i<cs.size(); i++)
		{
			CTreeNode ch = cs.get(i);
			if(CKit.equals(ch.getTreeNodeKey(), p.getTreeNodeKey()))
			{
				return i;
			}
		}
		return -1;
	}
	
	
	public CTreeNode getChildAt(int ix)
	{
		return children().get(ix);
	}
}
