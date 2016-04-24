// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock;
import javafx.stage.Stage;


/**
 * FxDockWindow.
 */
public abstract class FxDockWindow
	extends Stage
{
	public abstract FxDockPane createPane(String type);
	
	
	public FxDockWindow()
	{
	}
}
