// Copyright (c) 2005-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.options;
import goryachev.common.ui.GlobalSettings;
import goryachev.common.util.CKit;
import goryachev.common.util.CSettings;
import java.util.Collection;


public abstract class COption<T>
{
	public abstract T parseProperty(String s);
	
	public abstract String toProperty(T value);
	
	public abstract T defaultValue();
	
	//
	
	private String id;
	private CSettings settings;
	private T value;
	private boolean set;
	private boolean modified;
	
	
	public COption(String id)
	{
		this(id, GlobalSettings.getSettings(), GlobalSettings.getOptionList());
	}
	
	
	public COption(String id, CSettings settings, Collection<COption<?>> list)
	{
		this.id = id;
		this.settings = settings;
		if(list != null)
		{
			list.add(this);
		}
	}
	
	
	public String getID()
	{
		return id;
	}
	
	
	public T get()
	{
		if(set)
		{
			return value;
		}

		String prop = settings.getProperty(id);
		if(prop == null)
		{
			value = defaultValue();
		}
		else
		{
			value = parseProperty(prop);
		}
		set = true;
		return value;
	}
	
	
	public void set(T value)
	{
		if(!CKit.equals(this.value, value))
		{
			this.value = value;
			set = true;
			modified = true;
		}
	}
	
	
	public boolean isModified()
	{
		return modified;
	}
	
	
	public void save()
	{
		if(isModified())
		{
			String val = toProperty(value);
			settings.set(id, val);
		}
	}
}
