// Copyright © 2016-2025 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.scene.Node;


/**
 * Flat Button.
 */
public class FlatButton
	extends FxButton
{
	public static final CssStyle STYLE = new CssStyle();
	
	
	public FlatButton(String text, FxAction a)
	{
		super(text, a);
		STYLE.set(this);
	}
	
	
	public FlatButton(String text, Runnable action)
	{
		super(text, action);
		STYLE.set(this);
	}
	
	
	public FlatButton(String text)
	{
		super(text);
		STYLE.set(this);
	}
	
	
	public FlatButton(Node icon)
	{
		super(icon);
		STYLE.set(this);
	}
	
	
	public FlatButton(Node icon, FxAction a)
	{
		super(icon, a);
		STYLE.set(this);
	}
	
	
	public FlatButton(Node icon, Runnable action)
	{
		super(icon, action);
		STYLE.set(this);
	}
}
