// Copyright (c) 2015-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.Theme;
import goryachev.common.ui.ThemeKey;
import goryachev.common.ui.UI;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;


public class AgCheckBoxIcon 
	implements Icon
{
	public static final int SIZE = 13;
	public static final Color outerBorderColor = ThemeColor.create(ThemeKey.TEXT_FG, 0.5, ThemeKey.PANEL_BG);
	public static final Color innerBorderColor = ThemeColor.create(ThemeKey.TEXT_BG, 0.8, ThemeKey.PANEL_BG);
	public static final Color normalControlColor = ThemeColor.create(ThemeKey.TEXT_FG, 0.9, ThemeKey.PANEL_BG);
	public static final Color disabledControlColor = ThemeColor.create(ThemeKey.TEXT_FG, 0.4, ThemeKey.PANEL_BG);
	public static final BasicStroke stroke = new BasicStroke(1.9f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER);
	public static final Path2D.Float check = createCheck();
	
	
	public AgCheckBoxIcon()
	{
	}
	
	
	private static Path2D.Float createCheck()
	{
		Path2D.Float p = new Path2D.Float();
		p.moveTo(-3, 1);
		p.lineTo(0, 3);
		p.lineTo(3, -3);
		return p;
	}
	
	
	public void paintIcon(Component comp, Graphics gg, int x, int y)
	{
		Graphics2D g = UI.createAntiAliasingAndQualityGraphics(gg);
		try
		{
			AbstractButton c = (AbstractButton)comp;
			ButtonModel m = c.getModel();
			
			// background
			if((m.isPressed() && m.isArmed()) || !m.isEnabled())
			{
				g.setColor(Theme.PANEL_BG);
			}
			else
			{
				g.setColor(Theme.TEXT_BG);
			}
			g.fillRect(x, y, SIZE, SIZE);
			
			// border
			g.setColor(outerBorderColor);
			g.drawRect(x, y, SIZE - 1, SIZE - 1);
			
			g.setColor(innerBorderColor);
			g.drawRect(x + 1, y + 1, SIZE - 3, SIZE - 3);
	
			// check mark
			if(m.isSelected())
			{
				if(m.isEnabled())
				{
					g.setColor(normalControlColor);
				}
				else
				{
					g.setColor(disabledControlColor);
				}
				
				g.setStroke(stroke);
				g.translate(x + SIZE/2, y + SIZE/2);
				g.draw(check);
			}
		}
		finally
		{
			g.dispose();
		}
	}


	public int getIconWidth()
	{
		return SIZE;
	}


	public int getIconHeight()
	{
		return SIZE;
	}
}