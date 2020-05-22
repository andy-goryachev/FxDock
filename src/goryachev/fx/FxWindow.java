// Copyright © 2016-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.fx.internal.FxSchema;
import goryachev.fx.internal.FxWindowBoundsMonitor;
import goryachev.fx.internal.LocalSettings;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


/**
 * Fx Window.
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
	
	//
	
	public final FxAction closeWindowAction = new FxAction(this::closeWithConfirmation);
	private final String name;
	private final BorderPane pane;
	private final FxWindowBoundsMonitor normalBoundsMonitor = new FxWindowBoundsMonitor(this);
	private final static ReadOnlyObjectWrapper<Node> lastFocusOwner = new ReadOnlyObjectWrapper();
	private LocalSettings settings;
	
	
	public FxWindow(String name)
	{
		this.name = name;
		this.pane = new BorderPane();
		
		Scene sc = new Scene(pane);
		setScene(sc);
		
		sc.focusOwnerProperty().addListener((s,p,val) -> updateFocusOwner(val));
	}
	
	
	protected void updateFocusOwner(Node n)
	{
		if(n != null)
		{
			lastFocusOwner.set(n);
		}
	}
	
	
	public Node getLastFocusOwner()
	{
		return lastFocusOwner.get();
	}
	
	
	public static ReadOnlyObjectProperty<Node> lastFocusOwnerProperty()
	{
		return lastFocusOwner.getReadOnlyProperty();
	}
	
	
	public String getName()
	{
		return name;
	}
	
	
	public double getNormalX()
	{
		return normalBoundsMonitor.getX();
	}
	
	
	public double getNormalY()
	{
		return normalBoundsMonitor.getY();
	}
	
	
	public double getNormalWidth()
	{
		return normalBoundsMonitor.getWidth();
	}
	
	
	public double getNormalHeight()
	{
		return normalBoundsMonitor.getHeight();
	}
	
	
	public void open()
	{
		FX.open(this);
	}
	
	
	public void setTop(Node n)
	{
		pane.setTop(n);
	}
	
	
	public void setBottom(Node n)
	{
		pane.setBottom(n);
	}
	
	
	public void setLeft(Node n)
	{
		pane.setLeft(n);
	}
	
	
	public void setRight(Node n)
	{
		pane.setRight(n);
	}
	
	
	public void setCenter(Node n)
	{
		pane.setCenter(n);
	}
	
	
	public Node getCenter()
	{
		return pane.getCenter();
	}
	
	
	public void setSize(int width, int height)
	{
		setWidth(width);
		setHeight(height);
	}
	
	
	public void setMinSize(int width, int height)
	{
		setMinWidth(width);
		setMinHeight(height);
	}
	
	
	public void setMaxSize(int width, int height)
	{
		setMaxWidth(width);
		setMaxHeight(height);
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
	

	/** returns a window-specific local settings instance */
	public LocalSettings localSettings()
	{
		if(settings == null)
		{
			settings = new LocalSettings();
		}
		return settings;
	}
	

	/** invoked by the framework after the window and its content is created. */
	public void loadSettings(String prefix)
	{
		if(settings != null)
		{
			String k = prefix + FxSchema.SFX_BINDINGS;
			settings.loadValues(k);
		}
	}


	/** invoked by the framework as necessary to store the window-specific settings */
	public void storeSettings(String prefix)
	{
		if(settings != null)
		{
			String k = prefix + FxSchema.SFX_BINDINGS;
			settings.saveValues(k);
		}
	}
}
