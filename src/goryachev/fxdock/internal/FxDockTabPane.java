// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock.internal;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;


/**
 * FxDockTabPane.
 */
public class FxDockTabPane
	extends TabPane
{
	protected final ReadOnlyObjectWrapper<Node> parent = new ReadOnlyObjectWrapper<Node>();

	
	public FxDockTabPane()
	{
	}
	
	
	public final ReadOnlyProperty<Node> dockParentProperty()
	{
		return parent.getReadOnlyProperty();
	}
	
	
	public void addTab(Node n)
	{
		Tab t = new Tab(null, n);
		getTabs().add(t);
		FxDockTools.setParent(this, n);
	}
	

	public int indexOfTab(Node n)
	{
		ObservableList<Tab> ts = getTabs();
		for(int i=ts.size()-1; i>=0; --i)
		{
			Tab t = ts.get(i);
			if(t.getContent() == n)
			{
				return i;
			}
		}
		return -1;
	}
}
