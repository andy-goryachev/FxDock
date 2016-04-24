// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.Theme;
import java.awt.FontMetrics;
import java.awt.Graphics;
import javax.swing.JComponent;


public class ThemeTools
{
	public static final ThemeColor BRIGHTER = ThemeColor.highlight(Theme.PANEL_BG, 0.15);
	public static final ThemeColor DARKER = ThemeColor.shadow(Theme.PANEL_BG, 0.15);
	public static final ThemeColor DARKER_DARKER = ThemeColor.shadow(Theme.PANEL_BG, 0.3);


	public static int stringWidth(JComponent c, FontMetrics fm, String string)
	{
		if(string == null || string.equals(""))
		{
			return 0;
		}

		return fm.stringWidth(string);
	}


	public static void drawString(JComponent c, Graphics g, String text, int x, int y)
	{
		if(text == null || text.length() <= 0)
		{
			return;
		}

		g.drawString(text, x, y);
	}
}
