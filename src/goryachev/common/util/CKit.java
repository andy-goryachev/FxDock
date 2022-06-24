// Copyright © 1996-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import goryachev.common.io.CWriter;
import goryachev.common.log.Log;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipFile;


public final class CKit
{
	public static final String COPYRIGHT = "Copyright © 1996-2022 Andy Goryachev <andy@goryachev.com>  All Rights Reserved.";
	protected static final Log log = Log.get("CKit");
	public static final char APPLE = '\u2318';
	public static final char BOM = '\ufeff';
	public static final String[] emptyStringArray = new String[0];
	public static final Charset CHARSET_8859_1 = Charset.forName("8859_1");
	public static final Charset CHARSET_ASCII = Charset.forName("US-ASCII");
	public static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");
	private static AtomicInteger id = new AtomicInteger(); 
	private static Boolean eclipseDetected;
	private static final double LOW_MEMORY_CHECK_THRESHOLD = 0.9;
	private static final double LOW_MEMORY_FAIL_AFTER_GC_THRESHOLD = 0.87;
	private static final long MS_IN_A_SECOND = 1000L;
	private static final long MS_IN_A_MINUTE = 60000L;
	private static final long MS_IN_AN_HOUR = 3600000L;
	private static final long MS_IN_A_DAY = 86400000L;
	private static final double NANOSECONDS_IN_A_SECOND = 1_000_000_000.0;
	
	
	public static void close(Closeable x)
	{
		try
		{
			if(x != null)
			{
				x.close();
			}
		}
		catch(Throwable ignore)
		{ }
	}
	
	
	public static void close(Socket x)
	{
		try
		{
			if(x != null)
			{
				x.close();
			}
		}
		catch(Throwable ignore)
		{ }
	}
	
	
	public static void close(ZipFile x)
	{
		try
		{
			if(x != null)
			{
				x.close();
			}
		}
		catch(Throwable ignore)
		{ }
	}
	
	
	public static boolean equals(Object a, Object b)
	{
		if(a == b)
		{
			return true;
		}

		if(a == null)
		{
			if(b == null)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else if(b == null)
		{
			return false;
		}
		else
		{
			Class ca = a.getClass();
			Class cb = b.getClass();
			if(ca.isArray() && cb.isArray())
			{
				Class ta = ca.getComponentType();
				Class tb = cb.getComponentType();
				
				if(ta.isPrimitive() || tb.isPrimitive())
				{
					if(ta.equals(tb))
					{
						if(ta == byte.class)
						{
							return Arrays.equals((byte[])a, (byte[])b);
						}
						else if(ta == boolean.class)
						{
							return Arrays.equals((boolean[])a, (boolean[])b);
						}
						else if(ta == char.class)
						{
							return Arrays.equals((char[])a, (char[])b);
						}
						else if(ta == short.class)
						{
							return Arrays.equals((short[])a, (short[])b);
						}
						else if(ta == short.class)
						{
							return Arrays.equals((short[])a, (short[])b);
						}
						else if(ta == int.class)
						{
							return Arrays.equals((int[])a, (int[])b);
						}
						else if(ta == long.class)
						{
							return Arrays.equals((long[])a, (long[])b);
						}
						else if(ta == float.class)
						{
							return Arrays.equals((float[])a, (float[])b);
						}
						else if(ta == double.class)
						{
							return Arrays.equals((double[])a, (double[])b);
						}

						else
						{
							return false;
						}
					}
					else
					{
						return false;
					}
				}
				else
				{
					return Arrays.deepEquals((Object[])a, (Object[])b);
				}
			}
			else
			{
				return a.equals(b);
			}
		}
	}


	public static boolean notEquals(Object a, Object b)
	{
		return !equals(a, b);
	}
	
	
	/** returns true if the character is a whitespace or a space character (0x00a0 for example) */
	public static boolean isBlank(int c)
	{
		if(Character.isWhitespace(c))
		{
			return true;
		}
		else if(Character.isSpaceChar(c))
		{
			return true;
		}
		return false;
	}
	
	
	public static boolean isBlank(Object x)
	{
		if(x == null)
		{
			return true;
		}
		else if(x instanceof char[])
		{
			return ((char[])x).length == 0;
		}
		else
		{
			// without trim() and allocating a new string
			String s = x.toString();
			int beg = 0;
			int end = s.length();

			while((beg < end) && isBlank(s.charAt(beg)))
			{
				beg++;
			}
			while((beg < end) && isBlank(s.charAt(end - 1)))
			{
				end--;
			}
			return beg == end;
		}
	}
	
	
	public static boolean isNotBlank(Object x)
	{
		return !isBlank(x);
	}
	
	
	public static boolean isEmpty(Collection<?> x)
	{
		if(x != null)
		{
			if(x.size() > 0)
			{
				return false;
			}
		}
		return true;
	}
	
	
	public static boolean isNotEmpty(Collection<?> x)
	{
		return !isEmpty(x);
	}
	
	
	public static void sleep(long ms)
	{
		if(ms > 0)
		{
			try
			{
				Thread.sleep(ms);
			}
			catch(InterruptedException ignore)
			{ }
		}
	}
	
	
	/** sleeps, if necessary, to insure minimum delay from start */
	public static void comfortSleep(long start, long minDelay)
	{
		long t = start + minDelay - System.currentTimeMillis();
		sleep(t);
	}

	
	public static URL getPackageResource(Class<?> c, String resource)
	{
		String pkg = c.getPackage().getName().replace(".","/");
		if(pkg.length() != 0)
		{
			resource = pkg + '/' + resource;
		}
		
		return c.getClassLoader().getResource(resource);
	}

	
	public static int indexOf(Collection<?> c, Object d)
	{
		if(c != null)
		{
			int ix = 0;
			for(Object item: c)
			{
				if(equals(item, d))
				{
					return ix;
				}
				
				++ix;
			}
		}
		
		return -1;
	}
	
	
	public static int indexOf(Object[] a, Object d)
	{
		if(a != null)
		{
			int ix = 0;
			for(Object item: a)
			{
				if(equals(item, d))
				{
					return ix;
				}
				
				++ix;
			}
		}
		
		return -1;
	}
	
	
	// convert milliseconds to MM:SS or HHH:MM:SS String
	public static String msToString(long ms)
	{
		StringBuffer sb = new StringBuffer();
		long n;
		ms /= 1000;

		if((n = ms/3600) != 0)
		{
			sb.append(n);
			sb.append(":");
		}

		ms %= 3600;

		sb.append(ms/600);
		ms %= 600;
		sb.append(ms/60);
		sb.append(':');
		ms %= 60;
		sb.append(ms/10);
		ms %= 10;
		sb.append(ms);

		return sb.toString();
	}


	public static String readString(Class cs, String resource) throws Exception
	{
		return readString(cs.getResourceAsStream(resource));
	}
	
	
	public static String readString(InputStream is) throws Exception
	{
		Reader in = new InputStreamReader(is, CHARSET_UTF8);
		try
		{
			SB sb = new SB(16384);
			int c;
			while((c = in.read()) != -1)
			{
				if(sb.length() == 0)
				{
					if(c == BOM)
					{
						continue;
					}
				}
				sb.append((char)c);
			}
			return sb.toString();
		}
		finally
		{
			close(in);
		}
	}


	public static String readStringQuiet(Class cs, String resource)
	{
		try
		{
			return readString(cs, resource);
		}
		catch(Exception e)
		{
			log.error(e);
			return null;
		}
	}


	public static String readString(String resource) throws Exception
	{
		return readString(resource, CHARSET_UTF8);
	}


	public static String readString(String resource, Charset encoding) throws Exception
	{
		InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(resource);
		try
		{
			return readString(in, encoding);
		}
		finally
		{
			close(in);
		}
	}
	
	
	public static String readString(File f, Charset cs) throws Exception
	{
		return readString(new FileInputStream(f), cs);
	}
	
	
	public static String readString(File f, int max, Charset cs) throws Exception
	{
		return readString(new FileInputStream(f), max, cs);
	}
	
	
	public static String readString(File f) throws Exception
	{
		return readString(f, CHARSET_UTF8);
	}
	
	
	public static String readStringQuiet(File f)
	{
		return readStringQuiet(f, Integer.MAX_VALUE);
	}
	
	
	public static String readStringQuiet(File f, int max)
	{
		try
		{
			return readString(f, max, CHARSET_UTF8);
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	
	public static String readString(InputStream is, String encoding) throws Exception
	{
		Reader in = new InputStreamReader(is, encoding);
		try
		{
			return readString(in);
		}
		finally
		{
			close(is);
		}
	}
	
	
	public static String readString(Reader in) throws Exception
	{
		return readString(in, Integer.MAX_VALUE);
	}
	

	public static String readString(InputStream is, Charset cs) throws Exception
	{
		return readString(is, Integer.MAX_VALUE, cs);
	}
	
	
	public static String readString(InputStream is, int max, Charset cs) throws Exception
	{
		if(!(is instanceof BufferedInputStream))
		{
			is = new BufferedInputStream(is);
		}
		
		Reader in = new InputStreamReader(is, cs);
		try
		{
			return readString(in, max);
		}
		finally
		{
			close(is);
		}
	}
	
	
	public static String readString(Reader in, int max) throws Exception
	{
		try
		{
			boolean first = true;
			StringBuilder sb = new StringBuilder(16384);
			int c;
			while((c = in.read()) != -1)
			{
				if(first)
				{
					first = false;
					if(c == BOM)
					{
						continue;
					}
				}
				
				if(sb.length() >= max)
				{
					break;
				}
				
				sb.append((char)c);
			}
			return sb.toString();
		}
		finally
		{
			close(in);
		}
	}
	
	
	public static String[] readLines(Class cs, String resource) throws Exception
	{
		String s = readString(cs, resource);
		return readLines(s);
	}
	
	
	public static String[] readLines(File f) throws Exception
	{
		String s = readString(f);
		return readLines(s);
	}
	
	
	private static String[] readLines(String text) throws Exception
	{
		BufferedReader rd = new BufferedReader(new StringReader(text));
		try
		{
			CList<String> lines = new CList();
			String s;
			while((s = rd.readLine()) != null)
			{
				lines.add(s);
			}
			return toArray(lines);
		}
		finally
		{
			close(rd);
		}
	}


	public static int compare(String a, String b)
	{
		if(a == null)
		{
			if(b != null)
			{
				return -1;
			}
			else
			{
				return 0;
			}
		}
		else
		{
			if(b == null)
			{
				return 1;
			}
			else
			{
				return a.compareTo(b);
			}
		}
	}
	
	
	public static int compare(long a, long b)
	{
		if(a < b)
		{
			return -1;
		}
		else if(a == b)
		{
			return 0;
		}
		else
		{
			return 1;
		}
	}
	
	
	/** universal comparison to be used when other logic fails */
	@SuppressWarnings("unchecked")
	public static int compareLastResort(Object a, Object b)
	{
		if(a == null)
		{
			if(b == null)
			{
				return 0;
			}
			else 
			{
				return -1;
			}
		}
		else if(b == null)
		{
			return 1;
		}
		
		Class ca = a.getClass();
		Class cb = b.getClass();
		if(ca == cb)
		{
			if(a instanceof Comparable)
			{
				return ((Comparable)a).compareTo(b);
			}
			
			return a.toString().compareTo(b.toString());
		}
		else
		{
			// will produce different result if obfuscation changes class name
			return ca.getName().compareTo(cb.getName());
		}
	}
	
	
	public static int computeHashCode(Object ... ss)
	{
		int hash = 0;
		for(Object s: ss)
		{
			if(s != null)
			{
				hash ^= s.hashCode();
			}
		}
		return hash;
	}

	
	/** returns path to root or null if the root is not a parent of the specified file */
	public static String pathToRoot(File root, File file)
	{
		try
		{
			root = root.getCanonicalFile();
		}
		catch(Exception e)
		{ }
		
		try
		{
			file = file.getCanonicalFile();
		}
		catch(Exception e)
		{ }
		
		SB sb = pathToRoot(null, root, file, 0);
		return sb == null ? null : sb.toString();
	}
	
	
	protected static SB pathToRoot(SB sb, File root, File file, int level)
	{
		if(file == null)
		{
			return null;
		}
		else if(root.equals(file))
		{
			if(level == 0)
			{
				return null;
			}
			else
			{
				return new SB();
			}
		}
		else
		{
			File p = file.getParentFile();
			
			sb = pathToRoot(sb, root, p, level + 1);
			if(sb == null)
			{
				return null;
			}
			else if(level > 0)
			{
				sb.append(file.getName());
				sb.append("/");
			}
			return sb;
		}
	}
	
	
	public static void write(File f, String text) throws Exception
	{
		write(f, text, CHARSET_UTF8);
	}
	
	
	public static void write(File f, String text, Charset encoding) throws Exception
	{
		FileTools.ensureParentFolder(f);
		FileOutputStream out = new FileOutputStream(f);
		try
		{
			if(text != null)
			{
				out.write(text.getBytes(encoding));
			}
		}
		finally
		{
			close(out);
		}
	}
	
	
	public static void write(byte[] buffer, String filename) throws Exception
	{
		write(buffer, new File(filename));
	}
	
	
	public static void write(byte[] buffer, File f) throws Exception
	{
		FileTools.ensureParentFolder(f);
		FileOutputStream out = new FileOutputStream(f);
		try
		{
			out.write(buffer);
		}
		finally
		{
			close(out);
		}
	}
	
	
	public static byte[] readBytes(File f) throws Exception
	{
		return readBytes(f, Integer.MAX_VALUE);
	}

	
	@SuppressWarnings("resource") // actually no resource leak, the compiler does not understand our close()
	public static byte[] readBytes(File f, int maxSize) throws Exception
	{
		int len = (int)Math.min(maxSize, f.length());
		byte[] buf = new byte[len];

		FileInputStream in = new FileInputStream(f);
		try
		{
			int read = 0;

			while(read < len)
			{
				int rv = in.read(buf, read, len-read);
				if(rv < 0)
				{
					throw new IOException("eof");
				}
				else
				{
					read += rv;
				}
			}
		}
		finally
		{
			close(in);
		}
		return buf; 
	}


	public static int sign(long x)
	{
		if(x < 0)
		{
			return -1;
		}
		else if(x > 0)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
	
	
	/** Splits a string using any whitespace as delimiter.  Returns a non-null value. */
	public static String[] split(String s)
	{
		CList<String> list = new CList<>();
		
		if(s != null)
		{
			int start = 0;
			int sz = s.length();
			boolean white = true;
			for(int i=0; i<sz; i++)
			{
				char c = s.charAt(i);
				if(isBlank(c))
				{
					if(!white)
					{
						String sub = s.substring(start, i);
						list.add(sub);
						white = true;
					}
				}
				else
				{
					if(white)
					{
						start = i;
						white = false;
					}
				}
			}
			
			if(!white)
			{
				String sub = s.substring(start, sz);
				list.add(sub);
			}
		}
		
		return list.toArray(new String[list.size()]);
	}
	
	
	/**
	 * Splits a string.  Works slightly different than String.split():
	 *  1. does not use regex pattern and therefore faster
	 *  2. splits ("a,", ",") -> String[] { "a", "" }
	 *  while the regular split omits the empty string
	 * Always returns a non-null value.
	 */
	public static String[] split(String s, String delim)
	{
		CList<String> list = new CList<>();
		
		if(s != null)
		{
			int start = 0;
			for(;;)
			{
				int ix = s.indexOf(delim, start);
				if(ix >= 0)
				{
					list.add(s.substring(start,ix));
					start = ix + delim.length();
				}
				else
				{
					list.add(s.substring(start, s.length()));
					break;
				}
			}
		}
		
		return list.toArray(new String[list.size()]);
	}
	
	
	// similar split, using single char delimiter
	// 1. does not use regex pattern and therefore faster
	// 2. splits ("a,", ",") -> String[] { "a", "" }
	//    while the regular split omits the empty string
	public static String[] split(CharSequence s, char delim)
	{
		return split(s, delim, false);
	}


	public static String[] split(CharSequence s, char delim, boolean includeDelimiter)
	{
		CList<String> a = new CList<>();

		if(s != null)
		{
			int start = 0;
			for(;;)
			{
				int ix = indexOf(s, delim, start);
				if(ix >= 0)
				{
					a.add(s.subSequence(start, ix).toString());
					if(includeDelimiter)
					{
						a.add(s.subSequence(ix, ix+1).toString());
					}
					start = ix + 1;
				}
				else
				{
					a.add(s.subSequence(start, s.length()).toString());
					break;
				}
			}
		}

		return a.toArray(new String[a.size()]);
	}
	
	
	public static int indexOf(CharSequence s, char ch, int start)
	{
		int len = s.length();
		for(int i=start; i<len; i++)
		{
			char c = s.charAt(i);
			if(c == ch)
			{
				return i;
			}
		}
		return -1;
	}
	
	
	/** Splits a string using any of the characters in the delimiters string */
	public static String[] splitAny(String s, String delimiters)
	{
		CList<String> list = new CList<>();
		
		int start = 0;
		boolean white = true;
		int len = s.length();
		
		for(int i=0; i<len; i++)
		{
			char c = s.charAt(i);
			if(delimiters.indexOf(c) >= 0)
			{
				// encountered a delimiter
				if(!white)
				{
					list.add(s.substring(start, i));
					white = true;
				}
			}
			else
			{
				if(white)
				{
					start = i;
					white = false;
				}
			}
		}
		
		if(!white)
		{
			list.add(s.substring(start));
		}
		
		return list.toArray(new String[list.size()]);
	}
	
	
	public static String stackTrace(Throwable e)
	{
		if(e == null)
		{
			return null;
		}
		
		return stackTrace(e, 0);
	}

	
	public static String stackTrace(Throwable e, int level)
	{
		SB sb = new SB();
		printStackTrace(sb, e, level);
		return sb.toString();
	}


	private static void printStackTrace(SB sb, Throwable e, int level)
	{
		sb.a(e).nl();
		
		StackTraceElement[] trace = e.getStackTrace();
		for(int i=level; i<trace.length; i++)
		{
			StackTraceElement em = trace[i];
			sb.a("\tat ").a(em).nl();
		}

		Throwable cause = e.getCause();
		if(cause != null)
		{
			printEnclosedStackTrace(sb, cause, trace, "Caused by: ");
		}
	}


	private static void printEnclosedStackTrace(SB sb, Throwable e, StackTraceElement[] enclosingTrace, String caption)
	{
		// Compute number of frames in common between this and enclosing trace
		StackTraceElement[] trace = e.getStackTrace();
		int m = trace.length - 1;
		int n = enclosingTrace.length - 1;
		while(m >= 0 && n >= 0 && trace[m].equals(enclosingTrace[n]))
		{
			m--;
			n--;
		}
		int framesInCommon = trace.length - 1 - m;

		sb.a(caption).a(e).nl();
		
		for(int i=0; i<=m; i++)
		{
			sb.a("\tat ").a(trace[i]).nl();
		}
		
		if(framesInCommon != 0)
		{
			sb.a("\t... ").a(framesInCommon).a(" more").nl();
		}

		Throwable ourCause = e.getCause();
		if(ourCause != null)
		{
			printEnclosedStackTrace(sb, ourCause, trace, "Caused by: ");
		}
	}

	
	/** converts byte array to a String assuming UTF-8 encoding */
	public static String toString(byte[] b)
	{
		if(b == null)
		{
			return null;
		}
		
		return new String(b, CHARSET_UTF8);
	}
	
	
	/** converts argument to its toString() representation or null */
	public static String toStringOrNull(Object x)
	{
		return (x == null) ? null : x.toString(); 
	}
	
	
	public static <T> String toString(T[] items)
	{
		if(items == null)
		{
			return "null";
		}
		else
		{
			SB sb = new SB(512);
			boolean comma = false;
			sb.a("[");
			for(T item: items)
			{
				if(comma)
				{
					sb.a(", ");
				}
				else
				{
					comma = true;
				}
				
				sb.a(item == null ? "null" : item.toString());
			}
			sb.a("]");
			return sb.toString();
		}
	}


	public static void forceGC()
	{
		System.runFinalization();
		System.gc();
	}
	
	
	/** returns amount of theoretically available memory */
	public static long availableMemory()
	{
		Runtime r = Runtime.getRuntime();
		return r.maxMemory() - (r.totalMemory() - r.freeMemory());
	}


	public static String stripBOM(String s)
	{
		if(s != null)
		{
			if(s.startsWith("\ufeff"))
			{
				return s.substring(1);
			}
		}
		return s;
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

	
	public static String toStringOrBlank(Object x)
	{
		return x == null ? "" : x.toString();
	}

	
	public static String getExceptionMessage(Throwable e)
	{
		if(e == null)
		{
			return null;
		}
		
		String msg = e.getMessage();
		if(isBlank(msg))
		{
			msg = e.getClass().getSimpleName();
		}
		return msg;
	}


	public static String getSimpleName(Object x)
	{
		return Dump.simpleName(x);
	}


	public static boolean startsWith(String a, String prefix)
	{
		if(a != null)
		{
			return a.startsWith(prefix);
		}
		return false;
	}


	public static void readFully(InputStream in, byte b[]) throws Exception
	{
		int offset = 0;
		while(offset < b.length)
		{
			int count = in.read(b, offset, b.length - offset);
			if(count < 0)
			{
				throw new EOFException("read only " + offset + " bytes instead of " + b.length);
			}
			offset += count;
		}
	}

	
	/** returns the file "extension", 
	 * the part of the file name, lowercased,  from the last dot, or complete filename if there is no dot */
	public static String getExtension(String name)
	{
		if(name != null)
		{
			int ix = name.lastIndexOf('.');
			if(ix >= 0)
			{
				return name.substring(ix+1).toLowerCase();
			}
		}
		return "";
	}
	
	
	/** returns the file base name (without the "extension"), 
	 * the part of the file name until the last dot, or an empty string if there is no dot */
	public static String getBaseName(String name)
	{
		if(name != null)
		{
			int ix = name.lastIndexOf('.');
			if(ix >= 0)
			{
				return name.substring(0, ix);
			}
		}
		return "";
	}
	

	public static boolean sameClass(Object a, Object b)
	{
		if(a == null)
		{
			return (b == null);
		}
		else if(b == null)
		{
			return false;
		}
		else
		{
			return a.getClass() == b.getClass();
		}
	}
	
	
	/** returns true if array contains a non-null x */
	public static boolean contains(Object[] array, Object x)
	{
		if(array != null)
		{
			if(x != null)
			{
				for(Object a: array)
				{
					if(x.equals(a))
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	
	/** copies input stream into the output stream using 64K buffer.  returns the number of bytes copied.  supports cancellation */
	public static long copy(InputStream in, OutputStream out) throws Exception
	{
		return copy(in, out, 65536);
	}
	
	
	/** copies input stream into the output stream.  returns the number of bytes copied.  supports cancellation */
	public static long copy(InputStream in, OutputStream out, int bufferSize) throws Exception
	{
		if(bufferSize < 1)
		{
			throw new IllegalArgumentException("invalid bufferSize=" + bufferSize);
		}
		
		if(in == null)
		{
			return 0;
		}
		
		byte[] buf = new byte[bufferSize];
		long count = 0;
		for(;;)
		{
			checkCancelled();
			
			int rd = in.read(buf);
			if(rd < 0)
			{
				out.flush();
				return count;
			}
			else if(rd > 0)
			{
				out.write(buf, 0, rd);
				count += rd;
			}
		}
	}
	
	
	public static String compressString(String s) throws Exception
	{
		if(s == null)
		{
			return "";
		}
		
		ByteArrayOutputStream ba = new ByteArrayOutputStream(s.length() * 2 + 20);
		DeflaterOutputStream out = new DeflaterOutputStream(ba);
		byte[] bytes = s.getBytes(CHARSET_UTF8);
		out.write(bytes);
		out.finish();
		out.flush();
		
		byte[] compressed = ba.toByteArray();
		return Hex.toHexString(compressed);
	}
	
	
	public static String decompressString(String s) throws Exception
	{
		if(s == null)
		{
			return null;
		}
		
		byte[] compressed = Hex.parseByteArray(s);
		InflaterInputStream in = new InflaterInputStream(new ByteArrayInputStream(compressed));
		ByteArrayOutputStream out = new ByteArrayOutputStream(compressed.length * 2);
		copy(in, out);
		byte[] decompressed = out.toByteArray();
		return new String(decompressed, CHARSET_UTF8);
	}
	
	
	public static boolean isEven(int sz)
	{
		return (sz & 1) == 0;
	}
	
	
	public static boolean isOdd(int sz)
	{
		return !isEven(sz);
	}


	public static String beforeSpace(String s)
	{
		return TextTools.beforeSpace(s);
	}
	
	
	public static int indexOfWhitespace(String s)
	{
		return TextTools.indexOfWhitespace(s);
	}
	
	
	public static int indexOfWhitespace(String s, int start)
	{
		return TextTools.indexOfWhitespace(s, start);
	}
	
	
	public static boolean startsWithIgnoreCase(String s, String pattern)
	{
		return TextTools.startsWithIgnoreCase(s, pattern);
	}
	
	
	public static boolean endsWithIgnoreCase(String s, String suffix)
	{
		return TextTools.endsWithIgnoreCase(s, suffix);
	}

	
	public static void makeParentFile(File f) throws IOException
	{
		f = f.getCanonicalFile();
		File p = f.getParentFile();
		if(p != null)
		{
			p.mkdirs();
		}
	}
	

	/** ensures the name gets the specified extension (format: ".ext"), unless one already exists */
	public static File ensureExtension(File f, String ext)
	{
		String name = f.getName();
		if(name.contains("."))
		{
			return f;
		}
		else
		{
			return new File(f.getParentFile(), name + ext);
		}
	}
	
	
	/** ensures the name gets the specified extension (format: ".ext"), unless one already exists */
	public static String ensureExtension(String name, String ext)
	{
		if(name.contains("."))
		{
			return name;
		}
		else
		{
			return name + ext;
		}
	}


	/** returns (x % max) for all possible x, including negative */
	public static int mod(int x, int max)
	{
		if(x < 0)
		{
			return max + ((x + 1) % max) - 1;
		}
		else
		{
			return x % max;
		}
	}
	

	public static int id()
	{
		return id.getAndIncrement();
	}
	
	
	public static void todo()
	{
		throw new Error("(to be implemented)");
	}
	
	
	/** reads byte array from a resource local to the parent object (or class) */
	public static byte[] readBytes(Object parent, String name) throws Exception
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream(65536);
		Class c = (parent instanceof Class ? (Class)parent : parent.getClass()); 
		InputStream in = c.getResourceAsStream(name);
		try
		{
			copy(in, out);
		}
		finally
		{
			close(in);
			close(out);
		}
		return out.toByteArray();
	}
	
	
	/** reads byte array from a resource local to the parent object (or class), without throwing an exception */
	public static byte[] readBytesQuiet(Object parent, String name)
	{
		try
		{
			return readBytes(parent, name);
		}
		catch(Exception ignore)
		{
			return null;
		}
	}


	/** 
	 * checks whether the current thread has been interrupted or low memory condition exists.
	 * if interrupted - throws CancelledException
	 * if low memory condition - throws LowMemoryException
	 */
	public static void checkCancelled() throws CancelledException,LowMemoryException
	{
		if(Thread.interrupted())
		{
			throw new CancelledException();
		}
		
		if(isLowMemory())
		{
			throw new LowMemoryException();
		}
	}
	
	
	public static boolean isLowMemory()
	{
		return isLowMemory(LOW_MEMORY_CHECK_THRESHOLD, LOW_MEMORY_FAIL_AFTER_GC_THRESHOLD);
	}
	
	
	public static boolean isLowMemory(double triggerThreshold, double failThreshold)
	{
		Runtime r = Runtime.getRuntime();
		
		long total = r.totalMemory();
		long used = total - r.freeMemory();
		long max = r.maxMemory();
		
		if(used > (long)(max * triggerThreshold))
		{
			// let's see if gc can help
			System.gc();
			System.runFinalization();
			
			total = r.totalMemory();
			used = total - r.freeMemory();
			if(used > (long)(max * failThreshold))
			{
				return true;
			}
		}
		return false;
	}
	
	
	/** returns true if text string contains any character from the pattern string */
	public static boolean containsAny(String text, String pattern)
	{
		if(text != null)
		{
			for(int i=0; i<pattern.length(); i++)
			{
				char c = pattern.charAt(i);
				if(text.indexOf(c) >= 0)
				{
					return true;
				}
			}
		}
		return false;
	}

	
	public static byte[] readBytes(InputStream in) throws Exception
	{
		return readBytes(in, Integer.MAX_VALUE);
	}


	public static byte[] readBytes(InputStream in, int max) throws Exception
	{
		if(in == null)
		{
			return null;
		}
		
		int read = 0;
		byte[] buf = new byte[Math.min(max, 65536)];
		ByteArrayOutputStream ba = new ByteArrayOutputStream(65536);
		while(read < max)
		{
			int rd = in.read(buf);
			if(rd < 0)
			{
				break;
			}
			else if(rd > 0)
			{
				int allowed = max - read;
				if(allowed < rd)
				{
					ba.write(buf, 0, allowed);
					break;
				}
				else
				{
					ba.write(buf, 0, rd);
					read += rd;
				}
			}
			else
			{
				sleep(10);
			}
		}
		
		return ba.toByteArray();		
	}
	
	
	/** System.nanoTime() in milliseconds */
	public static long time()
	{
		return System.nanoTime() / 1000000L;
	}
	
	
	/** convert to lower case in ENGLISH locale in order to remove dependency on particular platform */
	public static String toLowerCase(Object x)
	{
		if(x == null)
		{
			return null;
		}
		else
		{
			return x.toString().toLowerCase(Locale.ENGLISH);
		}
	}
	
	
	/** convert to upper case in ENGLISH locale in order to remove dependency on particular platform */
	public static String toUpperCase(Object x)
	{
		if(x == null)
		{
			return null;
		}
		else
		{
			return x.toString().toUpperCase(Locale.ENGLISH);
		}
	}
	

	public static BufferedInputStream toBufferedInputStream(InputStream in)
	{
		if(in instanceof BufferedInputStream)
		{
			return (BufferedInputStream)in;
		}
		else if(in != null)
		{
			return new BufferedInputStream(in);
		}
		return null;
	}
	
	
	public static BufferedOutputStream toBufferedOutputStream(OutputStream out)
	{
		if(out instanceof BufferedOutputStream)
		{
			return (BufferedOutputStream)out;
		}
		else
		{
			return new BufferedOutputStream(out);
		}
	}
	
	
	public static BufferedReader toBufferedReader(Reader rd)
	{
		if(rd instanceof BufferedReader)
		{
			return (BufferedReader)rd;
		}
		else
		{
			return new BufferedReader(rd);
		}
	}
	
	
	public static BufferedWriter toBufferedWriter(Writer wr)
	{
		if(wr instanceof BufferedWriter)
		{
			return (BufferedWriter)wr;
		}
		else
		{
			return new BufferedWriter(wr);
		}
	}
	
	
	public static String trimBOM(String s)
	{
		if(s != null)
		{
			if(s.length() > 0)
			{
				if(s.charAt(0) == BOM)
				{
					return s.substring(1);
				}
			}
		}
		return s;
	}
	
	
	public static String getPercentString(double value, int significantDigits)
	{
		MathContext mc = new MathContext(significantDigits, RoundingMode.HALF_DOWN);
		BigDecimal d = new BigDecimal(100.0 * value, mc);
		return d.toPlainString() + "%";
	}
	

	public static void append(String filename, String s) throws Exception
	{
		append(new File(filename), s);
	}
	
	
	public static void append(File f, String s) throws Exception
	{
		FileTools.ensureParentFolder(f);
		CWriter wr = new CWriter(new FileOutputStream(f, true), CHARSET_UTF8);
		try
		{
			if(s != null)
			{
				wr.write(s);
			}
		}
		finally
		{
			close(wr);
		}
	}


	/** 
	 * Makes a deep copy of the specified object via java serialization mechanism.  
	 * Constituent parts of the supplied object must be serializable.
	 */
	public static Object deepCopy(Object x) throws Exception
	{
		try
		{
			ByteArrayOutputStream bout = new ByteArrayOutputStream(4096);
			ObjectOutputStream out = new ObjectOutputStream(bout);
			try
			{
				// serialize
				out.writeObject(x);
			}
			finally
			{
				close(out);
				out = null;
			}
			
			byte[] b = bout.toByteArray();
			bout = null;
			
			ByteArrayInputStream bin = new ByteArrayInputStream(b);
			ObjectInputStream in = new ObjectInputStream(bin);
			try
			{
				// deserialize
				return in.readObject();
			}
			finally
			{
				close(in);
			}
		}
		catch(Exception e)
		{
			throw new Exception("failed to copy " + getSimpleName(x), e);
		}
	}
	
	
	/**
	 * Encodes string to a valid URL ASCII.
	 * http://en.wikipedia.org/wiki/Percent-encoding
	 * http://www.w3schools.com/tags/ref_urlencode.asp
	 */
	public static String toURL(String s)
	{
		byte[] bytes = s.getBytes(CHARSET_UTF8);
		int sz = bytes.length;
		SB sb = new SB(sz * 3);
		
		for(int i=0; i<sz; i++)
		{
			int c = bytes[i] & 0xff;
			if(c <= 0x20)
			{
				// replace space and all control characters with '+'
				sb.append('+');
			}
			else if(c > 0x7f)
			{
				sb.append('%');
				Hex.hexByte(sb, c);
			}
			else
			{
				switch(c)
				{
				case '!':
				case '"':
				case '#':
				case '$':
				case '%':
				case '&':
				case '\'':
				case '(':
				case ')':
				case '*':
				case '+':
				case ',':
				case '/':
				case ':':
				case ';':
				case '<':
				case '=':
				case '>':
				case '?':
				case '@':
				case '[':
				case '\\':
				case ']':
				case '^':
				case '`':
				case '{':
				case '|':
				case '}':
				case '~':
					sb.append('%');
					Hex.hexByte(sb, c);
					break;
				default:
					sb.append((char)c);
				}
			}
		}
		
		return sb.toString();
	}
	
	
	public static String formatTime24(int hour, int min)
	{
		SB sb = new SB(5);
		
		if(hour < 10)
		{
			sb.a('0');
		}
		sb.a(hour);
		
		sb.a(':');
		
		if(min < 10)
		{
			sb.a('0');
		}
		sb.a(min);
		
		return sb.toString();
	}
	
	
	public static String formatTwoDigits(int x)
	{
		if((x >= 0) && (x < 10))
		{
			return "0" + x;
		}
		else
		{
			return String.valueOf(x);
		}
	}


	public static String formatTimePeriod(long t)
	{
		boolean force = false;
		SB sb = new SB();

		int d = (int)(t / MS_IN_A_DAY);
		if(d != 0)
		{
			sb.append(d);
			sb.append(':');
			t %= MS_IN_A_DAY;
			force = true;
		}

		int h = (int)(t / MS_IN_AN_HOUR);
		if(force || (h != 0))
		{
			append(sb, h, 2);
			sb.append(':');
			t %= MS_IN_AN_HOUR;
			force = true;
		}

		int m = (int)(t / MS_IN_A_MINUTE);
		if(force || (m != 0))
		{
			append(sb, m, 2);
			sb.append(':');
			t %= MS_IN_A_MINUTE;
			force = true;
		}

		int s = (int)(t / MS_IN_A_SECOND);
		if(force)
		{
			append(sb, s, 2);
		}
		else
		{
			sb.append(s);
		}
		sb.append('.');

		int ms = (int)(t % MS_IN_A_SECOND);
		append(sb, ms, 3);

		return sb.toString();
	}


	private static void append(SB sb, int n, int precision)
	{
		String s = String.valueOf(n);
		n = precision - s.length();
		if(n > 0)
		{
			sb.append("0000000000", 0, n);
		}
		sb.append(s);
	}

	
	/** concatenates two byte arrays */
	public static byte[] cat(byte[] a, byte[] b)
	{
		byte[] rv = new byte[a.length + b.length];
		System.arraycopy(a, 0, rv, 0, a.length);
		System.arraycopy(b, 0, rv, a.length, b.length);
		return rv;
	}


	public static Properties readProperties(String filename)
	{
		return readProperties(new File(filename));
	}
	
	
	public static Properties readProperties(File f)
	{
		Properties p = new Properties();
		try
		{
			FileInputStream in = new FileInputStream(f);
			try
			{
				p.load(in);
			}
			finally
			{
				close(in);
			}
		}
		catch(Exception ignore)
		{ }
		return p;
	}
	
	
	public static void writeProperties(Properties p, File f) throws Exception
	{
		if(p != null)
		{
			FileOutputStream out = new FileOutputStream(f);
			try
			{
				p.store(out, null);
			}
			finally
			{
				close(out);
			}
		}
	}
	
	
	/** determines the number of bins required to divide items into the specified number of bins */
	public static int binCount(int itemCount, int binSize)
	{
		if(itemCount == 0)
		{
			return 0;
		}
		else if(binSize == 0)
		{
			return itemCount;
		}
		else
		{
			return 1 + (itemCount - 1) / binSize;
		}
	}


	/** concatenates two byte arrays */
	public static byte[] concat(byte[] a, byte[] b)
	{
		byte[] r = new byte[a.length + b.length];
		System.arraycopy(a, 0, r, 0, a.length);
		System.arraycopy(b, 0, r, a.length, b.length);
		return r;
	}
	
	
	/** returns UTF-8 bytes */
	public static byte[] getBytes(String s)
	{
		if(s == null)
		{
			return null;
		}
		else
		{
			return s.getBytes(CHARSET_UTF8);
		}
	}
	
	
	/** alias to Math.round() typecast returns int */
	public static int round(double x)
	{
		return (int)Math.round(x);
	}
	
	
	/** alias to Math.ceil() typecast returns int */
	public static int ceil(double x)
	{
		return (int)Math.ceil(x);
	}
	
	
	/** alias to Math.floor() typecast returns int */
	public static int floor(double x)
	{
		return (int)Math.floor(x);
	}
	
	
	/** collect public static fields from a class, of specified type */
	@SuppressWarnings("unchecked")
	public static <T> CList<T> collectPublicStaticFields(Class<?> c, Class<T> type)
	{
		CList<T> rv = new CList();
		for(Field f: c.getFields())
		{
			int m = f.getModifiers();
			if(Modifier.isPublic(m) && Modifier.isStatic(m))
			{
				try
				{
					Object v = f.get(null);
					if(v != null)
					{
						if(type.isAssignableFrom(v.getClass()))
						{
							rv.add((T)v);
						}
					}
				}
				catch(Exception e)
				{
					log.error(e);
				}
			}
		}
		return rv;
	}
	
	
	/** returns true if running from Eclipse */
	public static boolean isEclipse()
	{
		if(eclipseDetected == null)
		{
			eclipseDetected = new File(".project").exists() && new File(".classpath").exists();
		}
		return eclipseDetected;
	}


	public static <T> Collection<T> asList(T ... items)
	{
		return new CList<>(items);
	}


	/** 
	 * utility method converts a String Collection to a String[].
	 * returns null if input is null 
	 */ 
	public static String[] toArray(Collection<String> coll)
	{
		if(coll == null)
		{
			return null;
		}
		return coll.toArray(new String[coll.size()]);
	}
	
	
	/** converts a collection to an array.  returns null if collection is null */
	public static <T> T[] toArray(Class<T> type, Collection<T> coll)
	{
		if(coll == null)
		{
			return null;
		}
		
		int sz = coll.size();
		T[] a = (T[])Array.newInstance(type, sz);
		return coll.toArray(a);
	}
	
	
	/** creates a string containing the specified number of tabs */
	public static String tabs(int count)
	{
		if(count <= 0)
		{
			return "";
		}
		return new SB(count).tab(count).toString();
	}
	
	
	/** creates a string containing the specified number of spaces */
	public static String spaces(int count)
	{
		if(count <= 0)
		{
			return "";
		}
		return new SB(count).sp(count).toString();
	}

	
	public static <T> T[] addAndGrow(T[] items, T item)
	{
		int len = items.length;
		T[] rv = Arrays.copyOf(items, len + 1);
		rv[len] = item;
		return rv;
	}
	
	
	public static <T> T[] removeAndShrink(T[] items, T item)
	{
		int ix = indexOf(items, item);
		if(ix < 0)
		{
			return items;
		}
		else
		{
			int len = items.length;
			T[] rv = (T[])Array.newInstance(items.getClass().getComponentType(), len - 1);
			
			if(ix > 0)
			{
				System.arraycopy(items, 0, rv, 0, ix);
			}
			
			if(ix + 1 < len)
			{
				System.arraycopy(items, ix + 1, rv, ix, len - ix - 1);
			}
			return rv;
		}
	}


	public static <K,V> CMap<K,V> toMap(Class<K> keyType, Class<V> valueType, Object ... pairs)
	{
		int sz = pairs.length;
		CMap<K,V> m = new CMap(sz / 2);
		for(int i=0; i<sz; )
		{
			K k = (K)pairs[i];
			if(!k.getClass().isAssignableFrom(keyType))
			{
				throw new Error("Expecting " + keyType + " at index " + i);
			}
			
			i++;
			
			V v = (V)pairs[i];
			if(v != null)
			{
				if(!v.getClass().isAssignableFrom(valueType))
				{
					throw new Error("Expecting " + valueType + " at index " + i);
				}
			}
			
			Object old = m.put(k, v);
			if(old != null)
			{
				throw new Error("Duplicate key " + k + " at index " + (i - 1));
			}
			
			i++;
		}
		return m;
	}


	public static <T> CSet<T> toSet(Class<T> type, T ... items)
	{
		int sz = items.length;
		CSet<T> rv = new CSet(sz);
		for(int i=0; i<sz; i++)
		{
			T item = items[i];
			if(!item.getClass().isAssignableFrom(type))
			{
				throw new Error("Expecting " + type + " at index " + i);
			}
			
			rv.add(item);
		}
		return rv;
	}
	
	
	public static String codePointToString(int cp)
	{
		 char[] cs = Character.toChars(cp);
		 return new String(cs);
	}
	
	
	public static boolean inRange(int value, int min, int max)
	{
		if(min > max)
		{
			throw new Error("min > max");
		}
		return (value >= min) && (value <= max);
	}
	

	/** IEC kibi = 2^10, or 2014 */
	public static long kibi(int x)
	{
		return 1024L * x;
	}

	
	/** IEC mebi = 2^20, or 1024 * 1024 */ 
	public static long mebi(int x)
	{
		return 1048576L * x;
	}
	
	
	/** IEC gibi = 2^30, or 1024 * 1024 * 1024 */ 
	public static long gibi(int x)
	{
		return 1073741824L * x;
	}
	
	
	/** IEC tebi = 2^40, or 1024 * 1024 * 1024 * 1024 */ 
	public static long tebi(int x)
	{
		return 1099511627776L * x;
	}
	

	public static long milliseconds(int hours, int minutes, int seconds)
	{
		return (hours * MS_IN_AN_HOUR) + (minutes * MS_IN_A_MINUTE) + (seconds * MS_IN_A_SECOND);
	}
	
	
	/** @return milliseconds for the given number of seconds */
	public static long secondsToMilliseconds(int seconds)
	{
		return seconds * MS_IN_A_SECOND;
	}
	
	
	/** @return milliseconds for the given number of minutes */
	public static long minutesToMilliseconds(int minutes)
	{
		return minutes * MS_IN_A_MINUTE;
	}
	
	
	/** @return milliseconds for the given number of hours */
	public static long hoursToMilliseconds(int hours)
	{
		return hours * MS_IN_AN_HOUR;
	}
	
	
	/** @return milliseconds for the given number of days */
	public static long daysToMilliseconds(int days)
	{
		return days * MS_IN_A_DAY;
	}
	
	
	public static byte[] copy(byte[] b)
	{
		if(b == null)
		{
			return null;
		}
		
		byte[] c = new byte[b.length];
		System.arraycopy(b, 0, c, 0, b.length);
		return c;
	}
	
	
	public static <S,T> List<T> transform(List<S> src, Function<S,T> converter)
	{
		return transform(src, null, converter);
	}
	
	
	public static <S,T> List<T> transform(List<S> src, List<T> target, Function<S,T> converter)
	{
		if(src == null)
		{
			return null;
		}
		
		int sz = src.size();
		if(target == null)
		{
			target = new CList<T>(sz);
		}
		
		for(int i=0; i<sz; i++)
		{
			S s = src.get(i);
			T t = converter.apply(s);
			target.add(t);
		}
		return target;
	}
	
	
	public static byte[] copyOf(byte[] b)
	{
		if(b == null)
		{
			return null;
		}
		
		return Arrays.copyOf(b, b.length);
	}
	
	
	/** returns a new copy of the specified array with the item added */
	public static <T> T[] add(T[] items, T item)
	{
		int len = items.length;
		T[] a = Arrays.copyOf(items, len + 1);
		a[len] = item;
		return a;
	}
	
	
	/** 
	 * returns a new copy of the specified array with the first matching item removed.
	 * the matching item is determined by CKit.equals() method.
	 * this method returns the original array if no matching item is found.
	 */
	public static <T> T[] remove(T[] items, T item)
	{
		int ix = indexOf(items, item);
		if(ix < 0)
		{
			return items;
		}
		
		int len = items.length - 1;
		T[] a = Arrays.copyOf(items, len);
		
		len = len - ix;
		if(len > 0)
		{
			System.arraycopy(items, ix + 1, a, ix, len);
		}
		
		return a;
	}
	
	
	/** returns the next power of 2 or, if overflow, the argument.  useful for allocating growing arrays */
	public static int toNeatSize(int x)
	{
		int v = Integer.highestOneBit(x);
		if(x == v)
		{
			return v;
		}
		v = (v << 1);
		if(v < 0)
		{
			return x;
		}
		return v;
	}
	
	
	public static <T> Iterator<T> iterator(T[] items)
	{
		if(items == null)
		{
			return Collections.emptyIterator();
		}
		else
		{
			return new Iterator<T>()
			{
				private int ix;
				
				
				public boolean hasNext()
				{
					return ix < items.length;
				}

				
				public T next()
				{
					return items[ix++];
				}
			};
		}
	}
	
	
	/** trims a toString() representation of an object, limiting the text to maxLength */
	public static String trim(Object x, int maxLength)
	{
		if(x == null)
		{
			return null;
		}
		
		String s = x.toString();
		if(s.length() > maxLength)
		{
			return s.substring(0, maxLength);
		}
		return s;
	}
	
	
	/** creates a new array instance, copying the contents */
	public static <T> T[] shallowCopy(T[] src)
	{
		if(src == null)
		{
			return null;
		}
		return Arrays.copyOf(src, src.length);
	}
	
	
	/** returns elapsed time in seconds, since the last time in nanoseconds, as returned by System.nanoTime() */
	public static double elapsedSeconds(long startNanoseconds)
	{
		long t = System.nanoTime() - startNanoseconds;
		return (t / NANOSECONDS_IN_A_SECOND);
	}
}
