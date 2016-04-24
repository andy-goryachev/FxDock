// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.Theme;
import javax.swing.JComponent;
import javax.swing.UIDefaults;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicMenuBarUI;


public class AgMenuBarUI
	extends BasicMenuBarUI
{
	public static void init(UIDefaults d)
	{
		d.put("MenuBarUI", AgMenuBarUI.class.getName());
		d.put("MenuBar.border", new CBorder());
		d.put("MenuBar.background", Theme.PANEL_BG);
		d.put("MenuBar.foreground", Theme.TEXT_FG);
	}


	public static ComponentUI createUI(JComponent c)
	{
		return new AgMenuBarUI();
	}
}
