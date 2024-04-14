// Copyright © 2018-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;


/**
 * Fx ToolBar.
 */
public class FxToolBar
	extends ToolBar
{
	public static final CssStyle STYLE = new CssStyle("FxToolBar_STYLE");
	
	
	public FxToolBar()
	{
		FX.style(this, STYLE);
	}
	
	
	public void add(Node n)
	{
		getItems().add(n);
	}
	
	
	public FxButton addButton(String text, FxAction a)
	{
		FxButton b = new FxButton(text, a);
		getItems().add(b);
		return b;
	}
	
	
	public FxButton addButton(String text, CssStyle style, FxAction a)
	{
		FxButton b = new FxButton(text, style, a);
		getItems().add(b);
		return b;
	}
	
	
	public FxButton addButton(String text, Runnable r)
	{
		FxButton b = new FxButton(text,  r);
		getItems().add(b);
		return b;
	}
	
	
	public FxButton addButton(String text)
	{
		return addButton(text, FxAction.DISABLED);
	}
	
	
	public void fill()
	{
		Pane p = new Pane();
		HBox.setHgrow(p, Priority.SOMETIMES);
		add(p);
	}
	
	
	public void fill(Node n)
	{
		HBox.setHgrow(n, Priority.SOMETIMES);
		add(n);
	}
	
	
	public void space()
	{
		space(10);
	}
	
	
	public void space(int space)
	{
		Pane p = new Pane();
		p.setMinWidth(space);
		p.setPrefWidth(space);
		p.setMaxWidth(space);
		
		add(p);
	}


	public FxToggleButton addToggleButton(String text, Property<Boolean> prop)
	{
		FxToggleButton b = new FxToggleButton(text, prop);
		add(b);
		return b;
	}
	
	
	public FxToggleButton addToggleButton(String text, String tooltip, Property<Boolean> prop)
	{
		FxToggleButton b =new FxToggleButton(text, tooltip, prop);
		add(b);
		return b;
	}
	
	
	public void clear()
	{
		getItems().clear();
	}
}
