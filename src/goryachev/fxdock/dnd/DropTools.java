// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock.dnd;
import goryachev.fxdock.internal.DockTools;
import javafx.scene.Node;


/**
 * DropTools.
 */
public class DropTools
{
	public static Node getDockableParent(Node n)
	{
		while(n != null)
		{
			if(DockTools.isDockableElement(n))
			{
				return n;
			}
			
			n = n.getParent();
		}
		return null;
	}
}
