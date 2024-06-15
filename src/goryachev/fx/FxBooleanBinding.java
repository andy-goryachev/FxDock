// Copyright © 2019-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.beans.Observable;
import javafx.beans.binding.BooleanBinding;


/**
 * FxBooleanBinding.
 */
public abstract class FxBooleanBinding
	extends BooleanBinding
{
	@Override
	protected abstract boolean computeValue();
	
	
	public FxBooleanBinding(Observable ... dependencies)
	{
		bind(dependencies);
	}
}
