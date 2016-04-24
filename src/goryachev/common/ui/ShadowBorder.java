// Copyright (c) 2011-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.border.AbstractBorder;


public class ShadowBorder
	extends AbstractBorder
{
	private int size;
	private ShadowPainter painter;


	public ShadowBorder(int size, int alpha)
	{
		this.size = size;
		this.painter = new ShadowPainter(size, alpha);
	}


	public Insets getBorderInsets(Component c)
	{
		return new Insets(size, size, size, size);
	}


	public Insets getBorderInsets(Component c, Insets m)
	{
		m.top = size;
		m.left = size;
		m.bottom = size;
		m.right = size;
		return m;
	}


	public void paintBorder(Component c, Graphics g, int x, int y, int w, int h)
	{
		int s2 = size + size;
		painter.paint(g, x+size, y+size, w-s2-1, h-s2-1);
	}
	
	
	public int getHorizontalGaps()
	{
		return size + size;
	}
}
