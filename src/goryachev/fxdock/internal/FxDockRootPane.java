// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock.internal;
import goryachev.fxdock.FxDockWindow;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;


/**
 * FxDockRootPane.
 */
public class FxDockRootPane
	extends BorderPane
{
	private final FxDockWindow window;
	
	
	public FxDockRootPane(FxDockWindow w)
	{
		this.window = w;
		setContent(new FxDockEmptyPane());
	}
	
	
	public void setContent(Node n)
	{
		setCenter(n);
	}
	
	
	public Node getContent()
	{
		return getCenter();
	}
}
