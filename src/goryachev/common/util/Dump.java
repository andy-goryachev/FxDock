// Copyright © 2004-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/** converts various objects to their printable user-friendly representation for debugging purposes */
public class Dump
{
	private static final String HEX = "0123456789ABCDEF";
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMdd-HH:mm:ss.SSS");


	public static String list(Object x)
	{
		if(x == null)
		{
			return "<null>";
		}
		else
		{
			if(x instanceof int[])
			{
				return listIntegerArray((int[])x);
			}
			if(x instanceof float[])
			{
				return listFloatArray((float[])x);
			}
			else if(x instanceof byte[])
			{
				return hex((byte[])x, 0);
			}
			else if(x.getClass().isArray())
			{
				return listObjectArray((Object[])x);
			}
			else if(x instanceof Collection)
			{
				return listCollection((Collection)x);
			}
			else if(x instanceof Enumeration)
			{
				return listEnumeration((Enumeration)x);
			}
			else if(x instanceof Iterable)
			{
				return listIterable((Iterable)x);
			}
			else if(x instanceof Iterator)
			{
				return listIterator((Iterator)x);
			}
			else if(x instanceof Map)
			{
				return listMap((Map)x);
			}
			else
			{
				return "Can't list " + CKit.getSimpleName(x);
			}
		}
	}
	
	
	private static String listIntegerArray(int[] a)
	{
		if(a == null)
		{
			return "null";
		}

		SB sb = new SB();
		
		for(int i=0; i<a.length; i++)
		{
			sb.append('\n');
			sb.append(format(i));
			sb.append(" [").append(format(a[i])).append(']');
		}
		
		return sb.toString();
	}
	
	
	private static String listFloatArray(float[] ar)
	{
		SB sb = new SB();
		
		for(int i=0; i<ar.length; i++)
		{
			sb.append('\n');
			sb.append(format(i));
			sb.append(" [").append(ar[i]).append(']');
		}
		
		return sb.toString();
	}


