// Copyright © 2020 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;


/**
 * Convenient Fx TabPane.
 */
public class FxTabPane
	extends TabPane
{
	public FxTabPane()
	{
	}
	
	
	public Tab addTab(String name, Node n)
	{
		Tab t = new Tab(name, n);
		getTabs().add(t);
		return t;
	}
	
	
	public Tab addTab(Tab t)
	{
		getTabs().add(t);
		return t;
	}
	
	
	public void selectTab(Tab t)
	{
		getSelectionModel().select(t);
	}
	
	
	/** selects a tab with the specified content Node */
	public boolean selectNode(Node n)
	{
		for(Tab t: getTabs())
		{
			if(t.getContent() == n)
			{
				getSelectionModel().select(t);
				return true;
			}
		}
		return false;
	}


	public Tab getSelectedItem()
	{
		return getSelectionModel().getSelectedItem();
	}
	
	
	public Node getSelectedNode()
	{
		Tab t = getSelectionModel().getSelectedItem();
		if(t == null)
		{
			return null;
		}
		return t.getContent();
	}
	
	
	public int indexOf(Node n)
	{
		for(int i=0, sz=getTabs().size(); i<sz; i++)
		{
			Tab t = getTabs().get(i);
			if(t.getContent() == n)
			{
				return i;
			}
		}
		return -1;
	}
	
	
	public Tab tabAt(int ix)
	{
		return getTabs().get(ix);
	}
}
