// Copyright © 2016-2025 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock;
import goryachev.fx.FX;
import goryachev.fx.FxAction;
import goryachev.fxdock.internal.DockTools;
import goryachev.fxdock.internal.DragAndDropHandler;
import goryachev.fxdock.internal.FxDockBorderPane;
import goryachev.fxdock.internal.FxDockTabPane;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;


/**
 * FxDockPane is a base class for all panes that can be placed in the docking framework.
 * 
 * When restoring the layout, concrete instances of FxDockPanes are created by the application FxDockWindow
 * implementation.
 */
public abstract class FxDockPane
	extends FxDockBorderPane
{
	public final FxAction closeAction = new FxAction(this::actionClose);
	public final FxAction popToWindowAction = new FxAction(this::actionPopToWindow); 
	public final Label titleField;
	private final ReadOnlyBooleanWrapper tabMode = new ReadOnlyBooleanWrapper();
	private final SimpleStringProperty title = new SimpleStringProperty();
	private final String type;
	
	
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
