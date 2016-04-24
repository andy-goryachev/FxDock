// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.Theme;
import goryachev.common.ui.ThemeKey;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JButton;
import javax.swing.plaf.UIResource;


public class AgArrowButton
	extends JButton
{
	protected int direction;
	
	private static Color shadow = ThemeColor.shadow(ThemeKey.PANEL_BG, 0.2);
	private static Color darkShadow = ThemeColor.shadow(ThemeKey.PANEL_BG, 0.4);
	private static Color highlight = ThemeColor.highlight(ThemeKey.PANEL_BG, 0.2);



	public AgArrowButton(int direction)
	{
		super();
		setRequestFocusEnabled(false);
		setDirection(direction);
		setBackground(Theme.PANEL_BG);
	}


	// SwingConstants.NORTH
	// SwingConstants.SOUTH 
	// SwingConstants.EAST
	// SwingConstants.WEST
	public int getDirection()
	{
		return direction;
	}


	// SwingConstants.NORTH
	// SwingConstants.SOUTH 
	// SwingConstants.EAST
	// SwingConstants.WEST
	public void setDirection(int dir)
	{
		direction = dir;
	}


	public void paint(Graphics g)
	{
		Color origColor;
		boolean isPressed;
		boolean isEnabled;
		int size;

		int w = getWidth();
		int h = getHeight();
		origColor = g.getColor();
		isPressed = getModel().isPressed();
		isEnabled = isEnabled();
		
		g.setColor(getBackground());
		g.fillRect(1, 1, w - 2, h - 2);

		// FIX simplify
		if(getBorder() != null && !(getBorder() instanceof UIResource))
		{
			paintBorder(g);
		}
		else if(isPressed)
		{
			g.setColor(shadow);
			g.drawRect(0, 0, w - 1, h - 1);
		}
		else
		{
			// Using the background color set above
			g.drawLine(0, 0, 0, h - 1);
			g.drawLine(1, 0, w - 2, 0);

			g.setColor(highlight); // inner 3D border
			g.drawLine(1, 1, 1, h - 3);
			g.drawLine(2, 1, w - 3, 1);

			g.setColor(shadow); // inner 3D border
			g.drawLine(1, h - 2, w - 2, h - 2);
			g.drawLine(w - 2, 1, w - 2, h - 3);

			g.setColor(darkShadow); // black drop shadow  __|
			g.drawLine(0, h - 1, w - 1, h - 1);
			g.drawLine(w - 1, h - 1, w - 1, 0);
		}

		// If there's no room to draw arrow, bail
		if(h < 5 || w < 5)
		{
			g.setColor(origColor);
			return;
		}

		if(isPressed)
		{
			g.translate(1, 1);
		}

		// arrow
		size = Math.min((h - 4) / 3, (w - 4) / 3);
		size = Math.max(size, 2);
		paintTriangle(g, (w - size) / 2, (h - size) / 2, size, direction, isEnabled);

		if(isPressed)
		{
			g.translate(-1, -1);
		}
		g.setColor(origColor);
	}


	public Dimension getPreferredSize()
	{
		return new Dimension(16, 16);
	}


	public Dimension getMinimumSize()
	{
		return new Dimension(5, 5);
	}


	public Dimension getMaximumSize()
	{
		return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}


	public boolean isFocusTraversable()
	{
		return false;
	}


	public void paintTriangle(Graphics g, int x, int y, int size, int direction, boolean isEnabled)
	{
		int i;
		Color oldColor = g.getColor();
		int j = 0;
		size = Math.max(size, 2);
		int mid = (size / 2) - 1;
		
		g.translate(x, y);
		g.setColor(isEnabled ? darkShadow : shadow);

		switch(direction)
		{
		case NORTH:
			for(i = 0; i < size; i++)
			{
				g.drawLine(mid - i, i, mid + i, i);
			}
			if(!isEnabled)
			{
				g.setColor(highlight);
				g.drawLine(mid - i + 2, i, mid + i, i);
			}
			break;
		case SOUTH:
			if(!isEnabled)
			{
				g.translate(1, 1);
				g.setColor(highlight);
				for(i = size - 1; i >= 0; i--)
				{
					g.drawLine(mid - i, j, mid + i, j);
					j++;
				}
				g.translate(-1, -1);
				g.setColor(shadow);
			}

			j = 0;
			for(i = size - 1; i >= 0; i--)
			{
				g.drawLine(mid - i, j, mid + i, j);
				j++;
			}
			break;
		case WEST:
			for(i = 0; i < size; i++)
			{
				g.drawLine(i, mid - i, i, mid + i);
			}
			if(!isEnabled)
			{
				g.setColor(highlight);
				g.drawLine(i, mid - i + 2, i, mid + i);
			}
			break;
		case EAST:
			if(!isEnabled)
			{
				g.translate(1, 1);
				g.setColor(highlight);
				for(i = size - 1; i >= 0; i--)
				{
					g.drawLine(j, mid - i, j, mid + i);
					j++;
				}
				g.translate(-1, -1);
				g.setColor(shadow);
			}

			j = 0;
			for(i = size - 1; i >= 0; i--)
			{
				g.drawLine(j, mid - i, j, mid + i);
				j++;
			}
			break;
		}
		g.translate(-x, -y);
		g.setColor(oldColor);
	}
}
