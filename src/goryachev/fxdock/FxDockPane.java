// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock;
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
	private final String type;
	private ReadOnlyBooleanWrapper tabMode;
	private final SimpleStringProperty title = new SimpleStringProperty();
	
	
	public FxDockPane(String type)
	{
		this.type = type;
		updateToolBar();
		
		parent.addListener((s,old,cur) -> setTabMode(cur instanceof FxDockTabPane));
	}
	
	
	public final String getDockPaneType()
	{
		return type;
	}
	
	
	protected final void setTabMode(boolean on)
	{
		tabModePropertyPrivate().set(on);
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
		 return tabModePropertyPrivate().getReadOnlyProperty();
	}
	
	
	protected final ReadOnlyBooleanWrapper tabModePropertyPrivate()
	{
		if(tabMode == null)
		{
			tabMode = new ReadOnlyBooleanWrapper()
			{
				protected void invalidated()
				{
					updateToolBar();
				}


				public Object getBean()
				{
					return FxDockPane.this;
				}


				public String getName()
				{
					return "tabMode";
				}
			};
		}
		return tabMode;
	}
	
	
	protected void updateToolBar()
	{
		Node tb;
		if(isTabMode())
		{
			tb = null;
		}
		else
		{
			Label label = new Label();
			label.textProperty().bindBidirectional(titleProperty());
			
			Button close = new Button("x");
			// TODO close
			
			ToolBar t = new ToolBar();
			t.getItems().addAll(label, close);
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
}
