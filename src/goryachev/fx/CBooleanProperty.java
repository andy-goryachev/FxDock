// Copyright © 2017-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleBooleanProperty;


/**
 * Convenient SimpleBooleanProperty.
 */
public class CBooleanProperty
	extends SimpleBooleanProperty
{
	public CBooleanProperty(boolean initialValue, Runnable invalidationListener)
	{
		super(initialValue);
		addListener((s) -> invalidationListener.run());
	}
	
	
	public CBooleanProperty(boolean initialValue)
	{
		super(initialValue);
	}
	
	
	public CBooleanProperty(Runnable invalidationListener)
	{
		addListener((s) -> invalidationListener.run());
	}
	
	
	public CBooleanProperty()
	{
	}
}
