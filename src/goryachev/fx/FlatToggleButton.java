// Copyright © 2019 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;


/**
 * Flat Toggle Button.
 */
public class FlatToggleButton
	extends ToggleButton
{
	public static final CssStyle STYLE = new CssStyle("FlatToggleButton_STYLE");
	
	
	public FlatToggleButton(String text, Node graphic)
	{
		super(text, graphic);
		init();
	}
	
	
	public FlatToggleButton(String text)
	{
		super(text);
		init();
	}
	
	
	public FlatToggleButton()
	{
		init();
	}
	
	
	private void init()
	{
		FX.style(this, STYLE);
	}
}
