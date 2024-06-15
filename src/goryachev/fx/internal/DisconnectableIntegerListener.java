// Copyright © 2021-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.internal;
import goryachev.common.util.IDisconnectable;
import java.util.function.IntConsumer;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;


/**
 * Disconnectable Integer Listener.
 */
public class DisconnectableIntegerListener
	implements ChangeListener<Number>, IDisconnectable
{
	private final ReadOnlyIntegerProperty prop;
	private final IntConsumer onChange;
	
	
	public DisconnectableIntegerListener(ReadOnlyIntegerProperty prop, IntConsumer onChange)
	{
		this.prop = prop;
		this.onChange = onChange;
		
		prop.addListener(this);
	}


	@Override
	public void disconnect()
	{
		prop.removeListener(this);
	}


	@Override
	public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
	{
		// newValue cannot be null because the source is a ReadOnlyIntegerProperty.
		int v = newValue.intValue();
		onChange.accept(v);
	}
}
