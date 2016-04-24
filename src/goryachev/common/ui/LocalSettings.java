// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.util.CMap;
import goryachev.common.util.Obj;
import goryachev.common.util.Rex;
import java.awt.Component;
import java.awt.Container;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;


public class LocalSettings
{
	private CMap<String,Component> components = new CMap();
	private static final Obj KEY = new Obj("LocalSettings");
	
	
	public LocalSettings(Container parent)
	{
		attach(parent);
	}
	
	
	public LocalSettings(Container parent, Object ... cs)
	{
		attach(parent);
		
		for(int i=0; i<cs.length; )
		{
			Component c = (Component)cs[i++];
			String prop = (String)cs[i++];
			
			add(c, prop);
		}
	}
	
	
	private void attach(Component x)
	{
		JComponent c = getJComponent(x);
		if(c == null)
		{
			throw new Rex("JFrame, JDialog, or JComponent");
		}
		c.putClientProperty(KEY, this);
	}
	
	
	public static JComponent getJComponent(Component x)
	{
		if(x instanceof JFrame)
		{
			return ((JFrame)x).getRootPane();
		}
		else if(x instanceof JDialog)
		{
			return ((JDialog)x).getRootPane();
		}
		else if(x instanceof JComponent)
		{
			return (JComponent)x;
		}
		else
		{
			return null;
		}
	}
	
	
	public static LocalSettings get(Component x)
	{
		JComponent c = getJComponent(x);
		if(c != null)
		{
			return (LocalSettings)c.getClientProperty(KEY);
		}
		return null;
	}
	
	
	public void add(Component c, String prop)
	{
		if(components.put(prop, c) != null)
		{
			throw new Rex("name already present: " + prop); 
		}
	}


	public void restore()
	{
		for(String prop: components.keySet())
		{
			Component c = components.get(prop);
			GlobalSettings.restore(prop, c);
		}
	}
	
	
	public void store(UISettings s)
	{
		for(String prop: components.keySet())
		{
			Component c = components.get(prop);
			GlobalSettings.store(prop, c);
		}
	}
}
