// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock;
import goryachev.fx.CAction;
import goryachev.fxdock.internal.FxDockRootPane;
import goryachev.fxdock.internal.FxWindowBase;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


/**
 * FxDockWindow.
 */
public abstract class FxDockWindow
	extends FxWindowBase
{
	/** 
	 * invoked by the framework after the window and its content is created.
	 * subclasses should override this method to retrieve window-specific parameters via querying the
	 * global settings:
	 * GlobalSettings.getXXX(prefix + ".SomeKey");
	 */
	public void loadSettings(String prefix) { }
	
	/**
	 * invoked by the framework when necessary to allow the window to store window-specific settings:
	 * GlobalSettings.setXXX(prefix + ".SomeKey", value);
	 */
	public void saveSettings(String prefix) { }
	
	//
	
	public final CAction closeWindowAction = new CAction() { public void action() { close(); } };
	private final BorderPane frame;
	private final FxDockRootPane root;
	
	
	public FxDockWindow()
	{
		root = new FxDockRootPane(this);
		frame = new BorderPane(root);
		Scene s = new Scene(frame);
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
	
	
	public void saveWindowSettings()
	{
		FxDockFramework.storeWindow(this);
	}
}
