// Copyright © 2016-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock;
import goryachev.fx.FxAction;
import goryachev.fx.FxDump;
import goryachev.fx.OnWindowClosing;
import goryachev.fx.internal.BaseFxWindow;
import goryachev.fxdock.internal.FxDockRootPane;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


/**
 * Base class for docking framework Stage.
 */
public abstract class FxDockWindow
	extends BaseFxWindow
{
	/** 
	 * Override to ask the user to confirm closing of window.
	 * Make sure to check if the argument already has the user's choice and
	 * perform the necessary action.
	 * If a dialog must be shown, make sure to call toFront().
	 */
	public void confirmClosing(OnWindowClosing choice) { }
	
	//
	
	public final FxAction closeWindowAction = new FxAction(this::actionClose);
	private final BorderPane frame;
	private final FxDockRootPane root;
	
	
	public FxDockWindow()
	{
		root = new FxDockRootPane(this);
		frame = new BorderPane(root);
		
		Scene s = new Scene(frame);
		setScene(s);
		
		FxDump.attach(this);
	}
	
	
	public void open()
	{
		FxDockFramework.open(this);
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
	
	
	/** saves all windows */
	public void saveLayout()
	{
		FxDockFramework.saveLayout();
	}

	
	protected void actionClose()
	{
		OnWindowClosing ch = new OnWindowClosing(false);
		confirmClosing(ch);
		if(!ch.isCancelled())
		{
			close();
		}
	}
}
