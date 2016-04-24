// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.plaf.UIResource;


public class CSplitPaneBorder
	implements Border, UIResource
{
	public CSplitPaneBorder()
	{
	}


	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
	{
		// The only tricky part with this border is that the divider is
		// not positioned at the top (for horizontal) or left (for vert),
		// so this border draws to where the divider is:
		// -----------------
		// |xxxxxxx xxxxxxx|
		// |x     ---     x|
		// |x     |	|     x|
		// |x     |D|     x|
		// |x     | |     x|
		// |x     ---     x|
		// |xxxxxxx xxxxxxx|
		// -----------------
		// The above shows (rather excessively) what this looks like for
		// a horizontal orientation. This border then draws the x's, with
		// the SplitPaneDividerBorder drawing its own border.
		Component child;
		Rectangle cBounds;
		JSplitPane splitPane = (JSplitPane)c;
		Color shadow = getShadowColor();
		Color highlight = getHighlightColor();

		child = splitPane.getLeftComponent();
		// This is needed for the space between the divider and end of
		// splitpane.
		g.setColor(c.getBackground());
		g.drawRect(x, y, width - 1, height - 1);
		if(splitPane.getOrientation() == JSplitPane.HORIZONTAL_SPLIT)
		{
			if(child != null)
			{
				cBounds = child.getBounds();
				g.setColor(shadow);
				g.drawLine(0, 0, cBounds.width + 1, 0);
				g.drawLine(0, 1, 0, cBounds.height + 1);

				g.setColor(highlight);
				g.drawLine(0, cBounds.height + 1, cBounds.width + 1, cBounds.height + 1);
			}
			child = splitPane.getRightComponent();
			if(child != null)
			{
				cBounds = child.getBounds();

				int maxX = cBounds.x + cBounds.width;
				int maxY = cBounds.y + cBounds.height;

				g.setColor(shadow);
				g.drawLine(cBounds.x - 1, 0, maxX, 0);
				g.setColor(highlight);
				g.drawLine(cBounds.x - 1, maxY, maxX, maxY);
				g.drawLine(maxX, 0, maxX, maxY + 1);
			}
		}
		else
		{
			if(child != null)
			{
				cBounds = child.getBounds();
				g.setColor(shadow);
				g.drawLine(0, 0, cBounds.width + 1, 0);
				g.drawLine(0, 1, 0, cBounds.height);
				g.setColor(highlight);
				g.drawLine(1 + cBounds.width, 0, 1 + cBounds.width, cBounds.height + 1);
				g.drawLine(0, cBounds.height + 1, 0, cBounds.height + 1);
			}
			child = splitPane.getRightComponent();
			if(child != null)
			{
				cBounds = child.getBounds();

				int maxX = cBounds.x + cBounds.width;
				int maxY = cBounds.y + cBounds.height;

				g.setColor(shadow);
				g.drawLine(0, cBounds.y - 1, 0, maxY);
				g.drawLine(maxX, cBounds.y - 1, maxX, cBounds.y - 1);
				g.setColor(highlight);
				g.drawLine(0, maxY, cBounds.width + 1, maxY);
				g.drawLine(maxX, cBounds.y, maxX, maxY);
			}
		}
	}


	public Insets getBorderInsets(Component c)
	{
		return new Insets(1, 1, 1, 1);
	}


	public boolean isBorderOpaque()
	{
		return true;
	}


	protected Color getShadowColor()
	{
		return ThemeTools.DARKER;
	}


	protected Color getHighlightColor()
	{
		return ThemeTools.BRIGHTER;
	}
}