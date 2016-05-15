// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock.internal;
import goryachev.fx.FX;
import goryachev.fxdock.FxDockStyles;
import javafx.beans.property.ReadOnlyProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;


/**
 * FxDockSplitPane.
 */
public class FxDockSplitPane
	extends SplitPane
{
	protected final ParentProperty parent = new ParentProperty();

	
	public FxDockSplitPane()
	{
		init();
	}
	
	
	public FxDockSplitPane(Orientation or, Node a, Node b)
	{
		setOrientation(or);
		init();
		
		addPane(a);
		addPane(b);
	}
	
	
	private void init()
	{
		FX.style(this, FxDockStyles.FX_SPLIT_PANE);
		
		addEventFilter(MouseEvent.MOUSE_RELEASED, (ev) -> updateDividers());
	}
	
	
	protected void updateDividers()
	{
		Orientation ori = getOrientation();
		for(int i=getPaneCount()-1; i>=0; i--)
		{
			Node n = getPane(i);
			if(n instanceof FxDockEmptyPane)
			{
				double sz;
				if(ori == Orientation.HORIZONTAL)
				{
					sz = ((FxDockEmptyPane)n).getWidth();
				}
				else
				{
					sz = ((FxDockEmptyPane)n).getHeight();
				}
				
				if(sz < DragAndDropHandler.SPLIT_COLLAPSE_THRESHOLD)
				{
					removePane(i);
				}
			}
		}
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
		n = DockTools.prepareToAdd(n);
		getItems().add(n);
		DockTools.setParent(this, n);
	}
	
	
	public void addPane(int ix, Node n)
	{
		n = DockTools.prepareToAdd(n);
		getItems().add(ix, n);
		DockTools.setParent(this, n);
	}
	
	
	public void setPane(int ix, Node n)
	{
		removePane(ix);
		addPane(ix, n);
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
