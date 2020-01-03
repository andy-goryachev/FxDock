// Copyright © 2016-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.scene.Node;
import javafx.scene.control.Button;


/**
 * Convenient Fx Button.
 */
public class FxButton
	extends Button
{
	/** style for a button that performs some kind of affirmative action, such as OK, Continue, Yes and so on */
	public static final CssStyle AFFIRM = new CssStyle("FxButton_AFFIRM");
	/** style for a button that performs some kind of destructive action, such as No, Reject, Abort, and so on */
	public static final CssStyle DESTRUCT = new CssStyle("FxButton_DESTRUCT");
	
	
	public FxButton(String text, FxAction a, CssStyle style)
	{
		super(text);
		a.attach(this);
		FX.style(this, style);
	}
	
	
	public FxButton(String text, CssStyle style)
	{
		super(text);
		FX.style(this, style);
	}
	
	
	public FxButton(String text, Runnable handler, CssStyle style)
	{
		this(text, new FxAction(handler), style);
	}
	
	
	public FxButton(String text, FxAction a)
	{
		super(text);
		a.attach(this);
	}
	
	
	public FxButton(String text, Runnable handler)
	{
		this(text, new FxAction(handler));
	}
	
	
	public FxButton(String text)
	{
		super(text);
	}
	
	
	public FxButton(Node icon)
	{
		setGraphic(icon);
	}
	
	
	public FxButton(Node icon, FxAction a)
	{
		setGraphic(icon);
		a.attach(this);
	}
}
