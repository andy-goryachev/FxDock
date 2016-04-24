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
	
	public final FxDockRootPane root;
	
	
	public FxDockWindow()
	{
		root = new FxDockRootPane(this);
		Scene s = new Scene(root);
		setScene(s);
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
}
