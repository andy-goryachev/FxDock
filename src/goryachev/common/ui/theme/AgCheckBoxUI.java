// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.Theme;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.UIDefaults;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicCheckBoxUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;


public class AgCheckBoxUI
	extends BasicCheckBoxUI
{
	private final static AgCheckBoxUI checkBoxUI = new AgCheckBoxUI();
	

	public static void init(UIDefaults d)
	{
		d.put("CheckBoxUI", AgCheckBoxUI.class.getName());
		d.put("CheckBoxUI.contentAreaFilled", Boolean.FALSE);
		d.put("CheckBox.icon", new AgCheckBoxIcon());
		d.put("CheckBox.foreground", Theme.TEXT_FG);
		d.put("CheckBox.background", Theme.PANEL_BG);
	}


	protected void installDefaults(AbstractButton b)
	{
		super.installDefaults(b);
		b.setOpaque(false);
	}


	public static ComponentUI createUI(JComponent b)
	{
		return checkBoxUI;
	}
	
	
	protected Color getFocusColor()
	{
		return Theme.FOCUS_COLOR;
	}


	protected void paintFocus(Graphics g, Rectangle textRect, Dimension d)
	{
		g.setColor(getFocusColor());
		BasicGraphicsUtils.drawDashedRect(g, textRect.x, textRect.y, textRect.width, textRect.height);
	}
}
