// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.ColorTools;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;


public class GradientPainter
{
	/** @deprecated */
	public static void paint(Graphics gg, boolean vertical, int x, int y, int w, int h, Color bg, int topPercent, int bottomPercent, float factor, boolean inverted, boolean sides)
	{
		gg.translate(x,y);
		paint(gg, vertical, w, h, bg, topPercent, bottomPercent, factor, inverted, sides);
		gg.translate(-x,-y);
	}
	
	
	/** @deprecated */
	public static void paint(Graphics gg, boolean vertical, int w, int h, Color bg, int topPercent, int bottomPercent, float factor, boolean inverted, boolean sides)
	{
		int span = vertical ? h : w;
		int th = (span * topPercent) / 100;
		int bh = (span * bottomPercent) / 100;

		Color light = ColorTools.brighter(bg,factor);
		Color dark  = ColorTools.darker(bg,factor);

		Color topColor;
		Color bottomColor;
		
		if(inverted)
		{
			topColor = dark;
			bottomColor = light;
		}
		else
		{
			topColor = light;
			bottomColor = dark;
		}
		
		Graphics2D g = (Graphics2D)gg;
		if(vertical)
		{
			if(th > 0)
			{
				g.setPaint(new GradientPaint(0,0,topColor,0,th,bg));
				g.fillRect(0,0,w,th);
			}
			
			if(h-th-bh > 0)
			{
				g.setPaint(bg);
				g.fillRect(0,th,w,h-th-bh);
			}
			
			if(bh > 0)
			{
				g.setPaint(new GradientPaint(0, h-bh, bg, 0, h, bottomColor));
				g.fillRect(0,h-bh,w,bh);
			}
			
			if(sides)
			{
				g.setColor(topColor);
				g.drawLine(0,0,0,h);
				g.setColor(bottomColor);
				g.drawLine(w-1, 0, w-1, h);
			}
		}
		else
		{
			if(th > 0)
			{
				g.setPaint(new GradientPaint(0,0,topColor,th,0,bg));
				g.fillRect(0,0,th,h);
			}
			
			if(h-th-bh > 0)
			{
				g.setPaint(bg);
				g.fillRect(th,0,w-th-bh,h);
			}
			
			if(bh > 0)
			{
				g.setPaint(new GradientPaint(w-bh, 0, bg, w, 0, bottomColor));
				g.fillRect(w-bh,0,bh,h);
			}
			
			if(sides)
			{
				g.setColor(topColor);
				g.drawLine(0,0,w,0);
				g.setColor(bottomColor);
				g.drawLine(0, h-1, w, h-1);
			}
		}
	}
	
	
	// gradient along vertical axis
	public static void paintVertical(Graphics gg, int x, int y, int w, int h, Color centerColor, int topPercent, Color topColor, int bottomPercent, Color bottomColor)
	{
		int th = (h * topPercent) / 100;
		int bh = (h * bottomPercent) / 100;

		Graphics2D g = (Graphics2D)gg;
		if(th > 0)
		{
			g.setPaint(new GradientPaint(x, y, topColor, x, y+th, centerColor));
			g.fillRect(x, y, w, th);
		}
		
		if(h-th-bh > 0)
		{
			g.setPaint(centerColor);
			g.fillRect(x, th, w, h-th-bh);
		}
		
		if(bh > 0)
		{
			g.setPaint(new GradientPaint(x, y+h-bh, centerColor, x, h, bottomColor));
			g.fillRect(x, y+h-bh, w, bh);
		}
	}
	
	
	// gradient along horizontal axis
	public static void paintHorizontal(Graphics gg, int x, int y, int w, int h, Color centerColor, int leftPercent, Color leftColor, int rightPercent, Color rightColor)
	{
		int lw = (w * leftPercent) / 100;
		int rw = (w * rightPercent) / 100;

		Graphics2D g = (Graphics2D)gg;
		if(lw > 0)
		{
			g.setPaint(new GradientPaint(x, y, leftColor, x+lw, y, centerColor));
			g.fillRect(x, y, lw, h);
		}
		
		if(w-lw-rw > 0)
		{
			g.setPaint(centerColor);
			g.fillRect(lw, y, w-lw-rw, h);
		}
		
		if(rw > 0)
		{
			g.setPaint(new GradientPaint(x, y+h-rw, centerColor, x, h, rightColor));
			g.fillRect(x+w-rw, y, w, h);
		}
	}
}
