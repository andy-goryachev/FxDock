// Copyright (c) 2015-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.Theme;
import javax.swing.JComponent;
import javax.swing.UIDefaults;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToolBarUI;


public class AgToolBarUI
	extends BasicToolBarUI
{
	public AgToolBarUI()
	{
	}
	
	
	public static void init(UIDefaults d)
	{
		d.put("ToolBarUI", AgToolBarUI.class.getName());
		d.put("ToolBarUI.background", Theme.TOOLBAR_BG);
		d.put("ToolBarUI.foreground", Theme.PANEL_FG);
		d.put("ToolBarUI.dockingBackground", Theme.PANEL_BG);
		d.put("ToolBarUI.dockingForeground", Theme.PANEL_FG);
		d.put("ToolBarUI.floatingBackground", Theme.PANEL_BG);
		d.put("ToolBarUI.floatingForeground", Theme.PANEL_FG);
		d.put("ToolBar.isRollover", Boolean.FALSE);
		
//		ToolBar.ancestorInputMap = InputMapUIResource
//		ToolBar.border = javax.swing.plaf.metal.MetalBorders$ToolBarBorder
//		ToolBar.borderColor = ColorUIResource(CCCCCC)
//		ToolBar.darkShadow = ColorUIResource(7A8A99)
//		ToolBar.highlight = ColorUIResource(FFFFFF)
//		ToolBar.isRollover = true
//		ToolBar.light = ColorUIResource(FFFFFF)
//		ToolBar.nonrolloverBorder = javax.swing.border.CompoundBorder
//		ToolBar.rolloverBorder = javax.swing.border.CompoundBorder
//		ToolBar.separatorSize = DimensionUIResource(w=10.0,h=10.0)
//		ToolBar.shadow = ColorUIResource(B8CFE5)
	}
	
	
	public static ComponentUI createUI(JComponent b)
	{
		return new AgToolBarUI();
	}
}
