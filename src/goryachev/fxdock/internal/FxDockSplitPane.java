// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock.internal;
import javafx.beans.property.ReadOnlyProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;


/**
 * FxDockSplitPane.
 */
public class FxDockSplitPane
	extends SplitPane
{
	protected final ParentProperty parent = new ParentProperty();

	
	public FxDockSplitPane()
	{
	}
	
	
	public FxDockSplitPane(Orientation or, Node a, Node b)
	{
		setOrientation(or);
		addPane(a);
		addPane(b);
	}
	
	
	public final ReadOnlyProperty<Node> dockParentProperty()
	{
		return parent.getReadOnlyProperty();
	}
	
	
	public Node getPane(int ix)
	{
		if(ix >= 0)
		{
			ObservableList<Node> ps = getItems();
			if(ix < ps.size())
			{
				return ps.get(ix);
			}
		}
		return null;
	}
	
	
	public void addPane(Node n)
	{
		if(n == null)
		{
			n = new FxDockEmptyPane();
		}
		getItems().add(n);
		DockTools.setParent(this, n);
	}
	
	
	public void addPane(int ix, Node n)
	{
		getItems().add(ix, n);
		DockTools.setParent(this, n);
	}
	
	
	public Node removePane(int ix)
	{
		Node n = getItems().remove(ix);
		DockTools.setParent(null, n);
		return n;
	}
	
	
	public void removePane(Node n)
	{
		int ix = indexOfPane(n);
		if(ix >= 0)
		{
			removePane(ix);
		}
	}
	
	
	public int getPaneCount()
	{
		return getItems().size();
	}
	
	
	public ObservableList<Node> getPanes()
	{
		return getItems();
	}


	public int indexOfPane(Node n)
	{
		return getItems().indexOf(n);
	}
}
