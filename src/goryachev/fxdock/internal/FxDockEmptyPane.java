// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock.internal;
import javafx.beans.property.ReadOnlyProperty;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;


/**
 * FxDockEmptyPane.
 */
public class FxDockEmptyPane
	extends BorderPane
{
	protected final ParentProperty parent = new ParentProperty();
	
	
	public FxDockEmptyPane()
	{
	}


	public final ReadOnlyProperty<Node> dockParentProperty()
	{
		return parent.getReadOnlyProperty();
	}
}
