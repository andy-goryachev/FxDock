// Copyright © 2016-2024 Andy Goryachev <andy@goryachev.com>
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
	
	
	public FxButton(String text, CssStyle style, FxAction a)
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
	
	
	public FxButton(String text, CssStyle style, Runnable handler)
	{
		this(text, style, new FxAction(handler));
	}
	
	
	public FxButton(String text, FxAction a)
	{
		super(text);
		a.attach(this);
	}
	
	
	public FxButton(String text, Runnable action)
	{
		this(text, new FxAction(action));
	}

	
	public FxButton(String text, String tooltip, FxAction a)
	{
		super(text);
		a.attach(this);
		FX.setTooltip(this, tooltip);
	}
	
	
	public FxButton(String text, String tooltip, CssStyle style, FxAction a)
	{
		this(text, style, a);
		FX.setTooltip(this, tooltip);
	}
	
	
	public FxButton(String text, String tooltip, CssStyle style, Runnable r)
	{
		this(text, style, r);
		FX.setTooltip(this, tooltip);
	}
	
	
	public FxButton(String text, String tooltip, Runnable action)
	{
		this(text, new FxAction(action));
		FX.setTooltip(this, tooltip);
	}
	
	
	public FxButton(Node icon, Runnable action)
	{
		this(icon, new FxAction(action));
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
