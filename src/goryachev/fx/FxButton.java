// Copyright © 2016-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.scene.control.Button;


/**
 * Convenient Fx Button.
 */
public class FxButton
	extends Button
{
	public FxButton(String text, FxAction a)
	{
		super(text);
		a.attach(this);
	}
	
	
	public FxButton(String text)
	{
		super(text);
	}
	
	
	public FxButton(String text, Runnable handler)
	{
		this(text, new FxAction(handler));
	}
}
