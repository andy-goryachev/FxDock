// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock.internal;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyProperty;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;


/**
 * FxDockRootPane.
 */
public class FxDockBorderPane
	extends BorderPane
{
	protected final ReadOnlyObjectWrapper<Node> parent = new ReadOnlyObjectWrapper<Node>();

	
	public FxDockBorderPane()
	{
		setContent(new FxDockEmptyPane());
	}
	
	
	public final void setContent(Node n)
	{
		if(n == null)
		{
			n = new FxDockEmptyPane();
		}
		setCenter(n);
		DockTools.setParent(this, n);
	}
	
	
	public final Node getContent()
	{
		return getCenter();
	}
	
	
	public final ReadOnlyProperty<Node> dockParentProperty()
	{
		return parent.getReadOnlyProperty();
	}
}
