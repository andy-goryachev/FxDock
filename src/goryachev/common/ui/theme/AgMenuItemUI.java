// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.Theme;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.UIDefaults;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicMenuItemUI;


public class AgMenuItemUI
	extends BasicMenuItemUI
{
	public static void init(UIDefaults d)
	{
		d.put("MenuItemUI", AgMenuItemUI.class.getName());
		//defs.put("MenuItem.acceleratorFont", Theme.plainFont());
		d.put("MenuItem.acceleratorForeground", Theme.PANEL_FG);
		d.put("MenuItem.acceleratorSelectionForeground", Theme.PANEL_FG);
		d.put("MenuItem.background", Theme.PANEL_BG);
		d.put("MenuItem.foreground", Theme.TEXT_FG);
		d.put("MenuItem.selectionBackground", Theme.TEXT_SELECTION_BG);
		d.put("MenuItem.selectionForeground", Theme.TEXT_SELECTION_FG);
		d.put("MenuItem.disabledForeground", Theme.FIELD_FG);
        
		d.put("MenuItem.border", new CBorder(2));
		d.put("MenuItem.borderPainted", Boolean.TRUE);
		d.put("MenuItem.margin", new Insets(2, 2, 2, 2));
		
		/*
		MenuItem.border = javax.swing.plaf.metal.MetalBorders$MenuItemBorder(t=2,l=2,b=2,r=2)
		MenuItem.disabledForeground = ColorUIResource(999999)
		MenuItem.foreground = PrintColorUIResource(333333)
		*/
	}


	public static ComponentUI createUI(JComponent c)
	{
		return new AgMenuItemUI();
	}
}
