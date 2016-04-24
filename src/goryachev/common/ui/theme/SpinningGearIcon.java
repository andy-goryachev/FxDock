// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.AbstractAnimatedIcon;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;


public class SpinningGearIcon
	extends AbstractAnimatedIcon
{
	private int size;
	private Color color;
	private static final double PI2 = Math.PI * 2;
	private static final int CORNERS = 6;


	public SpinningGearIcon(int frameCount, int period, int size)
	{
		super(frameCount, period);
		this.size = size;
	}


	public SpinningGearIcon(int size)
	{
		this(120, 30, size);
	}


	public SpinningGearIcon()
	{
		this(36);
	}


	public int getIconHeight()
	{
		return size;
	}


	public int getIconWidth()
	{
		return size;
	}


	public Color getColor()
	{
		return color;
	}


	public void setColor(Color c)
	{
		color = c;
	}


	public Image generateFrame(Component c, int idx)
	{
		Image image;
		if(c == null)
		{
			image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		}
		else
		{
			image = c.getGraphicsConfiguration().createCompatibleImage(size, size, Transparency.TRANSLUCENT);
		}
		
		float strokeWidth = size * 0.03f;
		double radius = size * 0.4;
		
		Path2D.Double path = new Path2D.Double();
		boolean first = true;
		int sz = CORNERS + 1;
		for(int i=0; i<sz; i++)
		{
			double angle = PI2/CORNERS * i + (PI2 * idx / getFrameCount());
			double x = radius * Math.cos(angle);
			double y = radius * Math.sin(angle);
			if(first)
			{
				path.moveTo(x, y);
				first = false;
			}
			else
			{
				path.lineTo(x, y);
			}
		}
		
		Graphics2D g = (Graphics2D)image.getGraphics();
		g.setComposite(AlphaComposite.Src);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.translate(size / 2, size / 2);
		g.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

		Color color = getColor();
		if(color == null)
		{
			color = Color.gray;
		}
		
		g.setColor(color);
		g.fill(path);
		g.dispose();
		
		return image;
	}
}
