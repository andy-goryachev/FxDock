// Copyright © 2016-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;


/**
 * Fx Button Pane.
 * 
 * TODO default button
 * TODO fix HPane layout
 * TODO set minimum button width
 * TODO own layout, sets min width and alignment (to avoid fill())
 */
public class FxButtonPane
	extends HPane
{
	public static final CssStyle PANE = new CssStyle("FxButtonPane_PANE");
	private static final int MIN_WIDTH = 70;

	
	public FxButtonPane()
	{
		super(5);
		FX.style(this, PANE);
	}
	
	
	public FxButton addButton(String text, CssStyle style, FxAction a)
	{
		FxButton b = new FxButton(text, style, a);
		return addButton(b);
	}
	
	
	public FxButton addButton(String text, FxAction a)
	{
		FxButton b = new FxButton(text, a);
		return addButton(b);
	}
	
	
	public FxButton addButton(String text, CssStyle style, Runnable r)
	{
		FxButton b = new FxButton(text, style, new FxAction(r));  
		return addButton(b);
	}
	
	
	public FxButton addButton(String text, CssStyle style)
	{
		FxButton b = new FxButton(text, style, FxAction.DISABLED);  
		return addButton(b);
	}
	
	
	public FxButton addButton(String text, Runnable r)
	{
		FxButton b = new FxButton(text, new FxAction(r));  
		return addButton(b);
	}
	
	
	public FxButton addButton(String text)
	{
		FxButton b = new FxButton(text);
		b.setDisable(true);
		return addButton(b);
	}
	
	
	public FxButton addButton(FxButton b)
	{
		b.setMinWidth(MIN_WIDTH);
		add(b);
		return b;
	}
}
