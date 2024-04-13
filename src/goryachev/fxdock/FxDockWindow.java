// Copyright © 2016-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock;
import goryachev.fx.FxDump;
import goryachev.fx.FxWindow;
import goryachev.fxdock.internal.FxDockRootPane;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


/**
 * Base class for docking framework Stage.
 */
public class FxDockWindow
	extends FxWindow
{
	private final BorderPane frame;
	private final FxDockRootPane root;
	
	
	public FxDockWindow(String name)
	{
		super(name);
		
		root = new FxDockRootPane(this);
		frame = new BorderPane(root);
		
		Scene s = new Scene(frame);
		setScene(s);
		
		FxDump.attach(this);
	}
	
	
	public void open()
	{
		show();
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
}
