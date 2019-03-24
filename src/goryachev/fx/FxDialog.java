// Copyright © 2016-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.stage.Modality;
import javafx.stage.Window;


/**
 * FX Dialogs might be a nice idea, but completely unacceptable:
 * buttons cannot be FxButtons, FxAction cannot be used,
 * too many hoops need to be jumped through to get even a simple dialog 
 * (converters, button types).
 * 
 * This is a different take which uses FxWindow and plugs in into our
 * FX framework.
 */
public class FxDialog
	extends FxWindow
{
	public final FxAction closeDialogAction = new FxAction(this::close);
	protected FxButtonPane buttonPane;
	
	
	public FxDialog(Object owner, String name)
	{
		super(name);
		
		initModality(Modality.APPLICATION_MODAL);

		Window win = FX.getParentWindow(owner);
		initOwner(win);
		
		// TODO center around parent window, but not outside of the current device
		double x = win.getX();
		double y = win.getY();
		double w = win.getWidth();
		double h = win.getHeight();
	}
	
	
	protected FxButtonPane buttonPane()
	{
		if(buttonPane == null)
		{
			buttonPane = new FxButtonPane();
			setBottom(buttonPane);
		}
		return buttonPane;
	}
	
	
	public FxButton addButton(String text, FxAction a, CssStyle style)
	{
		FxButton b = new FxButton(text, a, style);
		buttonPane().add(b);
		return b;
	}
	
	
	public FxButton addButton(String text, FxAction a)
	{
		FxButton b = new FxButton(text, a);
		buttonPane().add(b);
		return b;
	}
	
	
	public FxButton addButton(String text)
	{
		FxButton b = new FxButton(text, FxAction.DISABLED);
		buttonPane().add(b);
		return b;
	}
	
	
	public void fill()
	{
		buttonPane().fill();
	}
	
	
	public void open()
	{
		double w = getWidth();
		double h = getHeight();
		// TODO center over parent, but not to go outside of the screen
		
		super.open();
	}
}
