// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.CSkin;
import goryachev.common.ui.Theme;
import goryachev.common.ui.ThemeKey;
import goryachev.common.ui.UI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.UIDefaults;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;


public class AgButtonUI
	extends BasicButtonUI
{
	public static final Color BUTTON_SHADOW = ThemeColor.create(ThemeKey.TEXT_FG, 0.75, ThemeKey.PANEL_BG);
	public static final Color DISABLED_SHADOW = ThemeColor.create(ThemeKey.TEXT_FG, 0.2, ThemeKey.PANEL_BG);
	public static final Color DISABLED_FOREGROUND = ThemeColor.create(ThemeKey.TEXT_FG, 0.5, ThemeKey.PANEL_BG);
	public static final Color SELECTED_BG = ThemeColor.highlight(ThemeKey.PANEL_BG, 0.85);
	protected int dashedRectGapX;
	protected int dashedRectGapY;
	protected int dashedRectGapWidth;
	protected int dashedRectGapHeight;
	private boolean defaults_initialized;
	private final static AgButtonUI ui = new AgButtonUI();
	private static Insets margin = UI.newInsets(2, 10, 2, 10);
	private static CSkin SKIN = new CButtonSkin();
	private static Border BORDER = new CButtonBorder();
	

	public static void init(UIDefaults d)
	{
		d.put("ButtonUI", AgButtonUI.class.getName());
		d.put("Button.background", Theme.PANEL_BG);
		d.put("Button.foreground", Theme.TEXT_FG);
		d.put("Button.showMnemonics", Boolean.TRUE);
		d.put("Button.shadow", BUTTON_SHADOW);
		d.put("Button.disabledShadow", DISABLED_SHADOW);
	}
	
	
	// UIManager.getUI(JComponent) uses reflection to invoke this method.  not nice.
	public static ComponentUI createUI(JComponent c)
	{
		return ui;
	}


	protected void installDefaults(AbstractButton b)
	{
		super.installDefaults(b);
		
		if(!defaults_initialized)
		{
			dashedRectGapX = 5;
			dashedRectGapY = 4;
			dashedRectGapWidth = 10;
			dashedRectGapHeight = 8;
			defaults_initialized = true;
		}
		
		CSkin.set(b, SKIN);
		
		if(UI.isNullOrResource(b.getBorder()))
		{
			b.setBorder(createBorder());
		}
		
		if(UI.isNullOrResource(b.getMargin()))
		{
			b.setMargin(createMargin());
		}
	}
	
	
	protected Insets createMargin()
	{
		return margin;
	}
	
	
	protected Border createBorder()
	{
		// TODO background shows up of corners are not painted
		// FIX paint() insets
		return BORDER;
	}


	protected void uninstallDefaults(AbstractButton b)
	{
		super.uninstallDefaults(b);
		defaults_initialized = false;
	}


	protected Color getFocusColor()
	{
		return Theme.FOCUS_COLOR;
	}


	protected void paintFocus(Graphics g, AbstractButton b, Rectangle viewRect, Rectangle textRect, Rectangle iconRect)
	{
		int width = b.getWidth();
		int height = b.getHeight();
		g.setColor(getFocusColor());
		BasicGraphicsUtils.drawDashedRect
		(
			g, 
			dashedRectGapX + getTextShiftOffset(), 
			dashedRectGapY + getTextShiftOffset(), 
			width - dashedRectGapWidth, 
			height - dashedRectGapHeight
		);
	}


	public Dimension getPreferredSize(JComponent c)
	{
		Dimension d = super.getPreferredSize(c);

		// Ensure that the width and height of the button is odd,
		// to allow for the focus line if focus is painted
		AbstractButton b = (AbstractButton) c;
		if(d != null && b.isFocusPainted())
		{
			if(d.width % 2 == 0)
			{
				d.width += 1;
			}
			if(d.height % 2 == 0)
			{
				d.height += 1;
			}
		}
		return d;
	}

	
	public boolean isButtonSelected(JComponent c)
	{
		if(c instanceof AbstractButton)
		{
			return ((AbstractButton)c).isSelected();
		}
		return false;
	}


	public void paint(Graphics g, JComponent c)
	{
		setBorderPressed(c, false);
		
		if(isButtonSelected(c))
		{
			g.setColor(SELECTED_BG);
			g.fillRect(0, 0, c.getWidth(), c.getHeight());
		}
		else
		{
			// TODO paint gradient shifted by one pixel if pressed!
			CSkin skin = CSkin.get(c);
			if(skin != null)
			{
				skin.paint(g, c);
			}
		}
		
		super.paint(g, c);
	}
	

	protected void paintButtonPressed(Graphics g, AbstractButton b)
	{
		setBorderPressed(b, true);
		setTextShiftOffset();
	}
	
	
	protected void setBorderPressed(JComponent c, boolean on)
	{
		Border br = c.getBorder();
		if(br instanceof CButtonBorder)
		{
			((CButtonBorder)br).setPressed(on);
		}
	}
}
