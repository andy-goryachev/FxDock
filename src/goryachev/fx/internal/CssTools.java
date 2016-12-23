// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.internal;
import goryachev.common.util.SB;
import goryachev.fx.CssID;
import goryachev.fx.CssPseudo;
import goryachev.fx.CssStyle;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;


/**
 * Css Tools.
 */
public class CssTools
{
	/** bold type face */
	public static final CssStyle BOLD = new CssStyle("CommonStyles_BOLD");
	/** disables horizontal scroll bar */
	public static final CssStyle NO_HORIZONTAL_SCROLL_BAR = new CssStyle("CommonStyles_NO_HORIZONTAL_SCROLL_BAR");

	
	public static String toColor(Object x)
	{
		if(x == null)
		{
			return "null";
		}
		else if(x instanceof Integer)
		{
			int rgb = (Integer)x;
			int r = (rgb >> 16) & 0xff;
			int g = (rgb >>  8) & 0xff;
			int b = (rgb      ) & 0xff;
			
			return String.format("#%02x%02x%02x", r, g, b);
		}
		else if(x instanceof Color)
		{
			Color c = (Color)x;
			double r = c.getRed();
			double g = c.getGreen();
			double b = c.getBlue();
			
			if(c.isOpaque())
			{
				return String.format("#%02x%02x%02x", to8bit(r), to8bit(g), to8bit(b));
			}
			else
			{
				double a = c.getOpacity();
				return String.format("#%02x%02x%02x%02x", to8bit(r), to8bit(g), to8bit(b), to8bit(a));
//				return "rgba(" + r + "," + g + "," + b + "," + a + ")";
			}
		}
		else
		{
			return x.toString();
		}
	}
	
	
	public static String toColors(Object ... xs)
	{
		int sz = xs.length;
		if(sz == 1)
		{
			return toColor(xs[0]);
		}
		
		if((sz % 4) != 0)
		{
			throw new Error("please specify colors in groups of four");
		}
		
		SB sb = new SB();
		for(int i=0; i<sz; i++)
		{
			if(i == 0)
			{
				// nothing
			}
			else if((i % 4) == 0)
			{
				sb.a(',');
			}
			else
			{
				sb.sp();
			}
			
			sb.a(toColor(xs[i]));
		}
		return sb.toString();
	}
	
	
	private static int to8bit(double x)
	{
		int v = (int)Math.round(255 * x);
		if(v < 0)
		{
			return 0;
		}
		else if(v > 255)
		{
			return 255;
		}
		return v;
	}
	
	
	public static String toValue(Object x)
	{
		if(x == null)
		{
			return "null";
		}
		else if(x instanceof Number)
		{
			Number n = (Number)x;
			int vi = n.intValue();
			double vd = n.doubleValue();
			if(vd == vi)
			{
				return String.valueOf(vi);
			}
			else
			{
				return String.valueOf(vd);
			}
		}
		else if(x instanceof Color)
		{
			return toColor(x);
		}
		else
		{
			return x.toString();
		}
	}
	
	
	public static String toValue(double x)
	{
		return String.valueOf(x);
	}
	
	
	public static String toValue(ScrollPane.ScrollBarPolicy x)
	{
		switch(x)
		{
		case ALWAYS: return "always";
		case AS_NEEDED: return "as-needed";
		case NEVER: return "never";
		}
		throw new Error("?" + x);
	}
	
	
	public static String toValues(Object ... xs)
	{
		int sz = xs.length;
		if(sz == 1)
		{
			return toValue(xs[0]);
		}
		
		if((sz % 4) != 0)
		{
			throw new Error("please specify values in groups of four");
		}
		
		SB sb = new SB();
		for(int i=0; i<sz; i++)
		{
			if(i == 0)
			{
				// nothing
			}
			else if((i % 4) == 0)
			{
				sb.a(',');
			}
			else
			{
				sb.sp();
			}
			
			sb.a(toValue(xs[i]));
		}
		return sb.toString();
	}


	/** constructs selector string */
	public static String selector(Object[] sel)
	{
		SB sb = new SB();
		for(Object x: sel)
		{
			addSelector(sb, x);
		}
		return sb.toString();
	}
	
	
	private static void addSelector(SB sb, Object x)
	{
		if(x instanceof CssStyle)
		{
			if(sb.isNotEmpty())
			{
				sb.a(' ');
			}
			
			CssStyle s = (CssStyle)x;
			sb.a('.');
			sb.a(s.getName());
		}
		else if(x instanceof CssID)
		{
			if(sb.isNotEmpty())
			{
				sb.a(' ');
			}
			
			CssID s = (CssID)x;
			sb.a('#');
			sb.a(s.getID());
		}
		else if(x instanceof String)
		{
			String s = (String)x;
			if(!s.startsWith(":"))
			{
				if(sb.isNotEmpty())
				{
					sb.a(' ');
				}
			}
			
			sb.a(s);
		}
		else if(x instanceof CssPseudo)
		{
			CssPseudo s = (CssPseudo)x;
			sb.a(s.getName());
		}
		else
		{
			throw new Error("?" + x);
		}
	}
}
