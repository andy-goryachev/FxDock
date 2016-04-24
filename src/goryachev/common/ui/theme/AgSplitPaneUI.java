// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.UI;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;


// TODO needs to honor component orientation in horizontal split mode!
// http://bugs.sun.com/view_bug.do?bug_id=4265389
public class AgSplitPaneUI
	extends BasicSplitPaneUI
{
	private static int dividerSize = 5;
	protected boolean paintedHack;
	protected boolean keepHiddenHack;
	protected boolean dividerLocationIsSetHack;
	protected boolean ignoreDividerLocationChangeHack;
	protected CSplitPaneLayoutManager layoutManagerHack;
	

	public AgSplitPaneUI()
	{
	}
	
	
	public static void init(UIDefaults d)
	{
		d.put("SplitPaneUI", AgSplitPaneUI.class.getName());
		d.put("SplitPane.continuousLayout", Boolean.TRUE);
		
//		SplitPane.ancestorInputMap = InputMapUIResource
//		SplitPane.background = ColorUIResource(EEEEEE)
//		SplitPane.border = javax.swing.plaf.basic.BasicBorders$SplitPaneBorder
//		SplitPane.centerOneTouchButtons = false
//		SplitPane.darkShadow = ColorUIResource(7A8A99)
//		SplitPane.dividerFocusColor = ColorUIResource(C8DDF2)
//		SplitPane.dividerSize = 10
//		SplitPane.highlight = ColorUIResource(FFFFFF)
//		SplitPane.oneTouchButtonsOpaque = false
//		SplitPane.shadow = ColorUIResource(B8CFE5)
//		SplitPaneDivider.border = javax.swing.plaf.basic.BasicBorders$SplitPaneDividerBorder
//		SplitPaneDivider.draggingColor = ColorUIResource(404040)
//		SplitPaneUI = "javax.swing.plaf.metal.MetalSplitPaneUI"
	}


	public void installUI(JComponent c)
	{
		super.installUI(c);
		keepHiddenHack = false;
		dividerLocationIsSetHack = false;
	}


	public void uninstallUI(JComponent c)
	{
		if(splitPane.getLayout() == layoutManagerHack)
		{
			splitPane.setLayout(null);
		}

		super.uninstallUI(c);
		
		dividerLocationIsSetHack = false;
		layoutManagerHack = null;
	}
	

	public void resetToPreferredSizes(JSplitPane c)
	{
		if(splitPane != null)
		{
			layoutManagerHack.resetToPreferredSizes();
			splitPane.revalidate();
			splitPane.repaint();
		}
	}


	public Dimension getPreferredSize(JComponent jc)
	{
		if(splitPane != null)
		{
			return layoutManagerHack.preferredLayoutSize(splitPane);
		}
		return new Dimension(0, 0);
	}


	public Dimension getMinimumSize(JComponent jc)
	{
		if(splitPane != null)
		{
			return layoutManagerHack.minimumLayoutSize(splitPane);
		}
		return new Dimension(0, 0);
	}


	public Dimension getMaximumSize(JComponent jc)
	{
		if(splitPane != null)
		{
			return layoutManagerHack.maximumLayoutSize(splitPane);
		}
		return new Dimension(0, 0);
	}


	protected void resetLayoutManager()
	{
		if(getOrientation() == JSplitPane.HORIZONTAL_SPLIT)
		{
			layoutManagerHack = new CSplitPaneLayoutManager(this, true);
		}
		else
		{
			layoutManagerHack = new CSplitPaneLayoutManager(this, false);
		}
		
		splitPane.setLayout(layoutManagerHack);
		layoutManagerHack.updateComponents();
		
		splitPane.revalidate();
		splitPane.repaint();
	}


	protected void setKeepHiddenHack(boolean on)
	{
		keepHiddenHack = on;
	}


	public boolean getKeepHiddenHack()
	{
		return keepHiddenHack;
	}


	public void setDividerLocation(JSplitPane split, int location)
	{
		if(!ignoreDividerLocationChangeHack)
		{
			dividerLocationIsSetHack = true;
			splitPane.revalidate();
			splitPane.repaint();

			if(keepHiddenHack)
			{
				Insets insets = splitPane.getInsets();
				int orientation = splitPane.getOrientation();
				if((orientation == JSplitPane.VERTICAL_SPLIT && location != insets.top && location != splitPane.getHeight() - divider.getHeight() - insets.top) || (orientation == JSplitPane.HORIZONTAL_SPLIT && location != insets.left && location != splitPane.getWidth() - divider.getWidth() - insets.left))
				{
					setKeepHiddenHack(false);
				}
			}
		}
		else
		{
			ignoreDividerLocationChangeHack = false;
		}
	}


	public static ComponentUI createUI(JComponent x)
	{
		return new AgSplitPaneUI();
	}


	protected void installDefaults()
	{
		super.installDefaults();
		
		if(UI.isNullOrResource(splitPane.getBorder()))
		{
			splitPane.setBorder(new CSplitPaneBorder());
		}
		
		LookAndFeel.installProperty(splitPane, "dividerSize", dividerSize);
		divider.setDividerSize(splitPane.getDividerSize());
	}


	public BasicSplitPaneDivider createDefaultDivider()
	{
		return new CSplitPaneDivider(this);
	}
	
	
	public JSplitPane getJSplitPane()
	{
		return splitPane;
	}


	public void paint(Graphics g, JComponent jc)
	{
		if(!paintedHack && splitPane.getDividerLocation() < 0)
		{
			ignoreDividerLocationChangeHack = true;
			splitPane.setDividerLocation(getDividerLocation(splitPane));
		}
		paintedHack = true;
	}
}