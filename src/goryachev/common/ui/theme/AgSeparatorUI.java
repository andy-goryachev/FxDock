// Copyright (c) 2015-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import javax.swing.JComponent;
import javax.swing.UIDefaults;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSeparatorUI;


public class AgSeparatorUI 
	extends BasicSeparatorUI
{
	public static ComponentUI createUI(JComponent c)
	{
		return new AgSeparatorUI();
	}


	public static void init(UIDefaults defs)
	{
		defs.put("SeparatorUI", AgSeparatorUI.class.getName());
		defs.put("Separator.background", ThemeTools.BRIGHTER);
		defs.put("Separator.foreground", ThemeTools.DARKER);
		defs.put("Separator.highlight", ThemeTools.BRIGHTER);
		defs.put("Separator.shadow", ThemeTools.DARKER);
	}


	public AgSeparatorUI()
	{
	}
}
