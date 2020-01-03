// Copyright © 2018-2020 Andy Goryachev <andy@goryachev.com>
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
	public FxToolBar()
	{
	}
	
	
	public void add(Node n)
	{
		getItems().add(n);
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
		Pane p = new Pane();
		HBox.setHgrow(p, Priority.SOMETIMES);
		add(p);
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


	public FxToggleButton toggleButton(String text, Property<Boolean> prop)
	{
		FxToggleButton b = new FxToggleButton(text, prop);
		add(b);
		return b;
	}
}
