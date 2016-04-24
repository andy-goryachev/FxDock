// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.Log;
import goryachev.common.util.Rex;
import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import javax.swing.Action;
import javax.swing.text.JTextComponent;


// Focus traversal policy with a pre-defined order.
public class CFocusTraversalPolicy
	extends FocusTraversalPolicy
{
	private CList<Component> components = new CList();
	private Component defaultComponent;


	public CFocusTraversalPolicy()
	{
	}
	
	
	public CFocusTraversalPolicy(Container c)
	{
		apply(c);
	}
	
	
	/** applies the policy to a container */
	public void apply(Container c)
	{
		c.setFocusCycleRoot(true);
		c.setFocusTraversalPolicy(this);
	}
	
	
	/** adds a component to this focus traversal policy */
	public void add(Component c)
	{
		components.add(c);
	}
	
	
	public void add(Container parent, Action a)
	{
		Component c = UI.findByAction(parent, a);
		if(c == null)
		{
			throw new Rex("unable to find component by action " + a);
		}
			
		add(c);
	}
	
	
	/** adds components in the specified order to this focus traversal policy */
	public void addAll(Component ... cs)
	{
		for(Component c: cs)
		{
			add(c);
		}
	}
	
	
	public int size()
	{
		return components.size();
	}
	
	
	protected int indexOf(Component c)
	{
		do
		{
			for(int i=0; i<size(); i++)
			{
				if(components.get(i) == c)
				{
					return i;
				}
			}
			
			c = c.getParent();
			
		} while(c != null);
		return 0;
	}
	
	
	protected Component get(int ix, int delta)
	{
		int sz = size();
		
		// find a focusable component
		try
		{
			for(int i=0; i<sz; i++)
			{
				int ci = CKit.mod(ix + (i + 1)*delta, sz);
				Component c = components.get(ci);
				if(canFocus(c))
				{
					return c;
				}
			}
		}
		catch(Exception e)
		{
			Log.err(e);
		}
		
		return components.get(0);
	}
	
	
	protected boolean canFocus(Component c)
	{
		if(!c.isFocusable())
		{
			return false;
		}
		
		if(!c.isEnabled())
		{
			return false;
		}
		
		if(!c.isVisible())
		{
			return false;
		}
		
		if(!c.isValid())
		{
			return false;
		}
		
		if(c instanceof JTextComponent)
		{
			if(!((JTextComponent)c).isEditable())
			{
				return false;
			}
		}
		
		return true;
	}


	public Component getComponentAfter(Container cc, Component c)
	{
		int ix = indexOf(c);
		return get(ix, 1);
	}


	public Component getComponentBefore(Container cc, Component c)
	{
		int ix = indexOf(c);
		return get(ix, - 1);
	}


	public Component getDefaultComponent(Container cc)
	{
		if(defaultComponent != null)
		{
			return defaultComponent;
		}
		return getFirstComponent(cc);
	}


	public Component getFirstComponent(Container cc)
	{
		return get(0, 0);
	}


	public Component getLastComponent(Container cc)
	{
		return get(size() - 1, 0);
	}
	
	
	public void setDefaultComponent(Component c)
	{
		defaultComponent = c;
	}
	
	
	public Component getDefaultComponent()
	{
		return defaultComponent;
	}
}
