// Copyright (c) 2015-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;


/** capable of storing global settings */
public interface HasSettings
{
	public void restoreSettings(String key, UISettings settings);

	
	public void storeSettings(String key, UISettings settings);
}
