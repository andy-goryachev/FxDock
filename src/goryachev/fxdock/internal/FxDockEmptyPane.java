// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock.internal;
import goryachev.fx.FX;
import goryachev.fxdock.FxDockStyles;
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
		FX.style(this, FxDockStyles.FX_EMPTY_PANE);
	}


	public final ReadOnlyProperty<Node> dockParentProperty()
	{
		return parent.getReadOnlyProperty();
	}
}
