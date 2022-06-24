// Copyright © 2016-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.log.Log;
import java.util.function.Consumer;
import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleButton;


/**
 * An AbstractAction equivalent for FX, using method references.
 * 
 * Usage:
 *    public final FxAction backAction = new FxAction(this::actionBack);
 */
public class FxAction
    implements EventHandler<ActionEvent>
{
	protected static final Log log = Log.get("FxAction");
	public static final FxAction DISABLED = new FxAction(null, false);
	private final FxBoolean selectedProperty = new FxBoolean();
	private final FxBoolean disabledProperty = new FxBoolean();
	private Runnable onAction;
	private Consumer<Boolean> onSelected;
	
	
	public FxAction(Runnable onAction, Consumer<Boolean> onSelected, boolean enabled)
	{
		this.onAction = onAction;
		this.onSelected = onSelected;
		setEnabled(enabled);
		
		if(onSelected != null)
		{
			selectedProperty.addListener((src,prev,cur) -> fireSelected(cur)); 
		}
	}
	
	
	public FxAction(Runnable onAction, Consumer<Boolean> onSelected)
	{
		this(onAction, onSelected, true);
	}
	
	
	public FxAction(Runnable onAction, boolean enabled)
	{
		this(onAction, null, enabled);
	}
	
	
	public FxAction(Runnable onAction)
	{
		this.onAction = onAction;
	}
	
	
	public FxAction()
	{
	}
	
	
	public void setOnAction(Runnable r)
	{
		onAction = r;
	}
	
	
	protected final void invokeAction()
	{
		if(onAction != null)
		{
			try
			{
				onAction.run();
			}
			catch(Throwable e)
			{
				log.error(e);
			}
		}
	}


	public void attach(ButtonBase b)
	{
		b.setOnAction(this);
		b.disableProperty().bind(disabledProperty());

		if(b instanceof ToggleButton)
		{
			((ToggleButton)b).selectedProperty().bindBidirectional(selectedProperty());
		}
	}


	public void attach(MenuItem m)
	{
		m.setOnAction(this);
		m.disableProperty().bind(disabledProperty());

		if(m instanceof CheckMenuItem)
		{
			((CheckMenuItem)m).selectedProperty().bindBidirectional(selectedProperty());
		}
		else if(m instanceof RadioMenuItem)
		{
			((RadioMenuItem)m).selectedProperty().bindBidirectional(selectedProperty());
		}
	}


	public final BooleanProperty selectedProperty()
	{
		return selectedProperty;
	}


	public final boolean isSelected()
	{
		return selectedProperty.get();
	}


	public final void setSelected(boolean on)
	{
		if(selectedProperty.get() != on)
		{
			selectedProperty.set(on);
			fire();
		}
	}


	public final BooleanProperty disabledProperty()
	{
		return disabledProperty;
	}


	public final boolean isDisabled()
	{
		return disabledProperty.get();
	}


	public final void setDisabled(boolean on)
	{
		disabledProperty.set(on);
	}
	
	
	public final boolean isEnabled()
	{
		return !isDisabled();
	}
	
	
	public final void setEnabled(boolean on)
	{
		disabledProperty.set(!on);
	}
	
	
	public final void enable()
	{
		setEnabled(true);
	}
	
	
	public final void disable()
	{
		setEnabled(false);
	}
	
	
	/** fire onAction handler only if this action is enabled */
	public void fire()
	{
		if(isEnabled())
		{
			handle(null);
		}
	}
	
	
	/** execute an action regardless of whether its enabled or not */
	public void execute()
	{
		try
		{
			invokeAction();
		}
		catch(Throwable e)
		{
			log.error(e);
		}
	}
	
	
	protected void fireSelected(boolean on)
	{
		try
		{
			onSelected.accept(on);
		}
		catch(Throwable e)
		{
			log.error(e);
		}
	}


	/** override to obtain the ActionEvent */
	public void handle(ActionEvent ev)
	{
		if(isEnabled())
		{
			if(ev != null)
			{
				if(ev.getSource() instanceof Menu)
				{
					if(ev.getSource() != ev.getTarget())
					{
						// selection of a cascading child menu triggers action event for the parent 
						// for some unknown reason.  ignore this.
						return;
					}
				}
				
				ev.consume();
			}
			
			execute();
			
			// close popup menu, if applicable
			if(ev != null)
			{
				Object src = ev.getSource();
				if(src instanceof Menu)
				{
					ContextMenu p = ((Menu)src).getParentPopup();
					if(p != null)
					{
						p.hide();
					}
				}
			}
		}
	}
}