	private static String listObjectArray(Object[] ar)
	{
		SB sb = new SB();

		for(int i=0; i<ar.length; i++)
		{
			sb.append('\n');
			sb.append(format(i));
			sb.append(" [").append(ar[i]).append(']');
		}
		
		return sb.toString();
	}
	
	
	private static String listEnumeration(Enumeration<?> en)
	{
		CList<Object> a = new CList<Object>();
		while(en.hasMoreElements())
		{
			a.add(en.nextElement());
		}
		return list(a.toArray());
	}
	
	
	private static String listCollection(final Collection<?> c)
	{
		return list(c.toArray());
	}
	
	
	private static String listIterable(final Iterable<?> c)
	{
		return list(c.iterator());
	}
	
	
	private static String listIterator(final Iterator<?> c)
	{
		CList<Object> a = new CList<Object>();
		while(c.hasNext())
		{
			a.add(c.next());
		}
		return list(a.toArray());
	}
	
	
	private static String listMap(Map<?,?> map)
	{
		CList<String> a = new CList<>();
		Iterator<?> it = map.entrySet().iterator();
		while(it.hasNext())
		{
			Map.Entry<?,?> en = (Map.Entry<?,?>)it.next();
			a.add(en.getKey() + "=" + en.getValue());
		}
		
		return list(a.toArray());
	}
	
	
	// dumps byte array into a nicely formatted String
	// printing address first, then 16 bytes of hex then ASCII representation then newline
	//     "0000  00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00  ................" or
	// "00000000  00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00  ................" 
	// depending on startAddress
	// address starts with startAddress
	public static String hex(byte[] bytes, long startAddress)
	{
		if(bytes == null)
		{
			return "";
		}
		
		SB sb = new SB(((bytes.length/16)+1) * 77 + 1);
		hex(sb, bytes, startAddress, 0);
		return sb.toString();
	}
	
	
	private static void hex(SB sb, byte[] bytes, long startAddress, int indent)
	{
		boolean bigfile = ((startAddress + bytes.length) > 65535);
		
		int col = 0;
		long addr = startAddress;
		int lineStart = 0;
		//sb.append('\n');

		for(int i=0; i<bytes.length; i++)
		{
			if(col == 0)
			{
				// indent
				for(int j=0; j<indent; j++)
				{
					sb.a(' ');
				}
				
				// offset
				if(col == 0)
				{
					lineStart = i;
					if(bigfile)
					{
						hex(sb,(int)(addr >> 24));
						hex(sb,(int)(addr >> 16));
					}
					hex(sb,(int)(addr >> 8));
					hex(sb,(int)(addr));
					sb.append("  ");
				}
			}
			
			// byte
			hex(sb, bytes[i]);
			sb.append(' ');

			// space or newline
			if(col >= 15)
			{
				dumpASCII(sb, bytes, lineStart);
				col = 0;
			}
			else
			{
				col++;
			}

			addr++;
		}

		if(col != 0)
		{
			while(col++ < 16)
			{
				sb.append("   ");
			}

			dumpASCII(sb, bytes, lineStart);
		}
	}
	
	
	// dumps byte array into a nicely formatted String
	// printing address first, then 16 bytes of hex then ASCII representation then newline
	//     "0000  00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00  ................" or
	// "00000000  00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00  ................" 
	public static String hex(byte[] bytes)
	{
		return hex(bytes, 0);
	}
	
	
	public static String hex(long x)
	{
		char[] cs = new char[16];
		int shift = 60;
		
		for(int i=0; i<cs.length; i++)
		{
			cs[i] = HEX.charAt(((int)(x >>> shift)) & 0x0f);
			shift -= 4;
		}
		return new String(cs);
	}
	
	
	// find static (final) field name by its value
	public static String field(Class<?> cls, int n)
	{
		try
		{
			Field[] fields = cls.getFields();
			for(int i=0; i<fields.length; i++)
			{
				if(fields[i].getType() == int.class)
				{
					if(fields[i].getInt(null) == n)
					{
						return fields[i].getName();											
					}
				}
			}
		}
		catch(Throwable t)
		{
		}
		return String.valueOf(n);
	}


	// find static (final) field name by its value
	public static String field(Class<?> cls, short n)
	{
		try
		{
			Field[] fields = cls.getFields();
			for(int i=0; i<fields.length; i++)
			{
				if(fields[i].getType() == short.class)
				{
					if(fields[i].getShort(null) == n)
					{
						return fields[i].getName();											
					}
				}
			}
		}
		catch(Throwable t)
		{
		}
		return String.valueOf(n);
	}
	
	
	// find static (final) field name by its value
	public static String field(Class<?> cls, byte n)
	{
		try
		{
			Field[] fields = cls.getFields();
			for(int i=0; i<fields.length; i++)
			{
				if(fields[i].getType() == byte.class)
				{
					if(fields[i].getByte(null) == n)
					{
						return fields[i].getName();											
					}
				}
			}
		}
		catch(Throwable t)
		{
		}
		return String.valueOf(n);
	}


	// find static (final) field name by its value
	public static String field(Class<?> cls, String s)
	{
		try
		{
			Field[] fields = cls.getFields();
			for(int i=0; i<fields.length; i++)
			{
				if(fields[i].getType() == String.class)
				{
					if(fields[i].get(null).equals(s))
					{
						return fields[i].getName();											
					}
				}
			}
		}
		catch(Throwable t)
		{
		}
		return s;
	}
	
	
	private static String format(int n)
	{
		String s = "          " + n;
		return s.substring(s.length()-10, s.length());
	}
	
	
	private static void dumpASCII(SB sb, byte[] bytes, int lineStart)
	{
		// first, print padding
		sb.append(' ');
	
		int max = Math.min(bytes.length,lineStart+16);
		for(int i=lineStart; i<max; i++)
		{
			int d = bytes[i] & 0xff;
			if((d < 0x20) || (d >= 0x7f))
			{
				d = '.';
			}
			sb.append((char)d);
		}
		
		sb.append('\n');
	}
	

