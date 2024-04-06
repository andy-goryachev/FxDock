// Copyright © 2016-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.log.Log;
import goryachev.common.util.GlobalSettings;
import goryachev.common.util.WeakList;
import javafx.util.StringConverter;


/**
 * Global Properties.
 */
public class GlobalProperties
{
	protected static final Log log = Log.get("GlobalProperties");
	private static final WeakList<GlobalProperty<?>> properties = new WeakList();
	
	
	/** adds a property to the weak list of global properties.  no deduplication is performed though.  */ 
	public static <T> void add(GlobalProperty<T> p)
	{
		properties.add(p);
		
		p.addListener((src,old,cur) -> store(p));
		
		load(p);
	}
	
	
	protected static <T> void store(GlobalProperty<T> p)
	{
		try
		{
			String k = p.getName();
			T v = p.getValue();
			
			String s;
			if(v == null)
			{
				s = null;
			}
			else
			{
				StringConverter<T> c = p.getConverter();
				s = c.toString(v);
			}
			GlobalSettings.setString(k, s);
		}
		catch(Exception e)
		{
			log.error(e);
		}
	}
	
	
	protected static <T> void load(GlobalProperty<T> p)
	{
		try
		{
			String k = p.getName();
			String s = GlobalSettings.getString(k);
			if(s != null)
			{
				T v = p.getConverter().fromString(s);
				p.setValue(v);
			}
		}
		catch(Exception e)
		{
			log.error(e);
		}
	}
}
