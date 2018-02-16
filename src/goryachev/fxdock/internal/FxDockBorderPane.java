// Copyright © 2016-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock.internal;
import javafx.beans.property.ReadOnlyProperty;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;


/**
 * FxDockRootPane.
 */
public class FxDockBorderPane
	extends BorderPane
{
	protected final ParentProperty parent = new ParentProperty();

	
	public FxDockBorderPane()
	{
		setCenter(new FxDockEmptyPane());
	}
	
	
	public final ReadOnlyProperty<Node> dockParentProperty()
	{
		return parent.getReadOnlyProperty();
	}
}
