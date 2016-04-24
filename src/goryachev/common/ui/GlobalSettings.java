// Copyright (c) 2005-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.ui.options.COption;
import goryachev.common.ui.options.PreferenceListener;
import goryachev.common.ui.table.CTreeTable;
import goryachev.common.util.CFileSettings;
import goryachev.common.util.CList;
import goryachev.common.util.CMap;
import goryachev.common.util.CSettings;
import goryachev.common.util.CSettingsProvider;
import goryachev.common.util.Log;
import goryachev.common.util.Obj;
import goryachev.common.util.Rex;
import goryachev.common.util.SB;
import goryachev.common.util.WeakList;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Window;
import java.io.File;
import java.util.Collection;
import java.util.Vector;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JTree;


public class GlobalSettings
{
	private static final Obj IGNORE = new Obj("IGNORE");

	public static final String UI = "ui.";
	public static final String DELIMITER = "\u0000";

	private static UISettings settings = new UISettings();
	private static CMap<Object,Object> windows = new CMap();
	private static Vector<COption<?>> options = new Vector();
	private static WeakList<PreferenceListener> preferenceListeners = new WeakList();


	private GlobalSettings()
	{
	}
	
	
	public static void init(File f)
	{
		CFileSettings p = new CFileSettings(f);
		p.setSorted(true);

		settings.setProvider(p);

		try
		{
			p.load();
		}
		catch(Exception e)
		{ }
	}
	
	
	public static void setSettingsProvider(CSettingsProvider p)
	{
		settings.setProvider(p);
	}
	
	
	public static CSettings getSettings()
	{
		return settings;
	}
	
	
	public static CSettingsProvider getSettingsProfider()
	{
		return settings.getProvider();
	}


	public static Collection<COption<?>> getOptionList()
	{
		return options;
	}


	public static String getProperty(String key)
	{
		return settings.getProperty(key);
	}
	
	
	public static void setProperty(String key, String value)
	{
		settings.setProperty(key,value);
	}
	

	public static void restorePreferences(Component c)
	{
		if(isIgnored(c))
		{
			return;
		}
		
		if(c instanceof CTreeTable)
		{
			settings.restoreTreeTable(UI + getName(c), (CTreeTable)c);
		}
		else if(c instanceof JTable)
		{
			settings.restoreTable(UI + getName(c), (JTable)c, true);
		}
		else if(c instanceof JCheckBox)
		{
			// should be explicitly permitted
//			String name = getName(c);
//			if(name != null)
//			{
//				settings.restoreCheckbox(UI + name, (JCheckBox)c);
//			}
		}
		else if(c instanceof JTree)
		{
			settings.restoreTree(UI + getName(c), (JTree)c);
		}
		else if(c instanceof JSplitPane)
		{
			settings.restoreSplitPane(UI + getName(c), (JSplitPane)c);
		}
		else if(c instanceof Dialog)
		{
			String name = c.getName();
			if(name != null)
			{
				settings.restoreDialog(UI + name, (Dialog)c);
			}
		}
		else if(c instanceof Window)
		{
			Window w = (Window)c;
			settings.restoreWindow(UI + lookupWindowName(w), w);
		}
		
		// has settings
		if(c instanceof HasSettings)
		{
			((HasSettings)c).restoreSettings(UI + getName(c), settings);
		}
		
		// local settings
		LocalSettings ls = LocalSettings.get(c);
		if(ls != null)
		{
			ls.restore();
		}
		
		// component settings
		ComponentSettings s = ComponentSettings.get(c);
		if(s != null)
		{
			s.restoreSettings(UI + getName(c), settings);
		}
		
		// recurse down
		if(c instanceof Container)
		{
			Component[] cs = ((Container)c).getComponents();
			for(Component ch: cs)
			{
				restorePreferences(ch);
			}
		}
	}
	
	
	public static void restore(String name, Component c)
	{
		if(c instanceof JCheckBox)
		{
			settings.restoreCheckbox(name, (JCheckBox)c);
		}
		else if(c instanceof JTextField)
		{
			settings.restoreTextField(name, (JTextField)c);
		}
	}
	
	
	public static void store(String name, Component c)
	{
		if(c instanceof JCheckBox)
		{
			settings.storeCheckbox(name, (JCheckBox)c);
		}
		else if(c instanceof JTextField)
		{
			settings.storeTextField(name, (JTextField)c);
		}
	}
	
	
	public static void ignore(Component c)
	{
		if(c instanceof JComponent)
		{
			((JComponent)c).putClientProperty(IGNORE, IGNORE);
		}
	}
	
	
	public static boolean isIgnored(Component c)
	{
		if(c instanceof JComponent)
		{
			return IGNORE == ((JComponent)c).getClientProperty(IGNORE);
		}
		return false;
	}
	
	
	public static void storePreferences(Component c)
	{
		if(c.isVisible() == false)
		{
			return;
		}
		
		// settings attached to a component
		ComponentSettings s = ComponentSettings.get(c);
		if(s != null)
		{
			s.storeSettings(UI + getName(c), settings);
		}
		
		// local settings. was this a bad idea?
		LocalSettings ls = LocalSettings.get(c);
		if(ls != null)
		{
			ls.store(settings);
		}
		
		if(c instanceof HasSettings)
		{
			((HasSettings)c).storeSettings(UI + getName(c), settings);
		}
		
		if(isIgnored(c))
		{
			return;
		}
		
		if(c instanceof Dialog)
		{
			String name = c.getName();
			if(name != null)
			{
				settings.storeDialog(UI + name, (Dialog)c);
			}
		}
		if(c instanceof Window)
		{
			Window w = (Window)c;
			settings.storeWindow(UI + lookupWindowName(w), w);
		}
		else if(c instanceof CTreeTable)
		{
			settings.storeTreeTable(UI + getName(c), (CTreeTable)c);
		}
		if(c instanceof JTable)
		{
			settings.storeTable(UI + getName(c), (JTable)c, true);
		}
		else if(c instanceof JCheckBox)
		{
//			String name = getName(c);
//			if(name != null)
//			{
//				settings.storeCheckbox(UI + name, (JCheckBox)c);
//			}
		}
		else if(c instanceof JTree)
		{
			settings.storeTree(UI + getName(c), (JTree)c);
		}
		else if(c instanceof JSplitPane)
		{
			settings.storeSplitPane(UI + getName(c), (JSplitPane)c);
		}
		
		// recurse down the hierarchy
		if(c instanceof Container)
		{
			Component[] cs = ((Container)c).getComponents();
			for(Component ch: cs)
			{
				storePreferences(ch);
			}
		}
	}

	
	protected static void addWindow(Window window)
	{
		String name = getWindowName0(window);
		
		int i = 0;
		String id = "";
		// small limit ensures quick return at the price of forgetting settings for that many windows
		for(; i<100; i++)
		{
			id = name + i;
			if(!windows.containsKey(id))
			{
				break;
			}
		}
		
		windows.put(window, id);
		windows.put(id, window);
	}
	
	
	protected static void removeWindow(Window w)
	{
		Object id = windows.remove(w);
		if(id instanceof String)
		{
			windows.remove(id);
		}
	}
	
	
	protected static String getWindowName0(Window w)
	{
		String name = w.getName();
		return (name == null) ? w.getClass().getSimpleName() : name;
	}
	
	
	protected static String lookupWindowName(Window w)
	{
		Object x = windows.get(w);
		if(x instanceof String)
		{
			return (String)x;
		}
		else
		{
			return getWindowName0(w);
		}
	}
	
	
	public static void opening(Window w)
	{
		addWindow(w);
		restorePreferences(w);
	}
	

