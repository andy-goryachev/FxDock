// Copyright (c) 2015-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.ThemeKey;
import goryachev.common.ui.UI;
import goryachev.common.util.CPlatform;
import java.awt.Color;
import java.awt.Font;


public class DarkTheme 
	extends ATheme
{
	public DarkTheme(String name)
	{
		super(name);
	}


	protected void customize()
	{
		Color panelBG = new Color(0x1f1f1f);
		
		set(ThemeKey.AFFIRM_BUTTON_COLOR, Color.green);
		set(ThemeKey.DESTRUCTIVE_BUTTON_COLOR, Color.magenta);
		set(ThemeKey.FIELD_BG, UI.mix(Color.black, 0.85, panelBG));
		set(ThemeKey.FIELD_FG, UI.mix(Color.black, 0.15, Color.white));
		set(ThemeKey.FOCUS_COLOR, new Color(90, 90, 90));
		set(ThemeKey.GRID_COLOR, UI.mix(Color.black, 242, Color.white));
		set(ThemeKey.LINE_COLOR, UI.mix(Color.white, 16, panelBG));
		set(ThemeKey.LINK_COLOR, new Color(0, 102, 0));
		set(ThemeKey.PANEL_BG, panelBG);
		set(ThemeKey.PANEL_FG, new Color(0xcccccc));
		set(ThemeKey.TARGET_COLOR, UI.mix(Color.black, 0.5, new Color(253, 178, 84)));
		set(ThemeKey.TEXT_BG, Color.black);
		set(ThemeKey.TEXT_FG, Color.white);
		set(ThemeKey.TEXT_SELECTION_BG, UI.mix(Color.green, 0.5, Color.black));
		set(ThemeKey.TEXT_SELECTION_FG, Color.white);
		set(ThemeKey.TOOL_TIP_BG, new Color(48, 48, 48));
		set(ThemeKey.TOOLBAR_COLOR, UI.mix(Color.white, 0.1, panelBG));
		
		if(CPlatform.isWindows())
		{
			Font f = new Font("Tahoma", Font.PLAIN, 11);
			
			set(ThemeKey.BASE_FONT, f);
			set(ThemeKey.BOLD_FONT, f.deriveFont(Font.BOLD));
			set(ThemeKey.TITLE_FONT, UI.deriveFont(f, true, TITLE_FONT_FACTOR));
		}
	}
}
