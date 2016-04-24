/*
 * Copyright 2013, Morten Nobel-Joergensen
 *
 * License: The BSD 3-Clause License
 * http://opensource.org/licenses/BSD-3-Clause
 */
package goryachev.common.util.img.mortennobel;
import goryachev.common.util.CList;
import goryachev.common.util.img.jhlabs.UnsharpFilter;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;


/**
 * @author Morten Nobel-Joergensen
 */
public abstract class AdvancedResizeOp
	implements BufferedImageOp
{
	public static enum UnsharpenMask
	{
		None(0), 
		Soft(0.15f), 
		Normal(0.3f),
		VerySharp(0.45f),
		Oversharpened(0.60f);
		
		public final float factor;


		UnsharpenMask(float factor)
		{
			this.factor = factor;
		}
	}
	
	//
	
	protected abstract BufferedImage doFilter(BufferedImage src, BufferedImage dest, int dstWidth, int dstHeight);

	//
	
	private CList<ProgressListener> listeners = new CList<ProgressListener>();
	private final DimensionConstraint dimensionConstraint;
	private UnsharpenMask unsharpenMask = UnsharpenMask.None;


	public AdvancedResizeOp(DimensionConstraint dimensionConstrain)
	{
		this.dimensionConstraint = dimensionConstrain;
	}


	public UnsharpenMask getUnsharpenMask()
	{
		return unsharpenMask;
	}


	public void setUnsharpenMask(AdvancedResizeOp.UnsharpenMask unsharpenMask)
	{
		this.unsharpenMask = unsharpenMask;
	}


	protected void fireProgressChanged(float fraction)
	{
		for(ProgressListener progressListener: listeners)
		{
			progressListener.notifyProgress(fraction);
		}
	}


	public void addProgressListener(ProgressListener progressListener)
	{
		listeners.add(progressListener);
	}


	public boolean removeProgressListener(ProgressListener progressListener)
	{
		return listeners.remove(progressListener);
	}


	public BufferedImage filter(BufferedImage src, BufferedImage dest)
	{
		Dimension d = dimensionConstraint.getDimension(new Dimension(src.getWidth(), src.getHeight()));
		int w = d.width;
		int h = d.height;
		BufferedImage im = doFilter(src, dest, w, h);

		if(unsharpenMask != UnsharpenMask.None)
		{
			UnsharpFilter f = new UnsharpFilter();
			f.setRadius(2f);
			f.setAmount(unsharpenMask.factor);
			f.setThreshold(10);
			return f.filter(im, null);
		}

		return im;
	}


	public Rectangle2D getBounds2D(BufferedImage src)
	{
		return new Rectangle(0, 0, src.getWidth(), src.getHeight());
	}


	public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel destCM)
	{
		if(destCM == null)
		{
			destCM = src.getColorModel();
		}
		return new BufferedImage(destCM, destCM.createCompatibleWritableRaster(src.getWidth(), src.getHeight()), destCM.isAlphaPremultiplied(), null);
	}


	public Point2D getPoint2D(Point2D srcPt, Point2D dstPt)
	{
		return (Point2D)srcPt.clone();
	}


	public RenderingHints getRenderingHints()
	{
		return null;
	}
}
