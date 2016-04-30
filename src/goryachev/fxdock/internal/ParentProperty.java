// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock.internal;
import goryachev.common.util.D;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.Node;


/**
 * ParentProperty.
 */
public class ParentProperty
	extends ReadOnlyObjectWrapper<Node>
{
	public ParentProperty()
	{
	}


	protected void invalidated()
	{
		D.print(get());
	}
}
