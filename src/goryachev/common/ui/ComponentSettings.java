// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.util.CSettings;
import goryachev.common.util.Obj;
import goryachev.common.util.Rex;
import java.awt.Component;
import javax.swing.JComponent;


/** per-component settings logic, allows for chaining */
public abstract class ComponentSettings
{
	public abstract void store(String prefix, CSettings s);
	
	public abstract void restore(String prefix, CSettings s);
	
	//
	
	public static final Object KEY = new Obj("ComponentSettings.KEY");
	public final String id;
	private ComponentSettings next;
	
	
	public ComponentSettings(String id)
	{
		this.id = id;
	}
	
	
	public void attach(JComponent c)
	{
		Object x = c.getClientProperty(KEY);
		if(x == null)
		{
			c.putClientProperty(KEY, this);
		}
		else if(x instanceof ComponentSettings)
		{
			((ComponentSettings)x).append(this);
		}
		else
		{
			throw new Rex("unable to attach, " + x);
		}
	}
	
	
	protected void append(ComponentSettings s)
	{
		while(s.next != null)
		{
			s = s.next;
		}
		
		s.next = this;
	}
	

	public static ComponentSettings get(Component c)
	{
		if(c instanceof JComponent)
		{
			Object v = ((JComponent)c).getClientProperty(KEY);
			if(v instanceof ComponentSettings)
			{
				return (ComponentSettings)v;
			}
		}
		return null;
	}
	
	
	public void storeSettings(String prefix, CSettings s)
	{
		store(prefix + "." + id, s);
		
		if(next != null)
		{
			next.storeSettings(prefix, s);
		}
	}
	
	
	public void restoreSettings(String prefix, CSettings s)
	{
		restore(prefix + "." + id, s);
		
		if(next != null)
		{
			next.restoreSettings(prefix, s);
		}
	}
}
