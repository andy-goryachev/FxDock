// Copyright © 2016-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.fx.internal.BaseFxWindow;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


/**
 * Fx Window.
 * 
 * It is highly recommended not to show a Window again after it has been closed.
 */
public class FxWindow
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
	
	public final FxAction closeWindowAction = new FxAction(this::closeWithConfirmation);
	private final String name;
	protected final BorderPane pane;
	
	
	public FxWindow(String name)
	{
		this.name = name;
		this.pane = new BorderPane();
		
		Scene sc = new Scene(pane);
		setScene(sc);
	}
	
	
	public String getName()
	{
		return name;
	}
	
	
	public void open()
	{
		FX.open(this);
	}
	
	
	public void setTop(Node n)
	{
		pane.setTop(n);
	}
	
	
	public Node getTop()
	{
		return pane.getTop();
	}
	
	
	public void setBottom(Node n)
	{
		pane.setBottom(n);
	}
	
	
	public Node getBottom()
	{
		return pane.getBottom();
	}
	
	
	public void setLeft(Node n)
	{
		pane.setLeft(n);
	}
	
	
	public Node getLeft()
	{
		return pane.getLeft();
	}
	
	
	public void setRight(Node n)
	{
		pane.setRight(n);
	}
	
	
	public Node getRight()
	{
		return pane.getRight();
	}
	
	
	public void setCenter(Node n)
	{
		pane.setCenter(n);
	}
	
	
	public Node getCenter()
	{
		return pane.getCenter();
	}
	
	
	public void closeWithConfirmation()
	{
		OnWindowClosing ch = new OnWindowClosing(false);
		confirmClosing(ch);
		if(!ch.isCancelled())
		{
			close();
		}
	}
}
