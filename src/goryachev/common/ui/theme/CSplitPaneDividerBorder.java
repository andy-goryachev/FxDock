// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.Theme;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;


public class CSplitPaneDividerBorder
	implements Border, UIResource
{
	public CSplitPaneDividerBorder()
	{
	}


	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
	{
		JSplitPane splitPane = ((BasicSplitPaneDivider)c).getBasicSplitPaneUI().getSplitPane();
		Dimension size = c.getSize();
		Component child = splitPane.getLeftComponent();

		Color bg = c.getBackground();
		Color highlight = Theme.brighter(bg);
		Color shadow = Theme.darker(bg);

		g.setColor(bg);
		g.drawRect(x, y, width - 1, height - 1);
		if(splitPane.getOrientation() == JSplitPane.HORIZONTAL_SPLIT)
		{
			if(child != null)
			{
				g.setColor(highlight);
				g.drawLine(0, 0, 0, size.height);
			}
			child = splitPane.getRightComponent();
			if(child != null)
			{
				g.setColor(shadow);
				g.drawLine(size.width - 1, 0, size.width - 1, size.height);
			}
		}
		else
		{
			if(child != null)
			{
				g.setColor(highlight);
				g.drawLine(0, 0, size.width, 0);
			}
			child = splitPane.getRightComponent();
			if(child != null)
			{
				g.setColor(shadow);
				g.drawLine(0, size.height - 1, size.width, size.height - 1);
			}
		}
	}


	public Insets getBorderInsets(Component c)
	{
		Insets insets = new Insets(0, 0, 0, 0);
		if(c instanceof BasicSplitPaneDivider)
		{
			BasicSplitPaneUI bspui = ((BasicSplitPaneDivider)c).getBasicSplitPaneUI();
			if(bspui != null)
			{
				JSplitPane splitPane = bspui.getSplitPane();
				if(splitPane != null)
				{
					if(splitPane.getOrientation() == JSplitPane.HORIZONTAL_SPLIT)
					{
						// horizontal
						insets.top = insets.bottom = 0;
						insets.left = insets.right = 1;
						return insets;
					}
					else
					{
						// vertical
						insets.top = insets.bottom = 1;
						insets.left = insets.right = 0;
						return insets;
					}
				}
			}
		}
		insets.top = insets.bottom = insets.left = insets.right = 1;
		return insets;
	}


	public boolean isBorderOpaque()
	{
		return true;
	}
}