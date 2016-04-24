// Copyright (c) 2015-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import goryachev.common.ui.Theme;
import goryachev.common.ui.ThemeKey;
import goryachev.common.ui.UI;
import java.awt.Color;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;


/** Theme color that changes when theme changes */
public abstract class ThemeColor 
	extends Color
{
	protected abstract Color color();
	
	
	//
	
	
	protected ThemeColor()
	{
		super(0);
	}
	
	
	public static ThemeColor create(final ThemeKey key)
	{
		return new ThemeColor()
		{
			protected Color color()
			{
				return Theme.getColor(key);
			}
		};
	}
	
	
	public static ThemeColor create(final ThemeKey key, final double alpha)
	{
		return new ThemeColor()
		{
			protected Color color()
			{
				Color c = Theme.getColor(key);
				return UI.setAlpha(c, alpha);
			}
		};
	}
	
	
	public static ThemeColor create(final Object keyA, final double mixA, final Object keyB)
	{
		return new ThemeColor()
		{
			protected Color color()
			{
				Color a = Theme.getColor(keyA);
				Color b = Theme.getColor(keyB);
				return UI.mix(a, mixA, b);
			}
		};
	}
	
	
	public static ThemeColor shadow(final Object key, final double amount)
	{
		return new ThemeColor()
		{
			protected Color color()
			{
				Color c = Theme.getColor(key);
				Color sh = Theme.isDark() ? Color.white : Color.black;
				return UI.mix(sh, amount, c);
			}
		};
	}
	
	
	public static ThemeColor highlight(final Object key, final double amount)
	{
		return new ThemeColor()
		{
			protected Color color()
			{
				Color c = Theme.getColor(key);
				Color sh = Theme.isDark() ? Color.black : Color.white;
				return UI.mix(sh, amount, c);
			}
		};
	}
	

	public int getRed()
	{
		return color().getRed();
	}


	public int getGreen()
	{
		return color().getGreen();
	}


	public int getBlue()
	{
		return color().getBlue();
	}


	public int getAlpha()
	{
		return color().getAlpha();
	}


	public int getRGB()
	{
		return color().getRGB();
	}


	public Color brighter()
	{
		return color().brighter();
	}


	public Color darker()
	{
		return color().darker();
	}


	public int hashCode()
	{
		return ThemeColor.class.hashCode() ^ color().hashCode();
	}


	public boolean equals(Object x)
	{
		if(x == this)
		{
			return true;
		}
		return color().equals(x);
	}


	public String toString()
	{
		return color().toString();
	}


	public float[] getRGBComponents(float[] a)
	{
		return color().getRGBComponents(a);
	}


	public float[] getRGBColorComponents(float[] a)
	{
		return color().getRGBColorComponents(a);
	}


	public float[] getComponents(float[] a)
	{
		return color().getComponents(a);
	}


	public float[] getColorComponents(float[] a)
	{
		return color().getColorComponents(a);
	}


	public float[] getComponents(ColorSpace s, float[] a)
	{
		return color().getComponents(s, a);
	}


	public float[] getColorComponents(ColorSpace s, float[] a)
	{
		return color().getColorComponents(s, a);
	}


	public ColorSpace getColorSpace()
	{
		return color().getColorSpace();
	}


	public synchronized PaintContext createContext(ColorModel m, Rectangle r, Rectangle2D r2, AffineTransform t, RenderingHints h)
	{
		return color().createContext(m, r, r2, t, h);
	}


	public int getTransparency()
	{
		return color().getTransparency();
	}
}