	public static void hex(SB sb, int c)
	{
		sb.append(HEX.charAt((c >> 4) & 0x0f));
		sb.append(HEX.charAt(c & 0x0f));
	}
	
	
	public static String hex1(int d)
	{
		return String.valueOf(HEX.charAt(d & 0x0f));
	}
	
	
	public static String hex2(int d)
	{
		char[] ch = new char[2];
		ch[0] = HEX.charAt((d >> 4) & 0x0f);
		ch[1] = HEX.charAt(d & 0x0f);
		return new String(ch);
	}

	
	public static String toHexString(byte[] b)
	{
		if(b == null)
		{
			return "<null>";
		}
		else
		{
			int sz = b.length;
			SB sb = new SB(sz + sz);
			for(int i=0; i<sz; i++)
			{
				int d = b[i];
				sb.append(hex1(d >> 4));
				sb.append(hex1(d));
			}
			return sb.toString();
		}
	}
	
	
	public static String date(Date x)
	{
		synchronized(dateFormat)
		{
			return dateFormat.format(x);
		}
	}
	
	
	public static String date(long x)
	{
		synchronized(dateFormat)
		{
			return dateFormat.format(x);
		}
	}
	
	
	public static String describe(Object x)
	{
		SB sb = new SB();
		describe(x, sb, 0);
		return sb.toString();
	}
	

