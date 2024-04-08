// Copyright © 2016-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


/**
 * Fx Window.
 * 
 * It is highly recommended not to show a Window again after it has been closed.
 */
public class FxWindow
	extends Stage
{
	/** 
	 * Override to ask the user to confirm closing of window.
	 * Make sure to check if the argument already has the user's choice and
	 * perform the necessary action.
	 * If a dialog must be shown, make sure to call toFront().
	 */
	public void confirmClosing(OnWindowClosing choice) { }
	
	/** closing last essential window exits the application, regardless of the number of open non-essential windows */
	public boolean isEssentialWindow() { return true; }
	
	//
	
	protected final BorderPane pane;
	
	
	public FxWindow(String name)
	{
		this.pane = new BorderPane();
		FX.setName(this, name);
		
		Scene sc = new Scene(pane);
		setScene(sc);
	}
	
	
	public void open()
	{
		show();
	}
	
	
	public final FxAction closeWindowAction()
	{
		return new FxAction(this::closeWithConfirmation);
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
	
	
	public Parent getContentPane()
	{
		return getScene().getRoot();
	}
	
	
	public void setSize(double width, double height)
	{
		setWidth(width);
		setHeight(height);
	}
	
	
	public void setMinSize(double width, double height)
	{
		setMinWidth(width);
		setMinHeight(height);
		
		setSize(width, height);
	}
	
	
	public void setMaxSize(double width, double height)
	{
		setMaxWidth(width);
		setMaxHeight(height);
	}
	

	// TODO change to setClosingPolicy()
	public void closeWithConfirmation()
	{
		OnWindowClosing ch = new OnWindowClosing(false);
		confirmClosing(ch);
		if(!ch.isCancelled())
		{
			close();
		}
	}
	
	
	public String toString()
	{
		return "FxWindow{" + FX.getName(this) + "." + hashCode() + "}";
	}
}
