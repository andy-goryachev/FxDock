// Copyright © 2021-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.util.CList;
import goryachev.common.util.Disconnectable;
import javafx.beans.value.ObservableValue;


/**
 * Fx Disconnector.
 */
public class FxDisconnector
	implements Disconnectable
{
	private final CList<Disconnectable> items = new CList();
	
	
	public FxDisconnector()
	{
	}
	
	
	public void addDisconnectable(Disconnectable d)
	{
		items.add(d);
	}


	public void disconnect()
	{
		for(int i=items.size()-1; i>=0; i--)
		{
			Disconnectable d = items.remove(i);
			d.disconnect();
		}
	}
	

	/** 
	 * A simplified version of addChangeListener that only invokes the callback on change, 
	 * uses FxDisconnector to allow for easy removal of the listener.
	 */
	public Disconnectable addChangeListener(Runnable callback, boolean fireImmediately, ObservableValue<?> ... props)
	{
		FxChangeListener li = new FxChangeListener(callback);
		for(ObservableValue<?> p: props)
		{
			li.listen(p);
		}
		
		if(fireImmediately)
		{
			li.fire();
		}
		
		addDisconnectable(li);
		return li;
	}
	
	
	/** 
	 * A simplified version of addChangeListener that only invokes the callback on change, 
	 * uses FxDisconnector to allow for easy removal of the listener.
	 */
	public Disconnectable addChangeListener(Runnable callback, ObservableValue<?> ... props)
	{
		return addChangeListener(callback, false, props);
	}
}
