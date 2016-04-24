// Copyright (c) 2015-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.Theme;
import javax.swing.JComponent;
import javax.swing.ToolTipManager;
import javax.swing.UIDefaults;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToolTipUI;


public class AgToolTipUI
	extends BasicToolTipUI
{
	protected static final CBorder BORDER = new CBorder(1, Theme.TEXT_FG);
	protected static AgToolTipUI instance = new AgToolTipUI();
	
	
	public AgToolTipUI()
	{
	}
	
	
	// gets called by reflection from UIManager.getUI()
	public static ComponentUI createUI(JComponent h)
	{
		return instance;
	}
	
	
	public static void init(UIDefaults d)
	{
		d.put("ToolTipUI", AgToolTipUI.class.getName());
		d.put("ToolTipManager.enableToolTipMode", "activeApplication");
		
		d.put("ToolTip.background", Theme.TOOL_TIP_BG);
		d.put("ToolTip.backgroundInactive", Theme.TOOL_TIP_BG);
		d.put("ToolTip.border", BORDER);
		d.put("ToolTip.borderInactive", BORDER);
		d.put("ToolTip.foreground", Theme.TEXT_FG);
		d.put("ToolTip.foregroundInactive", Theme.FIELD_FG);
		d.put("ToolTip.hideAccelerator", Boolean.FALSE);
		
		// defs.put("ToolTip.font" // set by ATheme
		
		// configure tooltips
		ToolTipManager m = ToolTipManager.sharedInstance();
		m.setInitialDelay(50);
		m.setDismissDelay(Integer.MAX_VALUE);
		
		// TODO more intelligent placement of tool tips - need to overwrite each component
	}
}
