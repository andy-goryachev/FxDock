// Copyright (c) 2015-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import javax.swing.JComponent;
import javax.swing.UIDefaults;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPopupMenuSeparatorUI;


public class AgPopupSeparatorUI	
	extends BasicPopupMenuSeparatorUI
{
	public static ComponentUI createUI(JComponent c)
	{
		return new AgPopupSeparatorUI();
	}


	public static void init(UIDefaults defs)
	{
		defs.put("PopupSeparatorUI", AgPopupSeparatorUI.class.getName());
	}


	public AgPopupSeparatorUI()
	{
	}
}
