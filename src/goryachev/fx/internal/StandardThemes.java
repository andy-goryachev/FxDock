// Copyright © 2017-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.internal;
import goryachev.common.util.CMap;
import goryachev.fx.FX;
import goryachev.fx.Theme;
import javafx.scene.paint.Color;


/**
 * Standard Themes.
 */
public class StandardThemes
{
	/** standard light theme */
	public static Theme createLightTheme()
	{
		Color base = FX.rgb(0xececec);
		
		Theme t = new Theme();
		t.affirm = FX.mix(base, Color.LIGHTGREEN, 0.8);
		t.base = base;
		t.control = FX.rgb(0x666666);
		t.destruct = FX.mix(base, Color.MAGENTA, 0.7);
		t.focus = FX.rgb(0x48dd48); //FX.rgb(0xff6d00),
		t.outline = FX.rgb(0xdddddd);
		t.selectedTextBG = FX.rgb(255, 255, 148, 0.7); //Color.rgb(193, 245, 176), //FX.rgb(0xffff00),
		t.selectedTextFG = Color.BLACK;
		t.textBG = Color.WHITE;
		t.textFG = Color.BLACK;
		return t;
	}
}
