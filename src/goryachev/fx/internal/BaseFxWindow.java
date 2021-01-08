// Copyright © 2020-2021 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.internal;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.Node;
import javafx.stage.Stage;


/**
 * Base FxWindow.
 */
public class BaseFxWindow
	extends Stage
{
	private final FxWindowBoundsMonitor normalBoundsMonitor = new FxWindowBoundsMonitor(this);
	private final static ReadOnlyObjectWrapper<Node> lastFocusOwner = new ReadOnlyObjectWrapper();

	
	public BaseFxWindow()
	{
		sceneProperty().addListener((src,prev,cur) ->
		{
			if(cur != null)
			{
				cur.focusOwnerProperty().addListener((s,p,val) -> updateFocusOwner(val));
			}
		});
	}
	
	
	protected void updateFocusOwner(Node n)
	{
		if(n != null)
		{
			lastFocusOwner.set(n);
		}
	}
	
	
	public static Node getLastFocusOwner()
	{
		return lastFocusOwner.get();
	}
	
	
	public static ReadOnlyObjectProperty<Node> lastFocusOwnerProperty()
	{
		return lastFocusOwner.getReadOnlyProperty();
	}

	
	public final double getNormalX()
	{
		return normalBoundsMonitor.getX();
	}
	
	
	public final double getNormalY()
	{
		return normalBoundsMonitor.getY();
	}
	
	
	public final double getNormalWidth()
	{
		return normalBoundsMonitor.getWidth();
	}
	
	
	public final double getNormalHeight()
	{
		return normalBoundsMonitor.getHeight();
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
}
