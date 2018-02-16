// Copyright © 2017-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.internal;
import goryachev.common.util.CMap;
import goryachev.fx.FX;
import goryachev.fx.Theme.Key;
import javafx.scene.paint.Color;


/**
 * Standard Themes.
 */
public class StandardThemes
{
	/** standard light theme */
	public static CMap<Key,Object> createDefaultLightTheme()
	{
		return createFromArray
		(
			Key.BASE, FX.rgb(0xececec),
			Key.CONTROL, FX.rgb(0x666666),
			Key.FOCUS, FX.rgb(0xff6d00),
			Key.OUTLINE, FX.rgb(0xdddddd),
			Key.SELECTED_TEXT_BG, FX.rgb(0xffff00),
			Key.SELECTED_TEXT_FG, Color.BLACK,
			Key.TEXT_BG, Color.WHITE,
			Key.TEXT_FG, Color.BLACK
		);
	}
	
	
	private static CMap<Key,Object> createFromArray(Object ... items)
	{
		CMap<Key,Object> d = new CMap();
		for(int i=0; i<items.length; )
		{
			Key k = (Key)items[i++];
			Object v = items[i++];
			if(!k.type.isAssignableFrom(v.getClass()))
			{
				throw new Error(k + " requires type " + k.type);
			}
			d.put(k, v);
		}
		return d;
	}
}
