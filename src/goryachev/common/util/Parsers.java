// Copyright © 2011-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import goryachev.common.log.Log;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.HashSet;


/** 
 * Convenience methods that attempt to extract requested value, 
 * returning null if the said value can not be extracted.
 */
public class Parsers
{
	protected static final Log log = Log.get("Parsers");
	
	
	public static Double parseDouble(Object x)
	{
		if(x instanceof Number)
		{
			return ((Number)x).doubleValue();
		}
		else if(x != null)
		{
			try
			{
				String s = x.toString();
				s = s.trim();
				return Double.parseDouble(s);
			}
			catch(Exception e)
			{ }
		}
		return null;
	}
	
	
	public static double parseDouble(Object x, double defaultValue)
	{
		Double d = parseDouble(x);
		if(d == null)
		{
			return defaultValue;
		}
		else
		{
			return d;
		}
	}
	
	
	public static Integer parseInteger(Object x)
	{
		if(x instanceof Number)
		{
			return ((Number)x).intValue();
		}
		else if(x != null)
		{
			try
			{
				String s = x.toString();
				s = s.trim();
				return Integer.parseInt(s);
			}
			catch(Exception e)
			{ }
		}
		return null;
	}
	
	
	public static Integer parseIntegerHex(Object x)
	{
		if(x instanceof Number)
		{
			return ((Number)x).intValue();
		}
		else if(x != null)
		{
			try
			{
				String s = x.toString();
				s = s.trim();
				return Integer.parseInt(s, 16);
			}
			catch(Exception e)
			{ }
		}
		return null;
	}
	
	
	public static Integer[] parseIntegerArray(Object x)
	{
		if(x instanceof Integer[])
		{
			return (Integer[])x;
		}
		return null;
	}
	
	
	public static int parseInt(Object x, int defaultValue)
	{
		if(x instanceof Number)
		{
			return ((Number)x).intValue();
		}
		else if(x != null)
		{
			try
			{
				String s = x.toString();
				s = s.trim();
				return Integer.parseInt(s);
			}
			catch(Exception e)
			{ }
		}
		return defaultValue;
	}
	
	
	public static Float parseFloat(Object x)
	{
		if(x instanceof Number)
		{
			return ((Number)x).floatValue();
		}
		else if(x != null)
		{
			try
			{
				String s = x.toString();
				s = s.trim();
				return Float.parseFloat(s);
			}
			catch(Exception e)
			{ }
		}
		return null;
	}
	
	
	public static float parseFloat(Object x, float defaultValue)
	{
		Float f = parseFloat(x);
		if(f == null)
		{
			return defaultValue;
		}
		else
		{
			return f;
		}
	}
	
	
	public static Long parseLong(Object x)
	{
		if(x instanceof Number)
		{
			return ((Number)x).longValue();
		}
		else if(x != null)
		{
			try
			{
				String s = x.toString();
				s = s.trim();
				return Long.parseLong(s);
			}
			catch(Exception e)
			{ }
		}
		return null;
	}
	
	
	public static long parseLong(Object x, long defaultValue)
	{
		if(x instanceof Number)
		{
			return ((Number)x).longValue();
		}
		else if(x != null)
		{
			try
			{
				String s = x.toString();
				s = s.trim();
				return Long.parseLong(s);
			}
			catch(Exception e)
			{ }
		}
		return defaultValue;
	}
	
	
	public static String parseString(Object x, String defaultValue)
	{
		if(x == null)
		{
			return defaultValue;
		}
		return parseString(x);
	}
	
	
	public static String parseString(Object x)
	{
		if(x == null)
		{
			return null;
		}
		else if(x instanceof String)
		{
			return (String)x;
		}
		else if(x instanceof char[])
		{
			return new String((char[])x);
		}
		else
		{
			return x.toString();
		}
	}
	
	
	public static String parseStringNotNull(Object x)
	{
		if(x != null)
		{
			return x.toString();
		}
		return "";
	}
	
	
	public static String parseStringOnly(Object x)
	{
		if(x instanceof String)
		{
			return (String)x;
		}
		return null;
	}
	
	
	public static char[] parseCharArray(Object x)
	{
		if(x instanceof char[])
		{
			return (char[])x;
		}
		else if(x instanceof String)
		{
			return ((String)x).toCharArray();
		}
		return null;
	}
	
	
	public static Boolean parseBoolean(Object x)
	{
		if(x instanceof Boolean)
		{
			return (Boolean)x;
		}
		else if(x != null)
		{
			String s = x.toString();
			return "true".equalsIgnoreCase(s) || "y".equalsIgnoreCase(s) || "1".equals(s);
		}
		return null;
	}
	
	
	public static boolean parseBool(Object x, boolean defaultValue)
	{
		Boolean v = parseBoolean(x);
		if(v == null)
		{
			return defaultValue;
		}
		else
		{
			return v.booleanValue();
		}
	}
	
	
	public static boolean parseBool(Object x)
	{
		return parseBool(x, false);
	}
	
	
	public static boolean parseBooleanStrict(Object x)
	{
		return Boolean.TRUE.equals(x);
	}


