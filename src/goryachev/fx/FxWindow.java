// Copyright © 2016-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.fx.settings.WindowMonitor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


/**
 * Convenient FX Stage.
 */
public class FxWindow
	extends Stage
{
	private final BorderPane contentPane;
	
	
	public FxWindow(String name)
	{
		this.contentPane = new BorderPane();
		FX.setName(this, name);
		
		Scene sc = new Scene(contentPane);
		setScene(sc);
	}
	
	
	protected BorderPane getContentPane()
	{
		return contentPane;
	}
	
	
	public void open()
	{
		show();
	}
	
	
	public void setTop(Node n)
	{
		contentPane.setTop(n);
	}
	
	
	public Node getTop()
	{
		return contentPane.getTop();
	}
	
	
	public void setBottom(Node n)
	{
		contentPane.setBottom(n);
	}
	
	
	public Node getBottom()
	{
		return contentPane.getBottom();
	}
	
	
	public void setLeft(Node n)
	{
		contentPane.setLeft(n);
	}
	
	
	public Node getLeft()
	{
		return contentPane.getLeft();
	}
	
	
	public void setRight(Node n)
	{
		contentPane.setRight(n);
	}
	
	
	public Node getRight()
	{
		return contentPane.getRight();
	}
	
	
	public void setCenter(Node n)
	{
		contentPane.setCenter(n);
	}
	
	
	public Node getCenter()
	{
		return contentPane.getCenter();
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
	}
	
	
	public void setMaxSize(double width, double height)
	{
		setMaxWidth(width);
		setMaxHeight(height);
	}
	
	
	public void setClosingWindowOperation(ClosingWindowOperation op)
	{
		WindowMonitor.setClosingWindowOperation(this, op);
	}
	
	
	public void setNonEssentialWindow()
	{
		WindowMonitor.setNonEssentialWindow(this);
	}
	
	
	@Override
	public String toString()
	{
		return "FxWindow{" + FX.getName(this) + "." + hashCode() + "}";
	}
}
