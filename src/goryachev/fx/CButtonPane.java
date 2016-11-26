// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.geometry.Insets;


/**
 * CButtonPane.
 * 
 * TODO default button
 * TODO fix HPane layout
 */
public class CButtonPane
	extends HPane
{
	public CButtonPane()
	{
		super(10);
		setPadding(new Insets(10, 10, 10, 10));
	}
	
	
	public CButton addButton(String text, CAction a)
	{
		CButton b = new CButton(text, a);
		b.setMinWidth(70);
		
		if(getChildren().size() == 0)
		{
			fill();
		}
		
		add(b);
		return b;
	}
	
	
	public CButton addButton(String text)
	{
		CButton b = new CButton(text);
		b.setMinWidth(70);
		b.setDisable(true);
		
		if(getChildren().size() == 0)
		{
			fill();
		}
		
		add(b);
		return b;
	}
}
