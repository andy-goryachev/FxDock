// Copyright (c) 2007-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.util.Base64;
import goryachev.common.util.FileTools;
import goryachev.common.util.Log;
import goryachev.common.util.Reflector;
import goryachev.common.util.SB;
import goryachev.common.util.TextTools;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ByteLookupTable;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.LookupOp;
import java.awt.image.RenderedImage;
import java.awt.image.VolatileImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


public class ImageTools
{
	public static BufferedImage colorImage(BufferedImage in, Color c)
	{
		BufferedImage image = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		// first convert to grayscale
		new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY),null).filter(in,image);
		
		// grayscale to colored image lookup
		byte[] r = buildLookup(c.getRed());
		byte[] g = buildLookup(c.getGreen());
		byte[] b = buildLookup(c.getBlue());
		byte[] a = buildAlpha();
		return new LookupOp(new ByteLookupTable(0,new byte[][] { r,g,b,a }),null).filter(image,image);
	}
	
	
	public static BufferedImage colorImage(int w, int h, Color c)
	{
		BufferedImage im = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = im.createGraphics();
		try
		{
			g.setColor(c);
			g.fillRect(0, 0, w, h);
		}
		finally
		{
			g.dispose();
		}
		return im;
	}
	
	
	public static ImageIcon colorIcon(ImageIcon ic, Color c)
	{
		BufferedImage image = new BufferedImage(ic.getIconWidth(), ic.getIconHeight(), BufferedImage.TYPE_INT_ARGB);

		// get buffered image from button's icon
		Graphics2D gr = image.createGraphics();
		gr.drawImage(ic.getImage(),0,0,null);
		gr.dispose();

		return new ImageIcon(colorImage(image,c));
	}
	
	
	private static byte[] buildLookup(int hi)
	{
		byte[] ba = new byte[256];
		for(int i=0; i<256; i++)
		{
			ba[i] = (byte)(Math.ceil(hi*i/255f));
		}
		return ba;
	}
	
	
	private static byte[] buildAlpha()
	{
		byte[] ba = new byte[256];
		for(int i=0; i<256; i++)
		{
			ba[i] = (byte)i;
		}
		return ba;
	}
	
	
	public static BufferedImage create(int width, int height, Color c)
	{
		BufferedImage im = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		if(c != null)
		{
			Graphics2D g = im.createGraphics();
			g.setColor(c);
			g.fillRect(0, 0, width, height);
			g.dispose();
		}
		return im;
	}


	public static byte[] toPNG(BufferedImage im)
	{
		if(im != null)
		{
			try
			{
				ByteArrayOutputStream out = new ByteArrayOutputStream(32768);
				ImageIO.write(im, "PNG", out);
				return out.toByteArray();
			}
			catch(Exception e)
			{
				Log.err(e);
			}
		}
		return null;
	}


	public static byte[] toJPG(BufferedImage im)
	{
		if(im != null)
		{
			// TODO
			// http://stackoverflow.com/questions/17108234/setting-jpg-compression-level-with-imageio-in-java
			
			/*
			ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next();
			ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();
			jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			jpgWriteParam.setCompressionQuality(0.7f);
	
			OutputStream outputStream = createOutputStream(); //For example, FileImageOutputStream
			jpgWriter.setOutput(outputStream);
			IIOImage outputImage = new IIOImage(image, null, null);
			jpgWriter.write(null, outputImage, jpgWriteParam);
			jpgWriter.dispose();
	
			The call to ImageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT) is needed in order to explicitly set 
			the compression's level (quality).
	
			In ImageWriteParam.setCompressionQuality() 1.0f is maximum quality, minimum compression, while 0.0f is minimum quality, 
			maximum compression.
				
			For those of you who don't want to write in disk: 
			ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
			writer.setOutput(new MemoryCacheImageOutputStream(baos)); 
			baos.flush();
			byte[] returnImage = baos.toByteArray(); 
			baos.close();
			*/
			
			try
			{
				ByteArrayOutputStream out = new ByteArrayOutputStream(32768);
				ImageIO.write(im, "JPG", out);
				return out.toByteArray();
			}
			catch(Exception e)
			{
				Log.err(e);
			}
		}
		return null;
	}
	
	
	public static BufferedImage scaleImage(BufferedImage im, int w, int h)
	{
		if(im == null)
		{
			return null;
		}
		
		if((im.getWidth() > w) || (im.getHeight() > h))
		{
			float rx = w / (float)im.getWidth(); 
			float ry = h / (float)im.getHeight();
			if(rx < ry)
			{
				// reduce height
				h = Math.round(im.getHeight() * rx);
			}
			else
			{
				// reduce width
				w = Math.round(im.getWidth() * ry);
			}
			
			return ImageScaler.resize(im, w, h, true);
		}
		else
		{
			return im;
		}
	}
	
	
	public static boolean hasAlpha(Image im)
	{
		if(im == null)
		{
			return false;
		}
		else if(im instanceof BufferedImage)
		{
			return ((BufferedImage)im).getColorModel().hasAlpha();
		}
		else if(im instanceof VolatileImage)
		{
			int t = ((VolatileImage)im).getTransparency();
			switch(t)
			{
			case Transparency.BITMASK:
			case Transparency.TRANSLUCENT:
				return true;
			default:
				return false;
			}
		}
		
		if("sun.awt.image.ToolkitImage".equals(im.getClass().getName()))
		{
			ColorModel cm = Reflector.invoke(ColorModel.class, "getColorModel", im);
			if(cm != null)
			{
				return cm.hasAlpha();
			}
		}
		
		Log.err(new Exception("don't know how to get alpha from " + im.getClass()));
		return false;
	}
	
	
	public static BufferedImage toBufferedImage(Image image)
	{
		if(image instanceof BufferedImage)
		{
			return (BufferedImage)image;
		}
		else
		{
			int h = image.getHeight(null);
			int w = image.getWidth(null);
			boolean alpha = hasAlpha(image);
			
			BufferedImage im = new BufferedImage(w, h, alpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
			Graphics2D g = im.createGraphics();
			try
			{
				g.drawImage(image, 0, 0, null);
			}
			finally
			{
				g.dispose();
			}
			return im;
		}
	}


	public static BufferedImage read(File f) throws Exception
	{
		return ImageIO.read(f);
	}


	public static BufferedImage read(byte[] b) throws Exception
	{
		if(b == null)
		{
			return null;
		}
		return ImageIO.read(new ByteArrayInputStream(b));
	}
	
	
	public static BufferedImage readQuiet(byte[] b)
	{
		try
		{
			return read(b);
		}
		catch(Exception ignore)
		{ }
		return null;
	}
	
	
	public static void write(RenderedImage im, String filename) throws Exception
	{
		write(im, new File(filename));
	}
	
	
	public static void write(RenderedImage im, File f) throws Exception
	{
		String format = guessImageFormat(f.getName());
		FileTools.ensureParentFolder(f);
		ImageIO.write(im, format, f);
	}
	
	
	public static String guessImageFormat(String name)
	{
		if(TextTools.endsWithIgnoreCase(name, ".jpg") || TextTools.endsWithIgnoreCase(name, ".jpeg"))
		{
			return "JPG";
		}
		else if(TextTools.endsWithIgnoreCase(name, ".gif"))
		{
			return "GIF";
		}
		else
		{
			// returns lossless format by default
			return "PNG";
		}
	}


	/** creates a blank (or transparent) image with the same dimensions and type as the supplied one */
	public static BufferedImage createBlank(BufferedImage im)
	{
		return new BufferedImage(im.getWidth(), im.getHeight(), im.getType());
	}
	
	
	/** loads a BufferedImage from a resource relative to the supplied class */
	public static BufferedImage local(Class c, String name)
	{
		try
		{
			return ImageIO.read(c.getResourceAsStream(name));
		}
		catch(Exception e)
		{
			Log.print("Image not found: " + name);
			return colorImage(16, 16, Color.red);
		}
	}


	/** makes a copy while converting to RGB or ARGB */
	public static BufferedImage copyImageRGB(Image image)
	{
		if(image == null)
		{
			return null;
		}
		
		int h = image.getHeight(null);
		int w = image.getWidth(null);
		boolean alpha = ImageTools.hasAlpha(image);

		BufferedImage im = new BufferedImage(w, h, alpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
		Graphics2D g = im.createGraphics();
		try
		{
			g.drawImage(image, 0, 0, null);
		}
		finally
		{
			g.dispose();
		}
		return im;
	}
	
	
	public static void appendBase64PNG(BufferedImage im, SB sb) throws Exception
	{
		byte[] b = ImageTools.toPNG(im);
		sb.a("data:image/png;base64,");
		sb.a(Base64.encode(b));
	}
}
