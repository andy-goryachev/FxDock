// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.Theme;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.border.AbstractBorder;


public class CFieldBorder
	extends AbstractBorder
{
	private boolean drawLine;


	public CFieldBorder(boolean drawLine)
	{
		this.drawLine = drawLine;
	}
	
	
	public CFieldBorder()
	{
		this(true);
	}
	
	
	public Insets getBorderInsets(Component c)
	{
		return getBorderInsets(c, new Insets(0, 0, 0, 0));
	}


	public Insets getBorderInsets(Component c, Insets insets)
	{
		insets.top    = drawLine ? 3 : 2;
		insets.left   = drawLine ? 4 : 3;
		insets.bottom = drawLine ? 3 : 2;
		insets.right  = drawLine ? 2 : 1;
		return insets;
	}


	public void paintBorder(Component c, Graphics g, int x, int y, int w, int h)
	{
		// text field does not repaint border when focus changes...
		//Color col = c.hasFocus() ? Theme.focusColor() :  Theme.lineColor();
		Color col = Theme.LINE_COLOR;
		
		// line around
		if(drawLine)
		{
			w--;
			h--;

			g.setColor(col);
			g.drawRect(x, y, w, h);

			x++;
			y++;
		}

		// dark horizontal shadow
//		g.setColor(shadow);
//		g.drawLine(x, y, w, y);
//		y++;

		// vertical
//		g.setColor(shadow);
//		g.drawLine(x, y, x, h - dh);
	}
}
