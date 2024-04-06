// Copyright © 2020-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;


/**
 * A slightly more conventient SplitPane.
 */
public class FxSplitPane
	extends SplitPane
{
	public FxSplitPane(Orientation ori, Node ... items)
	{
		super(items);
		setOrientation(ori);
	}

	
	public FxSplitPane(Node ... items)
	{
		super(items);
	}
	
	
	public FxSplitPane()
	{
	}
	
	
	public void setNoResize(Node n)
	{
		FX.preventSplitPaneResizing(n);
	}
}