	public static byte[] parseByteArray(Object x)
	{
		try
		{
			if(x instanceof byte[])
			{
				return (byte[])x;
			}
			else if(x instanceof String)
			{
				return Hex.parseByteArray((String)x);
			}
		}
		catch(Exception e)
		{
			log.error(e);
		}
		
		return null;
	}
	
	
	public static byte[] parseByteArrayQuiet(Object x)
	{
		try
		{
			return parseByteArray(x);
		}
		catch(Exception e)
		{ }
		
		return null;
	}
	
	
	public static File parseFile(Object x)
	{
		if(x != null)
		{
			try
			{
				if(x instanceof File)
				{
					return (File)x;
				}
				else if(x instanceof String)
				{
					return new File((String)x);
				}
			}
			catch(Exception e)
			{
				log.error(e);
			}
		}
		return null;
	}
	
	
	public static File parseCanonicalFile(Object x)
	{
		if(x != null)
		{
			try
			{
				File f = null;
				if(x instanceof File)
				{
					f = (File)x;
				}
				else if(x instanceof String)
				{
					f = new File((String)x);
				}
				
				if(f != null)
				{
					try
					{
						return f.getCanonicalFile();
					}
					catch(Exception e)
					{
						log.error(e);
					}
				}
			}
			catch(Exception e)
			{
				log.error(e);
			}
		}
		return null;
	}
	
	
	public static String[] parseStringArray(Object x)
	{
		if(x instanceof String[])
		{
			return (String[])x;
		}
		return null;
	}
	
	
	public static String[] parseCommaSeparatedStringArray(String s)
	{
		if(s != null)
		{
			return CKit.split(s, ',');
		}
		return null;
	}
	
	
	public static Object[] parseObjectArray(Object x)
	{
		if(x instanceof Object[])
		{
			return (Object[])x;
		}
		return null;
	}
	
	
	public static BigInteger parseBigInteger(Object x)
	{
		if(x instanceof BigInteger)
		{
			return (BigInteger)x;
		}
		else if(x instanceof Number)
		{
			try
			{
				return BigInteger.valueOf(((Number)x).longValue());
			}
			catch(Exception e)
			{ }
		}
		else if(x instanceof String)
		{
			try
			{
				return new BigInteger((String)x);
			}
			catch(Exception e)
			{ }
		}
		return null;
	}
	
	
	public static BigInteger parseBigIntegerNotNull(Object x)
	{
		BigInteger v = parseBigInteger(x);
		if(v == null)
		{
			throw new IllegalArgumentException("not a BigInteger: " + x);
		}
		return v;
	}
	
	
	public static BigDecimal parseBigDecimal(Object x)
	{
		if(x instanceof BigDecimal)
		{
			return (BigDecimal)x;
		}
		else if(x instanceof Number)
		{
			try
			{
				return new BigDecimal(((Number)x).doubleValue());
			}
			catch(Exception e)
			{ }
		}
		else if(x instanceof String)
		{
			try
			{
				return new BigDecimal((String)x);
			}
			catch(Exception e)
			{ }
		}
		return null;
	}
	
	
	public static Exception parseException(Object x)
	{
		if(x instanceof Exception)
		{
			return (Exception)x;
		}
		if(x instanceof Throwable)
		{
			return new Exception((Throwable)x);
		}
		else
		{
			return null;
		}
	}


	public static ByteBuffer parseByteBuffer(Object x)
	{
		if(x != null)
		{
			if(x instanceof byte[])
			{
				return ByteBuffer.wrap((byte[])x);
			}
		}
		return null;
	}


	@SuppressWarnings("unchecked")
	public static <T> HashSet<T> parseHashSet(Object x)
	{
		if(x != null)
		{
			if(x instanceof Collection)
			{
				return new HashSet<>((Collection)x);
			}
		}
		return new HashSet<>();
	}
	
	
	/** parses an Enum value */
	public static <T extends Enum> T parseEnum(Object val, Class<T> type, T defaultValue)
	{
		if(val != null)
		{
			T[] values = type.getEnumConstants();
			for(T v: values)
			{
				if(v == val)
				{
					return v;
				}
				
				String s = val.toString();
				if(v.toString().equals(s))
				{
					return v;
				}
			}
		}
		return defaultValue;
	}
}
