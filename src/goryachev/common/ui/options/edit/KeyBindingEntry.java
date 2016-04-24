// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.options.edit;
import goryachev.common.ui.Accelerator;
import goryachev.common.ui.KeyNames;
import goryachev.common.util.CKit;
import javax.swing.KeyStroke;


public class KeyBindingEntry
{
	public final Accelerator accelerator;
	private KeyStroke key;
	private KeyStroke original;


	public KeyBindingEntry(Accelerator a, KeyStroke k)
	{
		this.accelerator = a;
		this.key = k;
		this.original = k;
	}


	public KeyStroke getKey()
	{
		return key;
	}
	
	
	public void setKey(KeyStroke k)
	{
		this.key = k;
	}
	
	
	public boolean isModified()
	{
		return CKit.notEquals(key, original);
	}
	
	
	public String getKeyName()
	{
		return KeyNames.getKeyName(key);
	}
	
	
	public String getCommand()
	{
		return accelerator.getName();
	}
	
	
	public void commit()
	{
		if(isModified())
		{
			accelerator.setKeyStroke(key);
		}
	}
}