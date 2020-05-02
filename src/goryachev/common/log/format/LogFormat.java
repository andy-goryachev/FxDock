// Copyright © 2017-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.log.format;
import goryachev.common.util.SB;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * Log Formatter tries to format the message while minimizing heap allocations.
 * 
 * Format Symbols:
 * 
 * {{} - the symbol {
 * {} - arguments in order
 * {0}...{n} - arguments 0...n
 * {%...} - printf style
 * {date.FORMAT} - accepts Number, Date, Calendar, TemporalAccessor
 * {json}
 * {unicode} - unicode code point from a number or a decimal string
 * {unicode.hex} - unicode code point from a number or a hex string
 * 
 * {map} - use {list}?
 * {map.keys}
 * {map.keys.sorted}
 * {list} - collection, array, map
 * {list.sorted}
 * {hex}
 * {hexdump}
 * {hexdump.ascii}
 * {hashcode}
 * 
 * to change order of arguments:
 * {1,date}
 * {1,%02f}
 * {1,hexdump}
 * 
 * TODO substitute gson with a lightweight code that stops at awt, swing, fx
 */
public class LogFormat
{
	private static final String DATE = "date.";
	private static final String UNICODE = "unicode";
	private static Gson gson;
	
	
	public static String format(String format, Object arg1)
	{
		return formatPrivate(format, arg1, null, null, null);
	}
	
	
	public static String format(String format, Object arg1, Object arg2)
	{
		return formatPrivate(format, arg1, arg2, null, null);
	}
	
	
	public static String format(String format, Object arg1, Object arg2, Object arg3)
	{
		return formatPrivate(format, arg1, arg2, arg3, null);
	}
	
	
	public static String format(String format, Object ... args)
	{
		return formatPrivate(format, null, null, null, args);
	}
	
	
	protected static int indexOf(String s, int start, char c1, char c2)
	{
		int len = s.length();
		for(int i=start; i<len; i++)
		{
			char c = s.charAt(i);
			
			if((c == c1) || (c == c2))
			{
				return i;
			}
		}
		return -1;
	}
	
	
	protected static int parseArgNumber(String format, int start, int end)
	{
		int ix = indexOf(format, start, ',', '}');
		if(ix >= 0)
		{
			if(ix <= end)
			{
				try
				{
					int rv = Integer.parseInt(format.substring(start, ix));
					if(rv >= 0)
					{
						return rv;
					}
				}
				catch(NumberFormatException ignore)
				{
				}
			}
		}
		
		return -1;
	}
	
	
	protected static boolean regionMatches(String text, int start, String pattern)
	{
		return text.regionMatches(start, pattern, 0, pattern.length());
	}
	
	
	protected static Object getArgument(int ix, Object arg1, Object arg2, Object arg3, Object[] args)
	{
		switch(ix)
		{
		case 0:
			if(arg1 != null)
			{
				return arg1;
			}
			break;
		case 1:
			if(arg2 != null)
			{
				return arg2;
			}
			break;
		case 2:
			if(arg3 != null)
			{
				return arg3;
			}
			break;
		}
		
		if(args != null)
		{
			if(ix < args.length)
			{
				return args[ix];
			}
		}
		return null;
	}
	
	
	protected static void formatJson(SB sb, Object arg)
	{
		if(arg == null)
		{
			sb.append("null");
		}
		else
		{
			try
			{
				if(gson == null)
				{
					// TODO need a customized library to generate a json-like output
					// limiting depth, avoiding cycles, and having special handling for
					// things like swing/fx components.
					// also include transient fields that gson does not serialize.
					gson = new GsonBuilder().
						setLenient().
						setPrettyPrinting().
						disableInnerClassSerialization().
						serializeSpecialFloatingPointValues().
						create();
				}
				
				String s = gson.toJson(arg);
				sb.nl();
				sb.append(s);
			}
			catch(Throwable ignore)
			{
				sb.append(arg);
			}
		}
	}
	
	
	protected static void formatUnicode(SB sb, Object arg)
	{
		if(arg == null)
		{
			sb.append("null");
		}
		else
		{
			try
			{
				if(arg instanceof Number)
				{
					int cp = ((Number)arg).intValue();
					sb.appendCodePoint(cp);
					return;
				}
				else
				{
					int cp = Integer.parseInt(arg.toString());
					sb.appendCodePoint(cp);
					return;
				}
			}
			catch(Exception e)
			{
			}
		
			sb.append(arg);
		}
	}
	
	
	protected static void formatUnicodeHex(SB sb, Object arg)
	{
		if(arg == null)
		{
			sb.append("null");
		}
		else
		{
			try
			{
				if(arg instanceof Number)
				{
					int cp = ((Number)arg).intValue();
					sb.appendCodePoint(cp);
					return;
				}
				else
				{
					int cp = Integer.parseInt(arg.toString(), 16);
					sb.appendCodePoint(cp);
					return;
				}
			}
			catch(Exception e)
			{
			}
		
			sb.append(arg);
		}
	}
	
	
	protected static void formatDate(SB sb, String fmt, Object arg)
	{
		if(arg == null)
		{
			sb.append("null");
		}
		else
		{
			try
			{
				if(arg instanceof TemporalAccessor)
				{
					DateTimeFormatter f = DateTimeFormatter.ofPattern(fmt);
					String s = f.format((TemporalAccessor)arg);
					sb.append(s);
					return;
				}
				
				if(arg instanceof Calendar)
				{
					arg = ((Calendar)arg).getTime();
				}
				
				if((arg instanceof Number) || (arg instanceof Date))
				{
					SimpleDateFormat f = new SimpleDateFormat(fmt);
					String s = f.format(arg);
					sb.append(s);
					return;
				}
			}
			catch(Exception ignore)
			{
			}
			
			// use toString() for errors and other types
			sb.append(arg.toString());
		}
	}
	
	
	protected static FormatSpec parseFormatSpec(String format, int ix, int ex)
	{
		if(ix == ex)
		{
			return FormatSpec.PLAIN;
		}
		else if(regionMatches(format, ix, "{"))
		{
			return FormatSpec.BRACE;
		}
		else if(regionMatches(format, ix, "json"))
		{
			return FormatSpec.JSON;
		}
		else if(regionMatches(format, ix, "%"))
		{
			return FormatSpec.PRINTF;
		}
		else if(regionMatches(format, ix, DATE))
		{
			return FormatSpec.DATE;
		}
		else if(regionMatches(format, ix, UNICODE))
		{
			if(regionMatches(format, ix + UNICODE.length(), ".hex"))
			{
				return FormatSpec.UNICODE_HEX;
			}
			else
			{
				return FormatSpec.UNICODE;
			}
		}
		
		return FormatSpec.AS_IS;
	}
	

	protected static String formatPrivate(String format, Object arg1, Object arg2, Object arg3, Object[] args)
	{
		int len = format.length();
		SB sb = new SB(len + 16);
		
		int currentArg = 0;
		int start = 0;
		int ix;
		while((ix = format.indexOf('{', start)) >= 0)
		{
			if(ix < 0)
			{
				// no opening brace
				return format;
			}
						
			int ex = format.indexOf('}', ix + 1);
			if(ex < 0)
			{
				// missing closing brace
				break;
			}
			
			if(ix > start)
			{
				sb.append(format, start, ix);
			}

			ix++;
			
			// argument number
			int num = parseArgNumber(format, ix, ex);
			if(num >= 0)
			{
				ix = indexOf(format, ix, ',', '}');
			}
			
			// format type
			FormatSpec spec = parseFormatSpec(format, ix, ex);
			
			Object arg;
			String fmt;
			
			switch(spec)
			{
			case AS_IS:
				sb.append(format, ix - 1, ex + 1);
				break;
			case BRACE:
				sb.append("{");
				break;
			case DATE:
				fmt = format.substring(ix + DATE.length(), ex);
				arg = getArgument(num < 0 ? currentArg++ : num, arg1, arg2, arg3, args);
				formatDate(sb, fmt, arg);
				break;
			case JSON:
				arg = getArgument(num < 0 ? currentArg++ : num, arg1, arg2, arg3, args);
				formatJson(sb, arg);
				break;
			case PLAIN:
				arg = getArgument(num < 0 ? currentArg++ : num, arg1, arg2, arg3, args);
				sb.append(arg == null ? "null" : arg.toString());
				break;
			case PRINTF:
				fmt = format.substring(ix, ex);
				arg = getArgument(num < 0 ? currentArg++ : num, arg1, arg2, arg3, args);
				new Formatter(sb).format(fmt, arg);
				break;
			case UNICODE:
				arg = getArgument(num < 0 ? currentArg++ : num, arg1, arg2, arg3, args);
				formatUnicode(sb, arg);
				break;
			case UNICODE_HEX:
				arg = getArgument(num < 0 ? currentArg++ : num, arg1, arg2, arg3, args);
				formatUnicodeHex(sb, arg);
				break;
			default:
				// TODO
				throw new Error("?" + spec);
			}
			
			start = ex + 1;
		}
		
		if(start < len)
		{
			sb.append(format, start, len);
		}
		
		return sb.toString();
	}
}
