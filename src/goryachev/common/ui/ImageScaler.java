// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.util.Log;
import goryachev.common.util.Rex;
import goryachev.common.util.img.jhlabs.GaussianFilter;
import goryachev.common.util.img.mortennobel.ResampleOp;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;


/** 
 * Image scaler utility suitable for generating scaled images and thumbnails.
 * Generates scaled image with inidividually settable:
 * <pre>
 * - outer margin
 * - wraparound shadow (angled shadow TODO)
 * - border
 * - padding
 * - image
 */
public class ImageScaler
{
	private int width;
	private int height;
	private int margin;
	private int border;
	private Color borderColor;
	private int padding;
	private Color paddingColor;
	private Color background;
	private int shadowAlpha;
	private int shadowAngle;
	private int shadowDepth;
	private CAlignment horizontalAlignment = CAlignment.CENTER;
	private CAlignment verticalAlignment = CAlignment.CENTER;
	private boolean trim;
	private boolean throwException;

	
	public ImageScaler()
	{
	}
	
	
	/** Set image background, null results in a transparent background. */
	public void setBackground(Color c)
	{
		background = c;
	}
	
	
	/** Sets overall output image size */
	public void setSize(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	
	
	public int getWidth()
	{
		return width;
	}
	
	
	public int getHeight()
	{
		return height;
	}
	
	
	/** Sets outer margin */
	public void setMargin(int x)
	{
		this.margin = x;
	}
	
	
	/** Sets one-pixel border of the specified color */
	public void setBorder(Color c)
	{
		setBorder(c, 1);
	}
	
	
	/** Sets the thickness and color of the border. */
	public void setBorder(Color c, int thickness)
	{
		border = thickness;
		borderColor = c; 
	}
	
	
	/** Sets the thickness and color of the padding between border and the bitmap */
	public void setPadding(Color c, int thickness)
	{
		paddingColor = c;
		padding = thickness;
	}

	
	/** Sets shadow depth in pixels. */
	public void setShadow(int depth)
	{
		setShadow(64, -1, depth);
	}
	
	
	/**
	 * Sets wraparound shadow parameters.  The shadow will wrap around the scaled image.
	 * 
	 * @param alpha - shadow alpha (0: fully transparent, 255:fully opaqie)
	 * @param depth - shadow depth
	 */
	public void setShadow(int alpha, int depth)
	{
		setShadow(alpha, -1, depth);
	}
	
	
	/**
	 * Sets directional shadow parameters.  
	 * 
	 * @param alpha - shadow alpha (0: fully transparent, 255:fully opaqie)
	 * @param degrees - shadow angle in degrees (0...359).  Negative value would create a wrap-around shadow
	 * @param depth - shadow depth
	 */
	public void setShadow(int alpha, int degrees, int depth)
	{
		this.shadowAlpha = alpha;
		this.shadowAngle = degrees;
		this.shadowDepth = depth;
	}
	
	
	public int getShadowAngle()
	{
		return shadowAngle;
	}
	
	
	/** Sets vertical image alignment */
	public void setVerticalAlignment(CAlignment a)
	{
		switch(a)
		{
		case TOP:
		case BOTTOM:
		case CENTER:
			verticalAlignment = a;
			break;
		default:
			throw new IllegalArgumentException("top, bottom, center: " + a);
		}
	}
	
	
	/** Sets horizontal image alignment */
	public void setHorizontalAlignment(CAlignment a)
	{
		switch(a)
		{
		case LEADING:
		case TRAILING:
		case CENTER:
			horizontalAlignment = a;
			break;
		default:
			throw new IllegalArgumentException("left, right, center: " + a);
		}
	}
	
	
	/** Sets scale mode to shrink the image as required to fit into the image area */
	public void setScaleModeShrink()
	{
		trim = false;
	}
	
	
	/** Sets scale mode to trim the image as necessary to fill all of the available image area */
	public void setScaleModeTrim()
	{
		trim = true;
	}
	
	
	/** Controls whether an exception will be thrown by the scaleImage() method, or to generate an red-filled image in case of an error. */
	public void setThrowException(boolean on)
	{
		throwException = on;
	}


	/** generates a scaled image */
	public BufferedImage scaleImage(Image sourceImage)
	{
		BufferedImage source;
		if(sourceImage == null)
		{
			source = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		}
		else
		{
			source = ImageTools.toBufferedImage(sourceImage);
		}
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		try
		{
			// background
			if(background != null)
			{
				g.setColor(background);
				g.fillRect(0, 0, width, height);
			}
			
			// available image area, assuming symmetric shadow and all other parameters
			int corner = margin + border + padding + shadowDepth;
			int tw = width - corner - corner;
			int th = height - corner - corner;
			
			if(tw < 1)
			{
				throw new Exception("target image width is too small"); 
			}
			
			if(th < 1)
			{
				throw new Exception("target image height is too small");
			}
			
			// scale the image
			double sx = tw / (double)source.getWidth();
			double sy = th / (double)source.getHeight();
			BufferedImage scaled;
			boolean fitWidth;
			if(trim)
			{
				if(sx > sy)
				{
					fitWidth = true;
				}
				else
				{
					fitWidth = false;
				}
				
				BufferedImage im = resize(source, tw, th, fitWidth);
				// crop
				if(im.getWidth() > tw)
				{
					int x = (im.getWidth() - tw) / 2;
					scaled = im.getSubimage(x, 0, tw, im.getHeight());
				}
				else if(im.getHeight() > th)
				{
					int y = (im.getHeight() - th) / 2;
					scaled = im.getSubimage(0, y, im.getWidth(), th);
				}
				else
				{
					scaled = im;
				}
			}
			else
			{
				int tw2;
				int th2;
				double scale = Math.min(sx, sy);
				
				if(sx > sy)
				{
					fitWidth = false;
					tw2 = (int)Math.round(image.getWidth() * scale); 
					th2 = th;					
				}
				else
				{
					fitWidth = true;
					tw2 = tw;
					th2 = (int)Math.round(image.getHeight() * scale); 
				}
				
				scaled = resize(source, tw2, th2, fitWidth);
			}
			
			// scaled image offsets and size
			int sw = scaled.getWidth();
			int sh = scaled.getHeight();
			int dx = tw - sw;
			int dy = th - sh;
			
			if(dx > 0)
			{
				switch(horizontalAlignment)
				{
				case LEADING:
					dx = 0;
					break;
				case TRAILING:
					break;
				default:
					dx /= 2;
					break;
				}
			}
			else if(dx < 0)
			{
				throw new Rex("dx=" + dx);
			}
			
			if(dy > 0)
			{
				switch(verticalAlignment)
				{
				case TOP:
					dy = 0;
					break;
				case BOTTOM:
					break;
				default:
					dy /= 2;
					break;
				}
			}
			else if(dy < 0)
			{
				throw new Rex("dy=" + dy);
			}
			
			// shadow			
			if(shadowDepth > 0)
			{
				// TODO shadow angle
				BufferedImage black = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
				Graphics2D g2 = black.createGraphics();
				try
				{
					g2.setColor(new Color(0, 0, 0, shadowAlpha));
					
					int x = margin + shadowDepth + dx;
					int y = margin + shadowDepth + dy;
					int d = padding + padding + border + border;
					g2.fillRect(x, y, sw + d, sh + d);
				}
				finally
				{
					g2.dispose();
				}
				
				GaussianFilter f = new GaussianFilter(shadowDepth);
				BufferedImage shadowImage = f.filter(black, null);
				g.drawImage(shadowImage, 0, 0, null);
			}
					
			// padding
			if(paddingColor != null)
			{
				int pcorner = margin + shadowDepth + border;
				int padding2 = padding + padding;
				g.setColor(paddingColor);
				g.fillRect(pcorner + dx, pcorner + dy, sw + padding2, sh + padding2);
			}

			// scaled image
			g.drawImage(scaled, corner + dx, corner + dy, null);
			
			// border
			if(borderColor != null)
			{
				g.setColor(borderColor);
				for(int i=0; i<border; i++)
				{
					int x = margin + shadowDepth + dx + i;
					int y = margin + shadowDepth + dy + i;
					int dd = padding + padding + border + border - i - i;
					g.drawRect(x, y, sw + dd - 1, sh + dd - 1);
				}
			}
		}
		catch(Exception e)
		{
			if(throwException)
			{
				throw new Rex(e);
			}
			
			// make it obvious
			Log.err(e);
			g.setColor(Color.red);
			g.fillRect(0, 0, width, height);
		}
		finally
		{
			g.dispose();
		}
		return image;
	}


	// single-core resize
	public static BufferedImage resize_OLD(Image image, boolean hasAlpha, int width, int height, boolean fitWidth)
	{
		int w = image.getWidth(null);
		int h = image.getHeight(null);
		
		if(fitWidth)
		{
			height = h * width / w;
		}
		else
		{
			width = w * height / h;
		}
		
		BufferedImage im = new BufferedImage(width, height, hasAlpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = im.createGraphics();
		try
		{
			if(fitWidth)
			{
				g.drawImage(image.getScaledInstance(width, -1, Image.SCALE_SMOOTH), 0, 0, null);
			}
			else
			{
				g.drawImage(image.getScaledInstance(-1, height, Image.SCALE_SMOOTH), 0, 0, null);
			}
		}
		finally
		{
			g.dispose();
		}
		
		return im;
	}
	
	
	// multi-core resize
	public static BufferedImage resize(Image image, int width, int height, boolean fitWidth)
	{
		int w = image.getWidth(null);
		int h = image.getHeight(null);
		
		if(fitWidth)
		{
			height = h * width / w;
		}
		else
		{
			width = w * height / h;
		}
		
		BufferedImage src = ImageTools.toBufferedImage(image);
		ResampleOp op = new ResampleOp(width, height);
		//op.setUnsharpenMask(AdvancedResizeOp.UnsharpenMask.VerySharp);
		return op.filter(src, null);
	}
}
