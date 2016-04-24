// Copyright (c) 2005-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.KeyStroke;


public abstract class CAction
	extends AbstractAction
{
	public abstract void action() throws Exception;
	
	//
	
	private String tooltip;
	private ActionEvent event;
	
	@Deprecated // the user should implement the real functionality
	public static final CAction TODO = new CAction() 
	{
		public void action() { }
		public boolean isEnabled() { return false; }
	};


	public CAction()
	{
	}


	public CAction(String title)
	{
		super(title);
	}


	public CAction(Icon icon)
	{
		super(null, icon);
	}


	public CAction(Icon img, String title)
	{
		super(title, img);
	}


	public CAction(Icon img, String title, String tooltip)
	{
		this(img, title);
		this.tooltip = tooltip;
	}


	public CAction(Icon img, String title, boolean enabled)
	{
		super(title, img);
		setEnabled(enabled);
	}


	public CAction(String name, KeyStroke ks)
	{
		this(name);
		setAccelerator(ks);
	}


	public CAction(Icon img, String name, Accelerator a)
	{
		this(img, name);
		a.set(this);
	}


	public CAction(String name, Accelerator a)
	{
		this(name);
		a.set(this);
	}


	public void actionPerformed(ActionEvent ev)
	{
		event = ev;
		try
		{
			action();
		}
		catch(Exception e)
		{
			Dialogs.err(getSourceWindow(), e);
		}
		finally
		{
			event = null;
		}
	}


	public void fire()
	{
		if(isEnabled())
		{
			actionPerformed(null);
		}
	}


	public void setText(String s)
	{
		setName(s);
	}


	public void setName(String s)
	{
		putValue(Action.NAME, s);
	}


	public String getName()
	{
		return (String)getValue(Action.NAME);
	}


	public void setIcon(Icon icon)
	{
		putValue(Action.SMALL_ICON, icon);
	}


	public Icon getIcon()
	{
		Object x = getValue(Action.SMALL_ICON);
		if(x instanceof Icon)
		{
			return (Icon)x;
		}
		return null;
	}


	public String getToolTipText()
	{
		return tooltip == null ? getName() : tooltip;
	}


	public void setAccelerator(KeyStroke ks)
	{
		putValue(Action.ACCELERATOR_KEY, ks);
	}


	public void setSelected(boolean on)
	{
		putValue(Action.SELECTED_KEY, on);
	}


	public boolean isSelected()
	{
		return Boolean.TRUE.equals(getValue(Action.SELECTED_KEY));
	}


	public boolean toggleSelected()
	{
		boolean on = !isSelected();
		setSelected(on);
		return on;
	}


	/** returns action source component.  for parent window, use getSourceWindow() */
	public Component getSourceComponent()
	{
		if(event == null)
		{
			return CFocusMonitor.getLastWindow();
		}

		if(event.getSource() instanceof Component)
		{
			return (Component)event.getSource();
		}
		return null;
	}


	/** returns source window even when the actual source component is a JMenuItem (which is not connected to a parent window) */
	public Window getSourceWindow()
	{
		return UI.findParentWindow(getSourceComponent());
	}
}
