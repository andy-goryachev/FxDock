// Copyright © 2016-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.beans.property.Property;
import javafx.scene.control.CheckMenuItem;


/**
 * CheckMenuItem that knows how to deal with CAction or a Property.
 */
public class CCheckMenuItem
	extends CheckMenuItem
{
	public CCheckMenuItem(String text)
	{
		super(text);
	}
	
	
	public CCheckMenuItem(String text, CAction a)
	{
		super(text);
		a.attach(this);
	}
	
	
	public CCheckMenuItem(String text, Property<Boolean> p)
	{
		super(text);
		selectedProperty().bindBidirectional(p);
	}
	
	
	public CCheckMenuItem(String text, GlobalBooleanProperty op)
	{
		super(text);
		selectedProperty().bindBidirectional(op);
	}
}
