// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.Action;


public class DelegateAction
	extends AbstractAction 
	implements PropertyChangeListener
{
	private Action delegate;
	
	
	public DelegateAction(Action a)
	{
		setDelegate(a);
	}
	
	
	public DelegateAction()
	{
	}
	
	
	public void clearDelegate()
	{
		setDelegate(null);
	}
	
	
	public void setDelegate(Action a)
	{
		if(delegate != null)
		{
			delegate.removePropertyChangeListener(this);
		}
		
		delegate = a;
		
		if(delegate == null)
		{
			setEnabled(false);
		}
		else
		{
			delegate.addPropertyChangeListener(this);
			setEnabled(delegate.isEnabled());
		}
	}


	public void actionPerformed(ActionEvent ev)
	{
		if(delegate != null)
		{
			delegate.actionPerformed(ev);
		}
	}
	
	
	public boolean isEnabled()
	{
		return delegate == null ? false : delegate.isEnabled();
	}


	public void propertyChange(PropertyChangeEvent ev)
	{
		if("enabled".equals(ev.getPropertyName()))
		{
			if(ev.getSource() == delegate)
			{
				setEnabled(delegate.isEnabled());
			}
		}
	}
}
