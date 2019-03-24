// Copyright © 2018-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Region;


/**
 * Fx ToolBar.
 */
public class FxToolBar
	extends ToolBar
{
	public FxToolBar()
	{
	}
	
	
	public FxButton button(String text, FxAction a)
	{
		FxButton b = new FxButton(text, a);
		getItems().add(b);
		return b;
	}
	
	
	public FxButton button(String text)
	{
		return button(text, FxAction.DISABLED);
	}
	
	
	public void fill()
	{
		// TODO
	}
	
	
	public void space()
	{
		// TODO does not seem to work.  need my own layout.
		Region r = new Region();
		r.setMinWidth(10);
		r.setPrefWidth(10);
		r.setMaxWidth(10);
		getItems().add(r);
	}
}
