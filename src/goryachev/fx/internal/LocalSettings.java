// Copyright © 2016-2021 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.internal;
import goryachev.common.util.CMap;
import goryachev.common.util.GlobalSettings;
import goryachev.common.util.SStream;
import goryachev.fx.Converters;
import goryachev.fx.FxAction;
import goryachev.fx.FxDouble;
import goryachev.fx.FxInt;
import goryachev.fx.HasSettings;
import goryachev.fx.SSConverter;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextInputControl;
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
public class LocalSettings
{
	protected abstract class Entry
	{
		public abstract void saveValue(String prefix);
		
		public abstract void loadValue(String prefix);
	}
	
	//

	private final CMap<String,Entry> entries = new CMap<>();
	private static final Object PROP_BINDINGS = new Object();
	
	
	public LocalSettings()
	{
	}
	
	
	/** returns a Node-specific instance, creating it within the Node's properties when necessary */
	public static LocalSettings get(Node n)
	{
		LocalSettings s = find(n);
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
		LocalSettings s = find(w);
		if(s == null)
		{
			s = new LocalSettings();
			w.getProperties().put(PROP_BINDINGS, s);
		}
		return s;
	}
	
	
	/** returns a Node-specific instance, or null if not found.  This method should not be called from the client code normally. */
	public static LocalSettings find(Node n)
	{
		return (LocalSettings)n.getProperties().get(PROP_BINDINGS);
	}
	
	
	/** returns a Window-specific instance, or null if not found.  This method should not be called from the client code normally. */
	public static LocalSettings find(Window w)
	{
		return (LocalSettings)w.getProperties().get(PROP_BINDINGS);
	}
	

	public <T> LocalSettings add(String subKey, Property<T> p, StringConverter<T> c)
	{
		StringConverter<T> conv = (c == null) ? Converters.get(p) : c;
		entries.put(subKey, new Entry()
		{
			public void saveValue(String prefix)
			{
				T v = p.getValue();
				String s = (v == null ? null : conv.toString(v));
				GlobalSettings.setString(prefix + "." + subKey, s);
			}

			public void loadValue(String prefix)
			{
				String s = GlobalSettings.getString(prefix + "." + subKey);
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
			public void saveValue(String prefix)
			{
				T v = p.getValue();
				SStream s = (v == null ? null : c.toStream(v));
				GlobalSettings.setStream(prefix + "." + subKey, s);
			}

			public void loadValue(String prefix)
			{
				SStream s = GlobalSettings.getStream(prefix + "." + subKey);
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
			public void saveValue(String prefix)
			{
				String v = p.getValue();
				GlobalSettings.setString(prefix + "." + subKey, v);
			}

			public void loadValue(String prefix)
			{
				String v = GlobalSettings.getString(prefix + "." + subKey);
				p.setValue(v);
			}
		});
		return this;
	}
	
	
	public LocalSettings add(String subKey, FxDouble p)
	{
		entries.put(subKey, new Entry()
		{
			public void saveValue(String prefix)
			{
				double v = p.getValue();
				GlobalSettings.setString(prefix + "." + subKey, String.valueOf(v));
			}

			public void loadValue(String prefix)
			{
				String s = GlobalSettings.getString(prefix + "." + subKey);
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
			public void saveValue(String prefix)
			{
				int v = p.getValue();
				GlobalSettings.setString(prefix + "." + subKey, String.valueOf(v));
			}

			public void loadValue(String prefix)
			{
				String s = GlobalSettings.getString(prefix + "." + subKey);
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
			public void saveValue(String prefix)
			{
				boolean v = p.getValue();
				GlobalSettings.setString(prefix + "." + subKey, v ? "true" : "false");
			}

			public void loadValue(String prefix)
			{
				String v = GlobalSettings.getString(prefix + "." + subKey);
				boolean on = Boolean.parseBoolean(v);
				p.setValue(on);
			}
		});
		return this;
	}
	
	
	public LocalSettings add(String subKey, ComboBox cb)
	{
		entries.put(subKey, new Entry()
		{
			public void saveValue(String prefix)
			{
				Object v = cb.getValue();
				String s = encode(v);
				GlobalSettings.setString(prefix + "." + subKey, s);
			}

			public void loadValue(String prefix)
			{
				String v = GlobalSettings.getString(prefix + "." + subKey);
				if(v != null)
				{
					ObservableList<?> items = cb.getItems();
					if(items != null)
					{
						for(Object x: items)
						{
							String s = encode(x);
							if(v.equals(s))
							{
								cb.setValue(x);
								return;
							}
						}
					}
					
					if(cb.isEditable())
					{
						cb.setValue(v);
					}
				}
			}
		});
		return this;
	}
	
	
	public LocalSettings add(String subKey, CheckBox cb)
	{
		entries.put(subKey, new Entry()
		{
			public void saveValue(String prefix)
			{
				boolean v = cb.isSelected();
				GlobalSettings.setString(prefix + "." + subKey, v ? "true" : "false");
			}

			public void loadValue(String prefix)
			{
				String s = GlobalSettings.getString(prefix + "." + subKey);
				if(s != null)
				{
					cb.setSelected(Boolean.parseBoolean(s));
				}
			}
		});
		return this;
	}
	
	
	public LocalSettings add(String subKey, TextInputControl t)
	{
		return add(subKey, t.textProperty());
	}
	
	
	public LocalSettings add(String subKey, FxAction a)
	{
		return add(subKey, a.selectedProperty());
	}
	
	
	public <T> LocalSettings add(String subKey, HasSettings x)
	{
		entries.put(subKey, new Entry()
		{
			public void saveValue(String prefix)
			{
				x.storeSettings(prefix + "." + subKey);
			}

			public void loadValue(String prefix)
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


	public void loadValues(String prefix)
	{
		for(String k: entries.keySet())
		{
			Entry en = entries.get(k);
			en.loadValue(prefix);
		}
	}


	public void saveValues(String prefix)
	{
		for(String k: entries.keySet())
		{
			Entry en = entries.get(k);
			en.saveValue(prefix);
		}
	}
}
