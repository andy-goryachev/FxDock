// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.util.CMap;
import goryachev.common.util.Log;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;


public class Img
	extends ImageIcon
{
	public static final Icon EMPTY = new CIcon(16);
	public static final Object KEY_URL = new Object();
	
	
	//
	
	
	private CMap<Object,Object> properties;
	

	public Img(String filename)
	{
		super(filename);
	}


	public Img(URL url)
	{
		super(url);
		setProperty(KEY_URL, url);
	}
	
	
	public Img(Image image)
	{
		super(image);
	}
	
	
	public URL getURL()
	{
		return (URL)getProperty(KEY_URL);
	}
	
	
	public String toString()
	{
		try
		{
			return getURL().toString();
		}
		catch(Exception e)
		{
			return "Img";
		}
	}
	
	
	public BufferedImage getBufferedImage()
	{
		Image im = getImage();
		if(im instanceof BufferedImage)
		{
			return (BufferedImage)im;
		}
		else
		{
			BufferedImage img = new BufferedImage(getIconWidth(), getIconHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = img.createGraphics();
			g.drawImage(im, 0, 0, null);
			g.dispose();
			return img;
		}
	}
	
	
	public Object setProperty(Object key, Object value)
	{
		if(properties == null)
		{
			properties = new CMap();
		}
		
		return properties.set(key, value);
	}
	
	
	public Object getProperty(Object key)
	{
		if(properties != null)
		{
			return properties.get(key);
		}
		return null;
	}

		
	private static URL constructURL(String name)
	{
		try
		{
			StackTraceElement[] st = new Throwable().getStackTrace();
			Class caller = Class.forName(st[1].getClassName());
			if(caller == Img.class)
			{
				caller = Class.forName(st[2].getClassName());
			}
			URL url = caller.getResource(name);
			if(url != null)
			{
				return url;
			}
			
			// TODO VLog, log error, create error icon instead
			throw new RuntimeException("icon not found: " + caller.getName() + " " + name);
		}
		catch(Exception e)
		{ 
			throw new RuntimeException(e);
		}
		
		// return constructURL("broken-image.png");
	}
	
	
	/** load from current package */
	public static Img ld(String name)
	{
		// FIX I don't think this works
		return new Img(name);
	}
	
	
	public static Img load(Class c, String name)
	{
		try
		{
			URL url = c.getResource(name);
			return new Img(url);
		}
		catch(Exception e)
		{
			Log.print("Icon not found: " + name);
			return new Img(ImageTools.create(16, 16, Color.red));
		}
	}
	
	
	/** load from current package, determined from reflection */
	public static Img local(String name)
	{
		try
		{
			return new Img(constructURL(name));
		}
		catch(Exception e)
		{
			Log.print("Icon not found: " + name);
			return new Img(ImageTools.create(16, 16, Color.red));
		}
	}
	
	
	/** loads icon from from classpath */
	public static Img load(String path)
	{
		// TODO cache
		URL url = ClassLoader.getSystemResource(path);
		if(url == null)
		{
			throw new RuntimeException("icon not found: " + path);
		}
		return new Img(url);
	}

	
	public static Icon emptyIcon(int size)
	{
		return emptyIcon(size, size);
	}

	
	public static Icon emptyIcon(final int width, final int height)
	{
		return new Icon()
		{
			public int getIconHeight() { return height; }
			public int getIconWidth() { return width; }
			public void paintIcon(Component c, Graphics g, int x, int y) { }
		};
	}
	
	
	//
	
	
	public static Icon colorIcon(final int width, final int height, final Color color)
	{
		return new Icon()
		{
			public int getIconHeight() { return height; }
			public int getIconWidth() { return width; }


			public void paintIcon(Component c, Graphics g, int x, int y)
			{
				g.setColor(color);
				g.fillRect(0,0,width,height);
			}
		};
	}
}
