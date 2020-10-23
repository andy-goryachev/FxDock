// Copyright © 2020 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import java.util.function.Consumer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;


/**
 * Special Change Listener that keeps track of the sole property it attaches to,
 * making it easier to for the client code to disconnect.
 */
public class FxChangeListener<T>
	implements ChangeListener<T>
{
	private final Consumer<T> callback;
	private ObservableValue<T> property;
	
	
	public FxChangeListener(Consumer<T> callback)
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
