// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.util.D;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.text.JTextComponent;


/** 
 * Application-wide component keeps track of last Window and editable JTextComponent.
 * This service is started by Application.startUI().
 */
public class CFocusMonitor
	implements PropertyChangeListener
{
	private Window lastWindow;
	private JTextComponent lastEditor;
	private Component lastComponent;
	private static CFocusMonitor monitor = register();
	

	protected CFocusMonitor()
	{
	}


	/** PropertyChangeListener */
	public void propertyChange(PropertyChangeEvent ev)
	{
		Object x = ev.getNewValue();
		if(x instanceof JTextComponent)
		{
			JTextComponent t = (JTextComponent)x;
			//if(t.isEditable())
			{
				lastEditor = t; 
			}
		}
		
		//D.simpleName(x);
		
		if(x instanceof Component)
		{
			lastComponent = (Component)x;
		}
		
		Window w = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusedWindow();
		if(w != lastWindow)
		{
			lastWindow = w;
		}
	}
	

	/** returns last editable editor text component */
	public static JTextComponent getLastTextComponent()
	{
		return monitor.lastEditor;
	}
	
	
	public static Component getLastComponent()
	{
		return monitor.lastComponent;
	}
	
	
	/** returns last window */
	public static Window getLastWindow()
	{
		return monitor.lastWindow;
	}
	

	private static CFocusMonitor register()
	{
		CFocusMonitor m = new CFocusMonitor();
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener("focusOwner", m);
		return m;
	}

	
	// called from Application.startUI
	// causes an instance to be loaded and registered
	public static void init()
	{
		monitor.hashCode();
	}
}
