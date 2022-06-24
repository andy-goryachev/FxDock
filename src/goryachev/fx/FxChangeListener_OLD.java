// Copyright © 2020-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.util.Disconnectable;
import java.util.function.Consumer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;


/**
 * Special Change Listener that keeps track of the sole property it attaches to,
 * making it easier to for the client code to disconnect.
 */
public class FxChangeListener_OLD<T>
	implements ChangeListener<T>, Disconnectable
{
	private final Consumer<T> callback;
	private ObservableValue<T> property;
	
	
	public FxChangeListener_OLD(Consumer<T> callback)
	{
		this.callback = callback;
	}
	

	public void attach(ObservableValue<T> prop)
	{
		if(prop == property)
		{
			// already attached
			return;
		}
		else if(property != null)
		{
			throw new Error("attached twice: " + property);
		}
		
		property = prop;
		
		prop.addListener(this);
	}
	
	
	public void disconnect()
	{
		if(property != null)
		{
			property.removeListener(this);
		}
	}


	public void changed(ObservableValue<? extends T> src, T old, T value)
	{
		callback.accept(value);
	}
}
