// Copyright (c) 2008-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.ui.options.COption;
import goryachev.common.util.CKit;
import goryachev.common.util.TXT;
import java.awt.Toolkit;
import java.util.ArrayList;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;


public class Accelerator
	extends COption<KeyStroke>
{
	private String name;
	private String subsystem; 
	private KeyStroke defaultValue;
	private static ArrayList<Accelerator> accelerators = new ArrayList();
	
	
	public Accelerator(String id, String subsystem, String name, KeyStroke ks)
	{
		super(id);
		this.subsystem = subsystem;
		this.name = name;
		this.defaultValue = ks;
		
		accelerators.add(this);
	}
	
	
	// InputEvent.SHIFT_MASK 
	// InputEvent.CTRL_MASK 
	// InputEvent.META_MASK 
	// InputEvent.ALT_MASK
	// InputEvent.ALT_GRAPH_MASK
	public Accelerator(String id, String subsystem, String name, int key, int modifiers)
	{
		this(id, subsystem, name, KeyStroke.getKeyStroke(key, modifiers));
	}
	
	
	public Accelerator(String id, String subsystem, String name)
	{
		this(id, subsystem, name, null);
	}
	
	
	// platform-specific menu short key
	public Accelerator(String id, String subsystem, String name, int key, boolean withMenuShortkey)
	{
		this(id, subsystem, name, KeyStroke.getKeyStroke(key, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
	}
	

	public boolean equals(Object x)
	{
		if(x == this)
		{
			return true;
		}
		
		if(x instanceof Accelerator)
		{
			Accelerator a = (Accelerator)x;
			return CKit.equals(getID(), a.getID()) && CKit.equals(getKeyStroke(), a.getKeyStroke());
		}
		return false;
	}
	
	
	public int hashCode()
	{
		return CKit.hashCode(getID(), getKeyStroke());
	}
	
	
	public String getName()
	{
		return name;
	}
	
	
	public String getFullName()
	{
		if(CKit.isBlank(subsystem))
		{
			return name;
		}
		else
		{
			return TXT.get("Accelerator.name.COMPONENT: NAME", "{0}: {1}", subsystem, name);
		}
	}
	
	
	public String getSubsystem()
	{
		return subsystem;
	}
	
	
	public String getKeyName()
	{
		return KeyNames.getKeyName(getKeyStroke());
	}
	
	
	public KeyStroke getKeyStroke()
	{
		return get();
	}
	
	
	public String toProperty(KeyStroke k)
	{
		return k == null ? "" : k.toString();
	}
	

	public void setKeyStroke(KeyStroke k)
	{
		set(k);
	}
	
	
	public void set(CAction a)
	{
		a.setAccelerator(getKeyStroke());
	}
	
	
	public void set(JMenuItem m)
	{
		m.setAccelerator(getKeyStroke());
	}


	public KeyStroke defaultValue()
	{
		return defaultValue;
	}


	public KeyStroke parseProperty(String s)
	{
		return KeyStroke.getKeyStroke(s);
	}
	
	
	public static Accelerator[] getAccelerators()
	{
		return accelerators.toArray(new Accelerator[accelerators.size()]);
	}
}
