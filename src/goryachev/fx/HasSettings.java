// Copyright © 2016-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;


/**
 * This interface indicates the component has [multiple] FX settings.
 */
public interface HasSettings
{
	/** stores in GlobalSettings under the specified prefix */
	public void storeSettings(String prefix);
	
	
	/** restores from GlobalSettings under the specified prefix */
	public void restoreSettings(String prefix);
}
