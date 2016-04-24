// Copyright (c) 2015-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.ThemeKey;
import goryachev.common.ui.UI;
import goryachev.common.util.CPlatform;
import java.awt.Color;
import java.awt.Font;


public class ThemeAndy 
	extends ATheme
{
	public ThemeAndy()
	{
		super("Andy");
	}
	
	
	protected void customize()
	{
		Color panelBG = new Color(212, 208, 200);
		
		set(ThemeKey.AFFIRM_BUTTON_COLOR, Color.green);
		set(ThemeKey.DESTRUCTIVE_BUTTON_COLOR, Color.magenta);
		set(ThemeKey.FIELD_BG, UI.mix(Color.white, 0.85, panelBG));
		set(ThemeKey.FIELD_FG, UI.mix(Color.white, 0.15, Color.black));
		set(ThemeKey.FOCUS_COLOR, new Color(90, 90, 90));
		set(ThemeKey.GRID_COLOR, new Color(242, 242, 242));
		set(ThemeKey.LINE_COLOR, new Color(0xaca8a2));
		set(ThemeKey.LINK_COLOR, new Color(0, 102, 0));
		set(ThemeKey.PANEL_BG, panelBG);
		set(ThemeKey.PANEL_FG, panelBG.darker());
		set(ThemeKey.TARGET_COLOR, new Color(253, 178, 84));
		set(ThemeKey.TEXT_BG, Color.white);
		set(ThemeKey.TEXT_FG, Color.black);
		set(ThemeKey.TEXT_SELECTION_BG, UI.mix(Color.yellow, 0.9, Color.white));
		set(ThemeKey.TEXT_SELECTION_FG, Color.black);
		set(ThemeKey.TOOL_TIP_BG, new Color(255, 255, 225));
		set(ThemeKey.TOOLBAR_COLOR, new Color(0xb2aea7));
		
		if(CPlatform.isWindows())
		{
			Font f = new Font("Tahoma", Font.PLAIN, 11);
			
			set(ThemeKey.BASE_FONT, f);
			set(ThemeKey.BOLD_FONT, f.deriveFont(Font.BOLD));
			set(ThemeKey.TITLE_FONT, UI.deriveFont(f, true, TITLE_FONT_FACTOR));
		}
	}
}
