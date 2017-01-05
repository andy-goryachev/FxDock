// Copyright © 2016-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;


/**
 * CButtonPane.
 * 
 * TODO default button
 * TODO fix HPane layout
 */
public class CButtonPane
	extends HPane
{
	public static final CssStyle PANE = new CssStyle("CButtonPane_PANE");

	
	public CButtonPane()
	{
		super(10);
		FX.style(this, PANE);
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
