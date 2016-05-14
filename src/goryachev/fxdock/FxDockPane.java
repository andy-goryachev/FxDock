// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock;
import goryachev.fx.CAction;
import goryachev.fxdock.dnd.DragAndDropHandler;
import goryachev.fxdock.internal.DockTools;
import goryachev.fxdock.internal.FxDockBorderPane;
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
	public final Label titleField = new Label();
	private final ReadOnlyBooleanWrapper tabMode = new ReadOnlyBooleanWrapper();
	private final SimpleStringProperty title = new SimpleStringProperty();
	private final String type;
	
	
	public FxDockPane(String type)
	{
		this.type = type;
		
		titleField.textProperty().bindBidirectional(titleProperty());
		DragAndDropHandler.attach(titleField, this);

		updateToolBar();
		
		parent.addListener((s,old,cur) -> setTabMode(cur instanceof FxDockTabPane));
	}
	
	
	public final String getDockPaneType()
	{
		return type;
	}
	
	
	protected final void setTabMode(boolean on)
	{
		tabMode.set(on);
		updateToolBar();
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
	
	
	protected void updateToolBar()
	{
		Node tb;
		if(isTabMode())
		{
			// TODO custom toolbar components
			tb = null;
		}
		else
		{
			Button b = new Button("x");
			closeAction.attach(b);
			
			// TODO HPane
			// TODO custom toolbar components
			ToolBar t = new ToolBar();
			t.getItems().addAll(titleField, b);
			tb = t;
		}
		
		setTop(tb);
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
		// TODO
	}
	
	
	/** bind a property to be saved in tile-specific settings using the specified subkey, using subkey */
	public <T> void bind(String subKey, Property<T> p, StringConverter<T> c)
	{
		// TODO
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
