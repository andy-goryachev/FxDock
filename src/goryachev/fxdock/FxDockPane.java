// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fxdock;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.BorderPane;


/**
 * FxDockPane is a base class for all panes that can be placed in the docking framework.
 * 
 * When restoring the layout, concrete instances of FxDockPanes are created by the application FxDockWindow
 * implementation.
 */
public abstract class FxDockPane
	extends BorderPane
{
	private final String type;
	private ReadOnlyBooleanWrapper tabMode;
	private final SimpleStringProperty title = new SimpleStringProperty();
	
	
	public FxDockPane(String type)
	{
		this.type = type;
	}
	
	
	public final String getDockPaneType()
	{
		return type;
	}
	
	
	protected final void setTabMode()
	{
		tabModePropertyPrivate().set(true);
	}
	
	
	protected final void setPaneMode()
	{
		tabModePropertyPrivate().set(false);
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
		// TODO
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
