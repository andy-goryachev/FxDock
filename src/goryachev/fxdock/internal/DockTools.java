// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock.internal;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.Node;


/**
 * Docking Framework Tools.
 */
public class DockTools
{
	public static void setParent(Node parent, Node child)
	{
		parent(child).set(parent);
	}
	
	
	public static Node getParent(Node n)
	{
		ReadOnlyObjectWrapper<Node> p = parent(n);
		return p == null ? null : p.get();
	}
	
	
	private static ReadOnlyObjectWrapper<Node> parent(Node n)
	{
		if(n instanceof FxDockBorderPane)
		{
			return ((FxDockBorderPane)n).parent;
		}
		else if(n instanceof FxDockEmptyPane)
		{
			return ((FxDockEmptyPane)n).parent;
		}
		else if(n instanceof FxDockSplitPane)
		{
			return ((FxDockSplitPane)n).parent;
		}
		else if(n instanceof FxDockTabPane)
		{
			return ((FxDockTabPane)n).parent;
		}
		return null;
	}
}
