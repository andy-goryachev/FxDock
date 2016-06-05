// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock;
import goryachev.fx.CAction;
import goryachev.fx.FX;
import goryachev.fx.SSConverter;
import goryachev.fx.internal.LocalBindings;
import goryachev.fxdock.internal.DockTools;
import goryachev.fxdock.internal.DragAndDropHandler;
import goryachev.fxdock.internal.FxDockBorderPane;
import goryachev.fxdock.internal.FxDockSchema;
import goryachev.fxdock.internal.FxDockTabPane;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.util.StringConverter;


/**
 * FxDockPane is a base class for all panes that can be placed in the docking framework.
 * 
 * When restoring the layout, concrete instances of FxDockPanes are created by the application FxDockWindow
 * implementation.
 */
public abstract class FxDockPane
	extends FxDockBorderPane
{
	public final CAction closeAction = new CAction() { public void action() { actionClose(); }};
	public final CAction popToWindowAction = new CAction() { public void action() { actionPopToWindow(); }}; 
	public final Label titleField;
	private final ReadOnlyBooleanWrapper tabMode = new ReadOnlyBooleanWrapper();
	private final SimpleStringProperty title = new SimpleStringProperty();
	private final String type;
	private LocalBindings bindings;
	
	
	public FxDockPane(String type)
	{
		this.type = type;
		
		FX.style(this, FxDockStyles.FX_DOCK_PANE);
		
		titleField = new Label();
		FX.style(titleField, FxDockStyles.TOOLBAR_TITLE);
		titleField.textProperty().bindBidirectional(titleProperty());
		DragAndDropHandler.attach(titleField, this);
		
		parent.addListener((s,old,cur) -> setTabMode(cur instanceof FxDockTabPane));
	}
	
	
	public final String getDockPaneType()
	{
		return type;
	}
	
	
	protected final void setTabMode(boolean on)
	{
		tabMode.set(on);
		
		Node tb = createToolBar(on);
		setTop(tb);
	}
	
	
	public final boolean isTabMode()
	{
		return tabModeProperty().get();
	}
	
	
	public final boolean isPaneMode()
	{
		return !tabModeProperty().get();
	}
	
	
	public final ReadOnlyBooleanProperty tabModeProperty()
	{
		 return tabMode.getReadOnlyProperty();
	}
	
	
	/** override to create your own toolbar, possibly with custom icons and buttons */
	protected Node createToolBar(boolean tabMode)
	{
		if(tabMode)
		{
			return null;
		}
		else
		{
			Button b = new Button("x");
			FX.style(b, FxDockStyles.TOOLBAR_CLOSE_BUTTON);
			closeAction.attach(b);
			
			ToolBar t = new ToolBar();
			FX.style(t, FxDockStyles.TOOLBAR);
			t.getItems().addAll(titleField, b);
			return t;
		}
	}
	
	
	public final void setTitle(String s)
	{
		titleProperty().set(s);
	}
	
	
	public final String getTitle()
	{
		return titleProperty().get();
	}
	
	
	public final SimpleStringProperty titleProperty()
	{
		return title;
	}
	
	
	/** bind a property to be saved in tile-specific settings using the specified subkey */
	public <T> void bind(String subKey, Property<T> p)
	{
		bindings().add(subKey, p, null);
	}
	
	
	/** bind a property to be saved in tile-specific settings using the specified subkey, using subkey */
	public <T> void bind(String subKey, Property<T> p, StringConverter<T> c)
	{
		bindings().add(subKey, p, c);
	}
	
	
	/** bind a property to be saved in tile-specific settings using the specified subkey, using subkey */
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
	
	
	/** called by the framework to load values */
	public void loadPaneSettings(String prefix)
	{
		if(bindings != null)
		{
			String k = FxDockSchema.getPath(prefix, this, FxDockSchema.SUFFIX_BINDINGS);
			bindings.loadValues(k);
		}
	}
	
	
	/** called by the framework to save values */
	public void savePaneSettings(String prefix)
	{
		if(bindings != null)
		{
			String k = FxDockSchema.getPath(prefix, this, FxDockSchema.SUFFIX_BINDINGS);
			bindings.saveValues(k);
		}
	}
	
	
	/** 
	 * while FxDockPane extends BorderPane, it's better to insert a custom content using this method only,
	 * because the top is used by the toolbar.
	 */
	public void setContent(Node n)
	{
		setCenter(n);
	}
	
	
	public void actionClose()
	{
		DockTools.remove(this);
	}
	
	
	public void actionPopToWindow()
	{
		DockTools.moveToNewWindow(this);
	}
}
