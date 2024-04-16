// Copyright © 2016-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.settings;
import goryachev.common.util.ASettingsStore;
import goryachev.common.util.CMap;
import goryachev.common.util.SStream;
import goryachev.fx.Converters;
import goryachev.fx.FxAction;
import goryachev.fx.FxDouble;
import goryachev.fx.FxInt;
import goryachev.fx.HasSettings;
import goryachev.fx.SSConverter;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.stage.Window;
import javafx.util.StringConverter;


/**
 * Local Settings: specific to a Node or a Window.
 * 
 * Supports chaining of calls, i.e.:
 * 
 * <pre>
 * LocalSettings.get(this).
 *    add(...).
 *    add(...);
 */
// FIX must be a part of FxSettings and use their storage provider!
public class LocalSettings
{
	protected abstract class Entry
	{
		public abstract void saveValue(String prefix, ASettingsStore store);
		
		public abstract void loadValue(String prefix, ASettingsStore store);
	}
	
	//

	private final CMap<String,Entry> entries = new CMap<>();
	private static final Object PROP_BINDINGS = new Object();
	
	
	public LocalSettings()
	{
	}
	
	
	/** returns a Node-specific instance, or null if not found.  This method should not be called from the client code normally. */
	public static LocalSettings getOrNull(Node n)
	{
		return (LocalSettings)n.getProperties().get(PROP_BINDINGS);
	}
	
	
	/** returns a Window-specific instance, or null if not found.  This method should not be called from the client code normally. */
	public static LocalSettings getOrNull(Window w)
	{
		return (LocalSettings)w.getProperties().get(PROP_BINDINGS);
	}
	
	
	/** returns a Node-specific instance, creating it within the Node's properties when necessary */
	public static LocalSettings get(Node n)
	{
		LocalSettings s = getOrNull(n);
		if(s == null)
		{
			s = new LocalSettings();
			n.getProperties().put(PROP_BINDINGS, s);
		}
		return s;
	}
	
	
	/** returns a Window-specific instance, creating it within the Window's properties when necessary */
	public static LocalSettings get(Window w)
	{
		LocalSettings s = getOrNull(w);
		if(s == null)
		{
			s = new LocalSettings();
			w.getProperties().put(PROP_BINDINGS, s);
		}
		return s;
	}
	

	public <T> LocalSettings add(String subKey, Property<T> p, StringConverter<T> c)
	{
		StringConverter<T> conv = (c == null) ? Converters.get(p) : c;
		entries.put(subKey, new Entry()
		{
			@Override
			public void saveValue(String prefix, ASettingsStore store)
			{
				T v = p.getValue();
				String s = (v == null ? null : conv.toString(v));
				store.setString(prefix + "." + subKey, s);
			}

			@Override
			public void loadValue(String prefix, ASettingsStore store)
			{
				String s = store.getString(prefix + "." + subKey);
				if(s != null)
				{
					T v = conv.fromString(s);
					p.setValue(v);
				}
			}
		});
		return this;
	}
	

	public <T> LocalSettings add(String subKey, SSConverter<T> c, Property<T> p)
	{
		entries.put(subKey, new Entry()
		{
			@Override
			public void saveValue(String prefix, ASettingsStore store)
			{
				T v = p.getValue();
				SStream s = (v == null ? null : c.toStream(v));
				store.setStream(prefix + "." + subKey, s);
			}

			@Override
			public void loadValue(String prefix, ASettingsStore store)
			{
				SStream s = store.getStream(prefix + "." + subKey);
				if(s != null)
				{
					T v = c.fromStream(s);
					p.setValue(v);
				}
			}
		});
		return this;
	}
	
	
	public LocalSettings add(String subKey, Property<String> p)
	{
		entries.put(subKey, new Entry()
		{
			@Override
			public void saveValue(String prefix, ASettingsStore store)
			{
				String v = p.getValue();
				if(v != null)
				{
					store.setString(prefix + "." + subKey, v);
				}
			}

			@Override
			public void loadValue(String prefix, ASettingsStore store)
			{
				String v = store.getString(prefix + "." + subKey);
				if(v != null)
				{
					p.setValue(v);
				}
			}
		});
		return this;
	}
	
	
	public LocalSettings add(String subKey, FxDouble p)
	{
		entries.put(subKey, new Entry()
		{
			@Override
			public void saveValue(String prefix, ASettingsStore store)
			{
				double v = p.getValue();
				store.setString(prefix + "." + subKey, String.valueOf(v));
			}

			@Override
			public void loadValue(String prefix, ASettingsStore store)
			{
				String s = store.getString(prefix + "." + subKey);
				if(s != null)
				{
					try
					{
						double v = Double.parseDouble(s);
						p.setValue(v);
					}
					catch(Exception ignore)
					{ }
				}
			}
		});
		return this;
	}
	
	
	public LocalSettings add(String subKey, FxInt p)
	{
		entries.put(subKey, new Entry()
		{
			@Override
			public void saveValue(String prefix, ASettingsStore store)
			{
				int v = p.getValue();
				store.setString(prefix + "." + subKey, String.valueOf(v));
			}

			@Override
			public void loadValue(String prefix, ASettingsStore store)
			{
				String s = store.getString(prefix + "." + subKey);
				if(s != null)
				{
					try
					{
						int v = Integer.parseInt(s);
						p.setValue(v);
					}
					catch(Exception ignore)
					{ }
				}
			}
		});
		return this;
	}
	
	
	public LocalSettings add(String subKey, BooleanProperty p)
	{
		entries.put(subKey, new Entry()
		{
			@Override
			public void saveValue(String prefix, ASettingsStore store)
			{
				boolean v = p.getValue();
				store.setString(prefix + "." + subKey, v ? "true" : "false");
			}

			@Override
			public void loadValue(String prefix, ASettingsStore store)
			{
				String v = store.getString(prefix + "." + subKey);
				boolean on = Boolean.parseBoolean(v);
				p.setValue(on);
			}
		});
		return this;
	}
	
	
	public LocalSettings add(String subKey, FxAction a)
	{
		return add(subKey, a.selectedProperty());
	}
	
	
	public <T> LocalSettings add(String subKey, HasSettings x)
	{
		entries.put(subKey, new Entry()
		{
			@Override
			public void saveValue(String prefix, ASettingsStore store)
			{
				x.storeSettings(prefix + "." + subKey);
			}

			@Override
			public void loadValue(String prefix, ASettingsStore store)
			{
				x.restoreSettings(prefix + "." + subKey);
			}
		});
		return this;
	}
	
	
	protected static String encode(Object x)
	{
		if(x == null)
		{
			return null;
		}
		
		// TODO hex, base64, or random
		return x.toString();
	}


	public void loadValues(String prefix, ASettingsStore store)
	{
		for(String k: entries.keySet())
		{
			Entry en = entries.get(k);
			en.loadValue(prefix, store);
		}
	}


	public void saveValues(String prefix, ASettingsStore store)
	{
		for(String k: entries.keySet())
		{
			Entry en = entries.get(k);
			en.saveValue(prefix, store);
		}
	}
}
