// Copyright (c) 2015-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.Theme;
import goryachev.common.util.CPlatform;
import javax.swing.JComponent;
import javax.swing.UIDefaults;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPopupMenuUI;


public class AgPopupMenuUI
	extends BasicPopupMenuUI
{
	public AgPopupMenuUI()
	{
	}
	
	
	public static void init(UIDefaults d)
	{
		d.put("PopupMenuUI", AgPopupMenuUI.class.getName());
		d.put("PopupMenu.background", Theme.PANEL_BG);
		d.put("PopupMenu.foreground", Theme.TEXT_FG);
		d.put("PopupMenu.opaque", Boolean.TRUE);
		
		// TODO better border for all platforms
		if(CPlatform.isLinux())
		{
			d.put("PopupMenu.border", new CBorder(Theme.LINE_COLOR));
		}
	}


	public static ComponentUI createUI(JComponent c)
	{
		return new AgPopupMenuUI();
	}
}
