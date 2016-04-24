// Copyright (c) 2010-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.table;


public interface CTreeTableListener
{
	public void treeTableWillExpand(CTreeTable tree, int tableRow) throws Exception;


	public void treeTableWillCollapse(CTreeTable tree, int tableRow) throws Exception;
}
