// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicGraphicsUtils;


public class CDashedBorder
	extends LineBorder
{
	public CDashedBorder(Color color)
	{
		super(color);
	}


	public CDashedBorder(Color color, int thickness)
	{
		super(color, thickness);
	}


	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
	{
		Color oldColor = g.getColor();
		int i;

		g.setColor(lineColor);
		for(i = 0; i < thickness; i++)
		{
			// FIX stroke
			BasicGraphicsUtils.drawDashedRect(g, x + i, y + i, width - i - i, height - i - i);
		}
		g.setColor(oldColor);
	}
}