// Copyright (c) 2010-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.table;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.Theme;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.plaf.basic.BasicGraphicsUtils;


public class CTableRendererBorder
	extends CBorder
{
	public static final int VGAP = 0;
	public static final int HGAP = 3;
	private boolean focused;
	private boolean focusEnabled = true;


	public CTableRendererBorder()
	{
		super(VGAP + 1, HGAP, VGAP, HGAP);
	}
	
	
	public static int getVerticalSpace()
	{
		return VGAP + VGAP + 1;
	}
	

	public void setFocused(boolean on)
	{
		focused = on;
	}


	public void setFocusEnabled(boolean on)
	{
		focusEnabled = on;
	}


	public void paintBorder(Component c, Graphics g, int x, int y, int w, int h)
	{
		super.paintBorder(c, g, x, y, w, h);

		if(focused && focusEnabled)
		{
			g.setColor(Theme.FOCUS_COLOR);
			BasicGraphicsUtils.drawDashedRect(g, x, y, w, h);
		}
	}
}