	public static void closing(Window w)
	{
		storePreferences(w);
		removeWindow(w);
	}

	
	protected static String getName(Component c)
	{
		SB sb = new SB();
		getNameRecursive(sb,c.getParent());
		
		String name = c.getName();
		if(name == null)
		{
			name = c.getClass().getSimpleName();
		}
		sb.append(name);
		return sb.toString();
	}
	
	
	protected static void getNameRecursive(SB sb, Component c)
	{
		if(c != null)
		{
			if(c instanceof Window)
			{
				Object name = windows.get(c);
				if(name == null)
				{
					// window was not added!
					name = c.getClass().getSimpleName();
				}
				sb.append(name);
				sb.append('.');
				return;
			}
			else
			{
				getNameRecursive(sb,c.getParent());
			}
			
			if(c.getName() == null)
			{
				sb.append(c.getClass().getSimpleName());
			}
			else
			{
				sb.append(c.getName());
			}
			sb.append('.');
		}
	}


	public static boolean getBool(String key, boolean defaultValue)
	{
		return settings.getBool(key, defaultValue);
	}


	public static Boolean getBoolean(String key)
	{
		return settings.getBoolean(key);
	}
	
	
	public static void set(String key, boolean value)
	{
		settings.set(key,value);
	}
	
	
	public static String getString(String key)
	{
		return settings.getProperty(key);
	}

	
	public static String getString(String key, String defaultValue)
	{
		return settings.getProperty(key,defaultValue);
	}
	
	
	public static void set(String key, String value)
	{
		settings.set(key,value);
	}
	

	public static CList<String> getStrings(String key)
	{
		CList<String> a = new CList();
		String sp = getString(key,null);
		if(sp != null)
		{
			for(String s: sp.split(DELIMITER))
			{
				a.add(s);
			}
		}
		return a;
	}
	

	public static void storeAll()
	{
		for(Window w: Window.getWindows())
		{
			//if(w.isVisible())
			{
				storePreferences(w);
			}
		}
	}
	
	
	public static void save()
	{
		try
		{
			storeAll();
		}
		catch(Exception e)
		{
			Log.err(e);
		}
		
		for(COption op: options)
		{
			try
			{
				op.save();
			}
			catch(Exception e)
			{
				Log.err(e);
			}
		}

		try
		{
			settings.save();
		}
		catch(Exception e)
		{
			Log.err(e);
		}
	}
	
	
	public static void refreshListeners()
	{
		for(PreferenceListener li: preferenceListeners.asList())
		{
			try
			{
				li.preferencesUpdated();
			}
			catch(Exception e)
			{
				Log.err(e);
			}
		}
	}
	
	
	public static void registerPreferenceListener(PreferenceListener li)
	{
		preferenceListeners.add(li);
	}
	
	
	/** allows automatic settings storing/restoring of component settings using a global key */
	public static void setKey(JComponent c, final String key)
	{
		if(c instanceof JToggleButton)
		{
			final JToggleButton t = (JToggleButton)c;
	
			// using global key to set property
			new ComponentSettings(key)
			{
				public void store(String prefix, CSettings s)
				{
					s.setBoolean(key, t.isSelected());
				}


				public void restore(String prefix, CSettings s)
				{
					Boolean v = s.getBoolean(key);
					if(v != null)
					{
						t.setSelected(v.booleanValue());
					}
				}
			}.attach(c);
		}
		else
		{
			throw new Rex();
		}
	}
}
