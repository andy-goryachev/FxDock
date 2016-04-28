// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock;
import goryachev.fx.CAction;
import goryachev.fxdock.internal.FxDockRootPane;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * FxDockWindow.
 */
public abstract class FxDockWindow
	extends Stage
{
	public abstract FxDockPane createPane(String type);
	
	//
	
	public final CAction closeWindowAction = new CAction() { public void action() { close(); } };
	// TODO two panels: outer and dock root
	public final FxDockRootPane root;
	
	
	public FxDockWindow()
	{
		root = new FxDockRootPane(this);
		Scene s = new Scene(root);
		setScene(s);
		
		// TODO add listeners for maximized/iconified/fullscreen mode to record size and location
		
		FxDockFramework.addFocusListener(this);
	}
	
	
	public void open()
	{
		FxDockFramework.open(this);
	}
	
	
	public void setMinSize(double w, double h)
	{
		setMinWidth(w);
		setMinHeight(h);
	}
	
	
	public void setContent(Node n)
	{
		root.setContent(n);
	}
	
	
	public Node getContent()
	{
		return root.getContent();
	}
}
