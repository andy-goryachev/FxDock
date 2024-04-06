// Copyright © 2016-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.internal;
import goryachev.common.util.CKit;
import goryachev.common.util.Parsers;
import goryachev.common.util.SB;
import goryachev.fx.CssID;
import goryachev.fx.CssPseudo;
import goryachev.fx.CssStyle;
import java.text.DecimalFormat;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.FontSmoothingType;


/**
 * Css Tools.
 */
public class CssTools
{
	/** bold type face */
	public static final CssStyle BOLD = new CssStyle("CommonStyles_BOLD");

	private static DecimalFormat decimalFormat;
	
	
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
	
	
	// FIX this needs to support things like "c1 c1 c3 c4, c5 c6, c7"
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
	
	
	public static String toValue(FontSmoothingType t)
	{
		switch(t)
		{
		case LCD:
			return "lcd";
		case GRAY:
		default:
			return "gray";
		}
	}
	
	
	public static String toValue(double x)
	{
		return String.valueOf(x);
	}
	
	
	public static String toValue(boolean x)
	{
		return x ? "true" : "false";
	}
	
	
	public static String toValue(OverrunStyle s)
	{
		switch(s)
		{
		case CENTER_ELLIPSIS:
			return "center-ellipsis";
		case CENTER_WORD_ELLIPSIS:
			return "center-word-ellipsis";
		case CLIP:
			return "clip";
		case ELLIPSIS:
			return "ellipsis";
		case LEADING_ELLIPSIS:
			return "leading-ellipsis";
		case LEADING_WORD_ELLIPSIS:
			return "leading-word-ellipsis";
		case WORD_ELLIPSIS:
			return "word-ellipsis";
		}
		throw new Error("?" + s);
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
	
	
	public static String toValue(StrokeLineCap x)
	{
		switch(x)
		{
		case BUTT: return "butt";
		case ROUND: return "round";
		case SQUARE: return "square";
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
	
	
	public static String list(String separator, Object[] items)
	{
		SB sb = new SB();
		boolean sep = false;
		for(Object x: items)
		{
			if(sep)
			{
				sb.a(separator);
			}
			else
			{
				sep = true;
			}
			sb.a(toValue(x));
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
	
	
	public static String toQuotedString(Object x)
	{
		if(x == null)
		{
			return "null";
		}
		
		String s = x.toString();
		if(s.startsWith("\"") && s.endsWith("\""))
		{
			return s;
		}
		else
		{
			return "\"" + s + "\"";
		}
	}
	
	
	
	
	/** 
	 * formats an CSS string using the following rules:
	 * 
	 * - %s inserts an argument
	 * - %1s,%2s,%3s,... inserts 1st (at index 0), 2nd, 3rd, etc. argument
	 * - %% inserts a single '%'
	 * - \[n,r,t] inserts a NL, CR, TAB char correspondingly
	 * - a string argument is quoted if it contains spaces
	 * - a double argument is formatted to "#0.##" specification
	 */
	public static String format(String format, Object ... args)
	{
		int sz = format.length();
		SB sb = new SB(sz * 3);
		
		for(int i=0; i<sz; i++)
		{
			char c = format.charAt(i);
			switch(c)
			{
			case '%':
				i++;
				int ix = format.indexOf('s', i);
				if(ix < 0)
				{
					sb.append(format, i, sz);
					i = sz;
					continue;
				}
				else if(ix == 0)
				{
					String a = formatArgument(args[0]);
					sb.append(a);
				}
				else
				{
					String n = format.substring(i, ix);
					i = ix;
					
					if(n.length() == 0)
					{
						ix = 1;
					}
					else
					{
						ix = Parsers.parseInteger(n);
					}
					String a = formatArgument(args[ix-1]);
					sb.append(a);
				}
				break;
			case '\\':
				i++;
				c = format.charAt(i);
				switch(c)
				{
				case 'n':
					sb.append('\n');
					break;
				case 'r':
					sb.append('\r');
					break;
				case 't':
					sb.append('\t');
					break;
				default:
					sb.append(c);
				}
				break;
			default:
				sb.append(c);
			}
		}
		
		return sb.toString();
	}
	
	
	private static String formatArgument(Object x)
	{
		if(x == null)
		{
			return "";
		}
		else if(x instanceof Number)
		{
			if(decimalFormat == null)
			{
				decimalFormat = new DecimalFormat("#0.##");
			}
			return decimalFormat.format(x);
		}
		else
		{
			String s = x.toString();
			if(CKit.containsAny(s, " "))
			{
				return '"' + s + '"';
			}
			return s;
		}
	}
}
