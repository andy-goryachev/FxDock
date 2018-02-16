// Copyright © 2016-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock;
import goryachev.fx.CAction;
import goryachev.fx.FxDump;
import goryachev.fx.OnWindowClosing;
import goryachev.fx.SSConverter;
import goryachev.fx.internal.FxWindowBoundsMonitor;
import goryachev.fx.internal.LocalBindings;
import goryachev.fxdock.internal.FxDockRootPane;
import goryachev.fxdock.internal.FxDockSchema;
import goryachev.fxdock.internal.FxWindowBase;
import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;


/**
 * Base class for docking framework Stage.
 */
public abstract class FxDockWindow
	extends FxWindowBase
{
	/** 
	 * Override to ask the user to confirm closing of window.
	 * Make sure to check if the argument already has the user's choice and
	 * perform the necessary action.
	 * If a dialog must be shown, make sure to call toFront().
	 */
	public void confirmClosing(OnWindowClosing choice) { }
	
	//
	
	public final CAction closeWindowAction = new CAction() { public void action() { actionClose(); } };
	private final BorderPane frame;
	private final FxDockRootPane root;
	private LocalBindings bindings;
	private final FxWindowBoundsMonitor normalBoundsMonitor = new FxWindowBoundsMonitor(this);
	
	
	public FxDockWindow()
	{
		root = new FxDockRootPane(this);
		frame = new BorderPane(root);
		Scene s = new Scene(frame);
		setScene(s);
		
		new FxDump().attach(this);
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
	
	
	/** save all windows */
	public void saveLayout()
	{
		FxDockFramework.saveLayout();
	}


	/** invoked by the framework after the window and its content is created. */
	public void loadSettings(String prefix)
	{
		if(bindings != null)
		{
			String k = prefix + FxDockSchema.SUFFIX_BINDINGS;
			bindings.loadValues(k);
		}
		
		FxDockSchema.loadContentSettings(prefix, getContent());
	}


	/** invoked by the framework as necessary to store the window-specific settings */
	public void storeSettings(String prefix)
	{
		if(bindings != null)
		{
			String k = prefix + FxDockSchema.SUFFIX_BINDINGS;
			bindings.saveValues(k);
		}
		
		FxDockSchema.saveContentSettings(prefix, getContent());
	}
	
	
	/** bind a property to be saved in tile-specific settings using the specified subkey */
	public <T> void bind(String subKey, Property<T> p)
	{
		bindings().add(subKey, p, null);
	}
	
	
	/** bind a property to be saved in tile-specific settings using the specified subkey */
	public <T> void bind(String subKey, Property<T> p, StringConverter<T> c)
	{
		bindings().add(subKey, p, c);
	}
	
	
	/** bind a property to be saved in tile-specific settings using the specified subkey */
	public <T> void bind(String subKey, Property<T> p, SSConverter<T> c)
	{
		bindings().add(subKey, c, p);
	}
	
	
	protected LocalBindings bindings()
	{
		if(bindings == null)
		{
			bindings = new LocalBindings();
		}
		return bindings;
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
