// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock.internal;
import javafx.beans.property.ReadOnlyProperty;
import javafx.scene.Node;
import javafx.scene.layout.Pane;


/**
 * FxDockEmptyPane.
 */
public class FxDockEmptyPane
	extends Pane
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
