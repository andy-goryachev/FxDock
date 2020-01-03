// Copyright © 2016-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.internal;
import goryachev.common.util.CMap;
import goryachev.common.util.GlobalSettings;
import goryachev.common.util.SStream;
import goryachev.fx.Converters;
import goryachev.fx.HasSettings;
import goryachev.fx.SSConverter;
import javafx.beans.property.Property;
import javafx.util.StringConverter;


/**
 * Local Bindings.
 */
public class LocalBindings
{
	private CMap<String,Entry> entries = new CMap<>();
	
	//
	
	abstract class Entry
	{
		public abstract void saveValue(String prefix);
		
		public abstract void loadValue(String prefix);
	}
	
	//
	
	public <T> void add(String subKey, Property<T> p, StringConverter<T> c)
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
	}
	

	public <T> void add(String subKey, SSConverter<T> c, Property<T> p)
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
	}
	
	
	public void add(String subKey, Property<String> p)
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
	}
	
	
	public <T> void add(String subKey, HasSettings x)
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
