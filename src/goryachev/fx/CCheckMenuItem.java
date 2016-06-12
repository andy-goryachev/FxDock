// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.util.D;
import javafx.beans.property.Property;
import javafx.scene.control.CheckMenuItem;


/**
 * CheckMenuItem with action.
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
		
		selectedProperty().set(p.getValue());
		D.print(isSelected());
		selectedProperty().bindBidirectional(p);
		D.print(isSelected());
	}
}
