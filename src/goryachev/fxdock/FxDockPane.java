// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock;
import javafx.scene.layout.BorderPane;


/**
 * FxDockPane is a base class for all panes that can be placed in the docking framework.
 * 
 * When restoring the layout, concrete instances of FxDockPanes are created by the application FxDockWindow
 * implementation.
 */
public abstract class FxDockPane
	extends BorderPane
{
	private final String type;
	
	
	public FxDockPane(String type)
	{
		this.type = type;
	}
	
	
	public String getDockPaneType()
	{
		return type;
	}
}
