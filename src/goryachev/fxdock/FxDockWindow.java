// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock;
import goryachev.fx.CAction;
import goryachev.fxdock.internal.FxDockRootPane;
import goryachev.fxdock.internal.FxDockSchema;
import goryachev.fxdock.internal.FxWindowBase;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


/**
 * Base class for docking framework Stage.
 */
public abstract class FxDockWindow
	extends FxWindowBase
{
	public final CAction closeWindowAction = new CAction() { public void action() { close(); } };
	private final BorderPane frame;
	private final FxDockRootPane root;
	
	
	public FxDockWindow()
	{
		root = new FxDockRootPane(this);
		frame = new BorderPane(root);
		Scene s = new Scene(frame);
		setScene(s);
		
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
	
	
	public final FxDockRootPane getDockRootPane()
	{
		return root;
	}
	
	
	public final void setContent(Node n)
	{
		root.setContent(n);
	}
	
	
	public final Node getContent()
	{
		return root.getContent();
	}
	
	
	public final void setTop(Node n)
	{
		frame.setTop(n);
	}
	
	
	public final Node getTop()
	{
		return frame.getTop();
	}
	
	
	public final void setBottom(Node n)
	{
		frame.setBottom(n);
	}
	
	
	public final Node getBottom()
	{
		return frame.getBottom();
	}
	
	
	public final void setLeft(Node n)
	{
		frame.setLeft(n);
	}
	
	
	public final Node getLeft()
	{
		return frame.getLeft();
	}
	
	
	public final void setRight(Node n)
	{
		frame.setRight(n);
	}
	
	
	public final Node getRight()
	{
		return frame.getRight();
	}
	
	
	public void saveSettings()
	{
		FxDockFramework.saveLayout();
	}


	/** invoked by the framework after the window and its content is created. */
	public void loadSettings(String prefix)
	{
		FxDockSchema.loadContentSettings(prefix, getContent());
	}


	/** invoked by the framework as necessary to store the window-specific settings */
	public void saveSettings(String prefix)
	{
		FxDockSchema.saveContentSettings(prefix, getContent());
	}
}
