// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
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
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;


@Deprecated // use something else of Theme.waitIcon()
public class SpinningDaisyIcon
	extends AbstractAnimatedIcon
{
	private int size;
	private float outerRadiusFraction = 0.48f; // fraction of size, can not exceed 0.5
	private float innerRadiusFraction = 0.27f;
	private int minAlpha = 16;
	private Line2D.Float line = new Line2D.Float();
	private Color color;
	private static final double PI2 = Math.PI * 2;


	public SpinningDaisyIcon(int frameCount, int period, int size)
	{
		super(frameCount, period);
		this.size = size;
	}
	
	
	public SpinningDaisyIcon(int size)
	{
		this(12, 120, size);
	}
	
	
	public SpinningDaisyIcon()
	{
		this(12, 120, 36);
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
		
		Graphics2D g = (Graphics2D)image.getGraphics();
		g.setComposite(AlphaComposite.Src);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.translate(size/2, size/2);
		
		int count = getFrameCount();
		float strokeWidth = 0.8f * size / count;
		g.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		
		Color color = getColor();
		if(color == null)
		{
			color = Color.black;
		}
		int alpha = 255;
		
		for(int i=0; i<count; i++)
		{
			double angle = PI2 - PI2 * (i - idx) / count;
			float cos = (float)Math.cos(angle);
			float sin = (float)Math.sin(angle);
			line.x1 = size * outerRadiusFraction * cos;
			line.y1 = size * outerRadiusFraction * sin;
			line.x2 = size * innerRadiusFraction * cos;
			line.y2 = size * innerRadiusFraction * sin;

			g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
			g.draw(line);
			if(alpha > minAlpha)
			{
				alpha = Math.max(minAlpha, alpha * 3 / 4);
			}
		}
		g.dispose();
		return image;
	}
}
