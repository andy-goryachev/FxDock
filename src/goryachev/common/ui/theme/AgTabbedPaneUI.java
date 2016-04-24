// Copyright (c) 2008-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.Theme;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.UIDefaults;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;


public class AgTabbedPaneUI
	extends BasicTabbedPaneUI
{
	private static Set managingFocusForwardTraversalKeys;
	private static Set managingFocusBackwardTraversalKeys;


	public static void init(UIDefaults d)
	{
		d.put("TabbedPaneUI", AgTabbedPaneUI.class.getName());
		d.put("TabbedPane.contentOpaque", Boolean.TRUE);
		d.put("TabbedPane.opaque", Boolean.FALSE);
		d.put("TabbedPane.selectedForeground", Theme.TEXT_FG);
		d.put("TabbedPane.foreground", Theme.TEXT_FG);
		
//		TabbedPane.ancestorInputMap = InputMapUIResource
//		TabbedPane.background = ColorUIResource(B8CFE5)
//		TabbedPane.borderHightlightColor = ColorUIResource(6382BF)
//		TabbedPane.contentAreaColor = ColorUIResource(C8DDF2)
//		TabbedPane.contentBorderInsets = Insets(t=4,l=2,b=3,r=3)
//		TabbedPane.contentOpaque = true
//		TabbedPane.darkShadow = ColorUIResource(7A8A99)
//		TabbedPane.focus = ColorUIResource(6382BF)
//		TabbedPane.focusInputMap = InputMapUIResource
//		TabbedPane.font = FontUIResource(Dialog,12,bold)
//		TabbedPane.foreground = PrintColorUIResource(333333)
//		TabbedPane.highlight = ColorUIResource(FFFFFF)
//		TabbedPane.labelShift = 1
//		TabbedPane.light = ColorUIResource(EEEEEE)
//		TabbedPane.selected = ColorUIResource(C8DDF2)
//		TabbedPane.selectedLabelShift = -1
//		TabbedPane.selectedTabPadInsets = InsetsUIResource(t=2,l=2,b=2,r=1)
//		TabbedPane.selectHighlight = ColorUIResource(FFFFFF)
//		TabbedPane.selectionFollowsFocus = true
//		TabbedPane.shadow = ColorUIResource(B8CFE5)
//		TabbedPane.tabAreaBackground = ColorUIResource(DADADA)
//		TabbedPane.tabAreaInsets = Insets(t=2,l=2,b=0,r=6)
//		TabbedPane.tabInsets = InsetsUIResource(t=0,l=9,b=1,r=9)
//		TabbedPane.tabRunOverlay = 2
//		TabbedPane.tabsOpaque = true
//		TabbedPane.tabsOverlapBorder = false
//		TabbedPane.textIconGap = 4
//		TabbedPane.unselectedBackground = ColorUIResource(EEEEEE)
	}
	

	public static ComponentUI createUI(JComponent c)
	{
		return new AgTabbedPaneUI();
	}


	protected void installDefaults()
	{
		super.installDefaults();
		
		tabInsets = new Insets(0, 4, 1, 4);
		selectedTabPadInsets = new Insets(2, 2, 2, 1);
		tabAreaInsets = new Insets(3, 2, 0, 2);
		contentBorderInsets = new Insets(2, 2, 3, 3);
		
		if(managingFocusForwardTraversalKeys == null)
		{
			managingFocusForwardTraversalKeys = new HashSet();
			managingFocusForwardTraversalKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0));
		}
		tabPane.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, managingFocusForwardTraversalKeys);

		if(managingFocusBackwardTraversalKeys == null)
		{
			managingFocusBackwardTraversalKeys = new HashSet();
			managingFocusBackwardTraversalKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, InputEvent.SHIFT_MASK));
		}
		tabPane.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, managingFocusBackwardTraversalKeys);

		shadow = ThemeTools.DARKER;
		darkShadow = ThemeTools.DARKER_DARKER;
	}


	protected void uninstallDefaults()
	{
		tabPane.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, null);
		tabPane.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, null);
		super.uninstallDefaults();
	}


	protected void setRolloverTab(int index)
	{
	}

	
	protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected)
	{
		switch(tabPlacement)
		{
		case LEFT:
			x += 1;
			y += 1;
			w -= 1;
			h -= 3;
			break;
		case RIGHT:
			y += 1;
			w -= 2;
			h -= 3;
			break;
		case BOTTOM:
			x += 1;
			w -= 3;
			h -= 1;
			break;
		case TOP:
		default:
			x += 1;
			y += 1;
			w -= 3;
			h -= 1;
			break;
		}

		Color bg = tabPane.getBackgroundAt(tabIndex);
		g.setColor(bg);
		g.fillRect(x, y, w, h);
		
		if(isSelected)
		{
			Color centerColor = new Color(255,255,255,0);
			Color topColor = new Color(255,255,255,150);
			GradientPainter.paintVertical(g, x, y, w, h, centerColor, 75, topColor, 0, null);
		}
	}
	
	
	public void setTabInsets(Insets m)
	{		
		tabInsets = m;
	}
	
	
	public void setSelectedTabPadInsets(Insets m)
	{
		selectedTabPadInsets = m;
	}
	
	
	public void setTabAreaInsets(Insets m)
	{
		tabAreaInsets = m;
	}
	
	
	public void setContentBorderInsets(Insets m)
	{
		contentBorderInsets = m;
	}
}
