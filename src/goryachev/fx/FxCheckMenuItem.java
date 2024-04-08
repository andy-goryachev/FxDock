// Copyright © 2016-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.beans.property.Property;
import javafx.scene.control.CheckMenuItem;


/**
 * CheckMenuItem that knows how to deal with FxAction or a Property.
 */
public class FxCheckMenuItem
	extends CheckMenuItem
{
	public FxCheckMenuItem(String text)
	{
		super(text);
	}
	
	
	public FxCheckMenuItem(String text, FxAction a)
	{
		super(text);
		a.attach(this);
	}
	
	
	public FxCheckMenuItem(String text, Property<Boolean> p)
	{
		super(text);
		selectedProperty().bindBidirectional(p);
	}
	
	
	public FxCheckMenuItem(String text, GlobalBooleanProperty op)
	{
		super(text);
		selectedProperty().bindBidirectional(op);
	}
}
