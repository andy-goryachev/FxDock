// Copyright (c) 2008-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;


public class CIcon
	implements Icon
{
	private int width;
	private int height;
	private Color color;
	private int border;
	private Color borderColor;
	
	
	public CIcon(int w, int h, Color c, int border, Color borderColor)
	{
		this.width = w;
		this.height = h;
		this.color = c;
		this.border = border;
		this.borderColor = borderColor;
	}
	
	
	public CIcon(int sz, Color c, int border, Color borderColor)
	{
		this(sz, sz, c, border, borderColor);
	}

	
	public CIcon(int w, int h, Color c)
	{
		this.width = w;
		this.height = h;
		this.color = c;
	}
	
	
	public CIcon(int w, int h)
	{
		this(w, h, null);
	}
	

	public CIcon(int size, Color c)
	{
		this(size, size, c);
	}
	
	
	public CIcon(int size)
	{
		this(size, size, null);
	}
	
	
	public CIcon()
	{
		this(16);
	}
	
	
	public int getIconHeight()
	{
		return height;
	}


	public int getIconWidth()
	{
		return width;
	}


	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		if(color != null)
		{
			g.setColor(color);
			g.fillRect(x, y, getIconWidth(), getIconHeight());
		}
		
		if(border > 0)
		{
			if(borderColor != null)
			{
				g.setColor(borderColor);
				g.drawRect(x, y, getIconWidth()-1, getIconHeight()-1);
			}
		}
	}
}
