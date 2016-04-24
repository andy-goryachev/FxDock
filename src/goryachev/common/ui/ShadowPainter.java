// Copyright (c) 2011-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;


// http://en.wikipedia.org/wiki/Gaussian_blur
public class ShadowPainter
{
	private int length = 500;
	private int size;
	private int alpha;
	private BufferedImage northWest;
	private BufferedImage north;
	private BufferedImage northEast;
	private BufferedImage east;
	private BufferedImage southEast;
	private BufferedImage south;
	private BufferedImage southWest;
	private BufferedImage west;
	
	
	public ShadowPainter()
	{
		this(20, 255);
	}
	
	
	public ShadowPainter(int sz, int alpha)
	{
		this.size = sz;
		this.alpha = alpha;
		double sigma = size/4.0;
		
		northWest = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		gaussCorner(northWest, sigma, size, size);
		
		north = new BufferedImage(length, size, BufferedImage.TYPE_INT_ARGB);
		gaussHorizontal(north, sigma, size);  
		
		northEast = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		gaussCorner(northEast, sigma, 0, size);
		
		east = new BufferedImage(size, length, BufferedImage.TYPE_INT_ARGB);
		gaussVertical(east, sigma, 0);
		
		southEast = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		gaussCorner(southEast, sigma, 0, 0);
		
		south = new BufferedImage(length, size, BufferedImage.TYPE_INT_ARGB);
		gaussHorizontal(south, sigma, 0);  
		
		southWest = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		gaussCorner(southWest, sigma, size, 0);
		
		west = new BufferedImage(size, length, BufferedImage.TYPE_INT_ARGB);
		gaussVertical(west, sigma, size);
	}
	
	
//	private void fill(BufferedImage im)
//	{
//		for(int x=0; x<im.getWidth(); x++)
//		{
//			for(int y=0; y<im.getHeight(); y++)
//			{
//				int rgb = 0xffff0000;
//				im.setRGB(x, y, rgb);
//			}
//		}
//	}


	private void gaussCorner(BufferedImage im, double sigma, int px, int py)
	{
		double twoSigmaSquared = 2.0 * sigma * sigma;
		
		for(int x=0; x<im.getWidth(); x++)
		{
			for(int y=0; y<im.getHeight(); y++)
			{
				double dx = x - px;
				double dy = y - py;
				double f = Math.exp(-(dx*dx + dy*dy)/twoSigmaSquared) / Math.sqrt(twoSigmaSquared);
				
				int rgb = ((int)Math.round(f*alpha) & 0xff) << 24;
				im.setRGB(x, y, rgb);
			}
		}
	}
	
	
	private void gaussHorizontal(BufferedImage im, double sigma, int py)
	{
		double twoSigmaSquared = 2.0 * sigma * sigma;
		
		for(int y=0; y<im.getHeight(); y++)
		{
			double dy = y - py;
			double f = Math.exp(-(dy*dy)/twoSigmaSquared) / Math.sqrt(twoSigmaSquared);
			
			int rgb = ((int)Math.round(f*alpha) & 0xff) << 24;
			for(int x=0; x<im.getWidth(); x++)
			{
				im.setRGB(x, y, rgb);
			}
		}
	}

	
	private void gaussVertical(BufferedImage im, double sigma, int px)
	{
		double twoSigmaSquared = 2.0 * sigma * sigma;
		
		for(int x=0; x<im.getWidth(); x++)
		{
			double dx = x - px;
			double f = Math.exp(-(dx*dx)/twoSigmaSquared) / Math.sqrt(twoSigmaSquared);
			
			int rgb = ((int)Math.round(f*alpha) & 0xff) << 24;
			for(int y=0; y<im.getHeight(); y++)
			{
				im.setRGB(x, y, rgb);
			}
		}
	}


	public void paint(Graphics g, int x, int y, int w, int h)
	{
		// nw
		g.drawImage(northWest, x-size, y-size, x, y, 0, 0, size, size, null);
		
		// n
		int px = 0;
		while(px < w)
		{
			int dw = Math.min(w-px+1, length);
			g.drawImage(north, x+px, y-size, x+px+dw, y, 0, 0, dw, size, null);
			px += length;
		}
		
		// ne
		g.drawImage(northEast, x+w+1, y-size, x+w+1+size, y, 0, 0, size, size, null);
		
		// e
		int py = 0;
		while(py < h)
		{
			int dh = Math.min(h-py+1, length);
			g.drawImage(east, x+w+1, y+py, x+w+size+1, y+py+dh, 0, 0, size, dh, null);
			py += length;
		}
		
		// se
		g.drawImage(southEast, x+w+1, y+h+1, x+w+1+size, y+h+1+size, 0, 0, size, size, null);
		
		// s
		px = 0;
		while(px < w)
		{
			int dw = Math.min(w-px+1, length);
			g.drawImage(south, x+px, y+h+1, x+px+dw, y+h+1+size, 0, 0, dw, size, null);
			px += length;
		}
		
		// sw
		g.drawImage(southWest, x-size, y+h+1, x, y+h+1+size, 0, 0, size, size, null);
		
		// w
		py = 0;
		while(py < h)
		{
			int dh = Math.min(h-py+1, length);
			g.drawImage(west, x-size, y+py, x, y+py+dh, 0, 0, size, dh, null);
			py += length;
		}
	}
	
	
	public void paint(Graphics g, Rectangle r)
	{
		// TODO remove -1, and remove +1 in the paint call above
		paint(g, r.x, r.y, r.width-1, r.height-1);
	}
}
