// Copyright © 2016-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Window;


/**
 * FX Dialogs might be a nice idea, but completely unacceptable:
 * buttons cannot be FxButtons, FxAction cannot be used;
 * too many hoops need to be jumped through to get even a simple dialog 
 * (converters, button types).
 * 
 * This class is of a more traditional design which uses a modal FxWindow.
 */
public class FxDialog<T>
	extends FxWindow
{
	public static final CssStyle PANE = new CssStyle("FxDialog_PANE");
	private final BorderPane pane;
	private T result;
	
	
	public FxDialog(Object owner, String name)
	{
		super(name);
		
		initModality(Modality.APPLICATION_MODAL);
		FX.style(getContentPane(), PANE);
		
		pane = new BorderPane();
		setCenter(pane);

		Window w = FX.getParentWindow(owner);
		initOwner(w);
		
		setMinSize(300, 200);
		
		if(w != null)
		{
			double x = w.getX();
			double y = w.getY();
			double width = w.getWidth();
			double height = w.getHeight();
			
			// TODO center around parent window, but not outside of the current device
			// or move it to FxFramework...
		}
	}
	
	
	public FxButtonPane buttonPane()
	{
		Node n = getBottom();
		if(n instanceof FxButtonPane)
		{
			return (FxButtonPane)n;
		}
		
		FxButtonPane p = new FxButtonPane();
		p.setPadding(new Insets(10));
		setBottom(p);
		return p;
	}
	
	
	protected void setResult(T result)
	{
		this.result = result;
		close();
	}
	
	
	public FxButton addButton(String text, CssStyle style, T result)
	{
		FxButton b = new FxButton(text, style, () -> setResult(result));
		buttonPane().add(b);
		return b;
	}
	
	
	public FxButton addButton(String text, T result)
	{
		FxButton b = new FxButton(text, () -> setResult(result));
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
	
	
	public T open(T defaultValue)
	{
		double w = getWidth();
		double h = getHeight();
		
		// FIX what's going on here?
		if(isInvalid(w))
		{
			w = 400;
			setWidth(w);
		}
		
		if(isInvalid(h))
		{
			h = 300;
			setHeight(h);
		}
		
		// TODO center over parent, but not to go outside of the screen
		
		super.showAndWait();
		return result == null ? defaultValue : result;
	}
	
	
	protected static boolean isInvalid(double x)
	{
		if(Double.isNaN(x))
		{
			return true;
		}
		else if(x <= 1)
		{
			return true;
		}
		return false;
	}
	
	
	public void closeOnEscape()
	{
		KeyMap.onKeyPressed(getContentPane(), KeyCode.ESCAPE, this::close);
	}


	public void setContentText(String text)
	{
		pane.setCenter(new Label(text));
	}
}
