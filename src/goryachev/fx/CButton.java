// Copyright © 2016-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.scene.control.Button;


/**
 * Convenient Button.
 */
public class CButton
	extends Button
{
	public CButton(String text, FxAction a)
	{
		super(text);
		a.attach(this);
	}
	
	
	public CButton(String text)
	{
		super(text);
	}
	
	
	public CButton(String text, Runnable handler)
	{
		this(text, new FxAction(handler));
	}
}