	public static void describe(Object x, SB sb, int indent)
	{
		for(int i=0; i<indent; i++)
		{
			sb.a("  ");
		}

		if(x == null)
		{
			sb.a("<null>");
		}
		else
		{
			// TODO format int/long with hex
			if(x instanceof String)
			{
				sb.a("String=");
				toShortString((String)x, sb);
			}
			else if(x instanceof byte[])
			{
				describeByteArray((byte[])x, sb, indent + 1);
			}
			else if(x.getClass().isArray())
			{
				describeArray(x, sb, indent + 1);
			}
			else if(x instanceof Collection)
			{
				describeCollection((Collection)x, sb, indent + 1);
			}
			else if(x instanceof Map)
			{
				describeMap((Map)x, sb, indent + 1);
			}
			else if(isPrimitive(x.getClass()))
			{
				sb.a(simpleName(x)).a("=").a(x);
			}
			else if(x instanceof Enum)
			{
				sb.a(x);
			}
			else
			{
				describeObject(x, sb, indent + 1);
			}
		}
	}
	
	
	private static boolean isStatic(Field f)
	{
		return Modifier.isStatic(f.getModifiers());
	}
	
	
	private static void describeObject(Object x, SB sb, int indent)
	{
		if(indent > 2)
		{
			// prevent stack overflow with circular dependencies 
			return;
		}
		
		Class c = x.getClass();
		sb.nl();
		
		sb.sp(indent);
		sb.append(c.getSimpleName());
		sb.nl();
		
		try
		{
			CMultiMap<String,Object> m = new CMultiMap<>();
			
			while(c != null)
			{
				Field[] fs = c.getDeclaredFields();
				for(Field f: fs)
				{
					if(isStatic(f))
					{
						// skip static fields
						continue;
					}
					
					Object v;
					try
					{
						f.setAccessible(true);
						v = f.get(x);
					}
					catch(Exception e)
					{
						v = "<ERR> unable to describe";
					}
					
					m.put(f.getName(), v);
				}
				
				c = c.getSuperclass();
				if(c == Object.class)
				{
					c = null;
				}
			}
			
			CList<String> names = new CList<>(m.keySet());
			CSorter.sort(names);
			
			for(String fname: names)
			{
				sb.sp(indent);
				sb.sp();
				sb.append(fname);
				sb.append(": ");
				
				List<Object> vals = m.get(fname);
				if(vals.size() == 1)
				{
					describe(vals.get(0), sb, indent+1);
				}
				else
				{
					for(Object v: vals)
					{
						describe(v, sb, indent+1);
						sb.nl();
					}
				}
			}
			
			sb.nl();
		}
		catch(Exception e)
		{
			sb.sp(indent);
			sb.sp();
			sb.append("<ERR> unable to describe"); 
		}
	}
	
	
	public static boolean isPrimitive(Class c)
	{
		if(c.isPrimitive())
		{
			return true;
		}
		
		if     (c == Boolean.class) return true;
		else if(c == Character.class) return true;
		else if(c == Byte.class) return true;
		else if(c == Short.class) return true;
		else if(c == Integer.class) return true;
		else if(c == Long.class) return true;
		else if(c == Float.class) return true;
		else if(c == Double.class) return true;
		else if(c == BigDecimal.class) return true;
		else if(c == BigInteger.class) return true;
		else return false;
	}
	
	
	public static void toShortString(String s, SB sb)
	{
		toShortString(s, sb, 80);
	}
	
	
	public static void dumpString(String s, SB sb)
	{
		toShortString(s, sb, Integer.MAX_VALUE);
	}
	
	
	public static void toShortString(String s, SB sb, int max)
	{
		s = s.replace("\r", "\\r");
		s = s.replace("\n", "\\n");
		s = s.replace("\t", "\\t");
		
		sb.a('"');
		
		int len = s.length();
		if(len < max)
		{
			sb.a(s);
		}
		else
		{
			sb.a(s.substring(0, len-1)).a("…");
		}
		
		sb.a('"');
	}
	
	
	private static void describeMap(Map x, SB sb, int indent)
	{
		sb.a(CKit.getSimpleName(x)).a("(").a(x.size()).a(")").nl();
		
		for(Object k: x.keySet())
		{
			sb.sp(indent);
			sb.a(k).a(": ");
			Object v = x.get(k);
			describe(v, sb, indent+1);
			sb.nl();
		}
	}
	
	
	private static void describeCollection(Collection x, SB sb, int indent)
	{
		sb.a(CKit.getSimpleName(x)).a("(").a(x.size()).a(")").nl();
		
		for(Object item: x.toArray())
		{
			describe(item, sb, indent+1);
			sb.nl();
		}
	}
	
	
	private static void describeArray(Object x, SB sb, int indent)
	{
		int sz = Array.getLength(x);
		sb.a(CKit.getSimpleName(x.getClass().getComponentType())).a("[").a(sz).a("]\n");
		for(int i=0; i<sz; i++)
		{
			describe(Array.get(x, i), sb, indent+1);
			sb.nl();
		}
	}
	
	
	private static void describeByteArray(byte[] bytes, SB sb, int indent)
	{
		sb.a('\n');
		hex(sb, bytes, 0, indent);
	}
	
	
	public static String className(Object x)
	{
		if(x == null)
		{
			return "<null>";
		}
		else
		{
			return x.getClass().getName();
		}
	}
	
	
	/** returns simple name of an object's class */
	public static String simpleName(Object x)
	{
		if(x == null)
		{
			return "<null>";
		}
		else if(x instanceof Class)
		{
			return ((Class)x).getSimpleName() + ".class";
		}
		else
		{
			Class c = x.getClass();
			return c.getSimpleName();
		}
	}

	
	public static String shorter(Object x)
	{
		if(x == null)
		{
			return "<null>";
		}
		
		String s = x.toString();
		int max = 6;
		if(s.length() > max)
		{
			return s.substring(0, max);
		}
		return s;
	}
	
	
	public static String toPrintable(Object x)
	{
		if(x == null)
		{
			return "";
		}
		
		String s = x.toString();
		int len = s.length();
		SB sb = new SB(len);
		for(int i=0; i<len; i++)
		{
			char c = s.charAt(i);
			switch(c)
			{
			case '\b':
				sb.append("\\b");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\f':
				sb.append("\\f");
				break;
			default:
				if(c < 0x20)
				{
					sb.append("\\x");
					sb.append(Hex.toHexByte(c));
				}
				else
				{
					sb.append(c);
				}
			}
		}
		return sb.toString();
	}
}
