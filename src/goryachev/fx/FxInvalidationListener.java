// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;


/**
 * An InvalidationListener with ability to fire immediately
 */
public class FxInvalidationListener
	implements InvalidationListener
{
	private Runnable handler;
	
	
	public FxInvalidationListener(Observable prop, boolean immediate, Runnable handler)
	{
		this.handler = handler;
		prop.addListener(this);
		
		if(immediate)
		{
			handler.run();
		}
	}
	
	
	public FxInvalidationListener(Observable prop, Runnable handler)
	{
		this(prop, false, handler);
	}


	public void invalidated(Observable src)
	{
		handler.run();
	}
}
