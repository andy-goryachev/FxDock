// Copyright © 2013-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.test;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.Dump;
import goryachev.common.util.SB;
import java.util.List;
import java.util.function.BiFunction;


/** Simple test framework */
public class TF
{
	/** checks if two arguments are CKit.equals() and throws a meaningful exception if not */
	public static void eq(Object value, Object expected)
	{
		if(CKit.notEquals(value, expected))
		{
			throw new TestException
			(
				"Unexpected value=" + 
				Dump.describe(value) + 
				", expected=" + 
				Dump.describe(expected)
			);
		}
	}
	
	
	/** checks if two arguments are not CKit.equals() and throws a meaningful exception if not */
	public static void notEquals(Object value, Object expected)
	{
		if(CKit.equals(value, expected))
		{
			throw new TestException
			(
				"Values are not expected to be equals: " + 
				Dump.describe(value) 
			);
		}
	}
	
	
	/** checks if two arguments are CKit.equals() and throws a meaningful exception if not */
	public static void eq(Object value, Object expected, Object message)
	{
		if(CKit.notEquals(value, expected))
		{
			throw new TestException
			(
				message +
				", unexpected value=" + 
				Dump.describe(value) + 
				", expected=" + 
				Dump.describe(expected)
			);
		}
	}
	
	
	/** checks whether an argument is not null */
	public static void notNull(Object x)
	{
		if(x == null)
		{
			throw new TestException("expecting a non-null value");
		}
	}
	
	
	/** checks whether an argument is not null */
	public static void isNull(Object x)
	{
		if(x != null)
		{
			throw new TestException("expecting a null value");
		}
	}
	
	
	public static void isTrue(boolean x)
	{
		if(!x)
		{
			throw new TestException("expression is not true");
		}
	}
	
	
	public static void isFalse(boolean x)
	{
		if(x)
		{
			throw new TestException("expression is not false");
		}
	}
	
	
	public static void fail()
	{
		throw new TestException("test failed");
	}
	
	
	public static void fail(String message)
	{
		throw new TestException(message);
	}

	
	/** print an item toString() representation */
	public static void print(Object x)
	{
		TestCase.print(x);
	}
	
	
	/** print a set of items */
	public static void print(Object ... xs)
	{
		SB sb = new SB();
		for(Object x: xs)
		{
			if(!sb.isEmpty())
			{
				sb.a(' ');
			}
			
			sb.a(x);
		}
		
		TestCase.print(sb);
	}
	
	
	/** adds stack trace to the test case printout */
	public static void print(Throwable e)
	{
		TestCase.print(CKit.stackTrace(e));
	}
	
	
	/** list elements of an array, map, collection, iterator, iterable. */
	public static void list(Object x)
	{
		print(Dump.list(x));
	}


	/** Runs a set of test classes */
	public static void run(Class ... tests)
	{
		TestRunner.run(new CList<>(tests));
	}
	
	
	/** runs tests in the caller class, as determined by reflection */
	public static void run()
	{
		try
		{
			StackTraceElement[] se = new Throwable().getStackTrace();
			StackTraceElement em = se[1];
			String name = em.getClassName();
			Class c = Class.forName(name);
			run(c);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	private static String get(List<?> items, int ix, String ifNone)
	{
		if(items == null)
		{
			return ifNone;
		}
		else if(ix < 0)
		{
			return "";
		}
		else if(ix >= items.size())
		{
			return ifNone;
		}
		
		return items.get(ix).toString();
	}
	
	
	private static String frame(Object x, int maxWidth, String ifNull)
	{
		String s = (x == null ? ifNull : x.toString());
		int diff = maxWidth - s.length();
		if(diff < 0)
		{
			return s.substring(0, maxWidth);
		}
		else if(diff > 0)
		{
			return s + CKit.spaces(diff);
		}
		else
		{
			return s;
		}
	}
	
	
	private static String num(int ix, int maxWidth)
	{
		String s = String.valueOf(ix);
		int diff = maxWidth - s.length();
		if(diff > 0)
		{
			s = CKit.spaces(diff) + s;
		}
		return s;
	}
	
	
	private static String sep(boolean mismatch)
	{
		return mismatch ? " ≠ " : "   ";
	}
	
	
	private static void append(CList<String> lines, int ix, String sep, String s)
	{
		s = lines.get(ix) + sep + s;
		lines.set(ix, s);
	}
	
	
	private static <T> int findMismatchIndex(List<T> left, List<T> right, BiFunction<T,T,Boolean> eqTest)
	{
		int leftSize = (left == null ? 0 : left.size()); 
		int rightSize = (right == null ? 0 : right.size());
			
		for(int i=0; i<10000; i++)
		{
			if((i >= leftSize) || (i >= rightSize))
			{
				return i;
			}
			
			T a = (left == null ? null : left.get(i));
			T b = (right == null ? null : right.get(i));
			
			if(!eqTest.apply(a, b))
			{
				return i;
			}
		}
		return -1;
	}


	/** prints two collections side by side, returning a descriptive exception */
	public static <T> RuntimeException printDiff(String nameLeft, List<T> left, String nameRight, List<T> right, BiFunction<T,T,Boolean> eqTest)
	{
		int leftSize = (left == null ? 0 : left.size());
		int rightSize = (right == null ? 0 : right.size());
		int sz = Math.max(leftSize, rightSize);
		CList<String> lines = new CList(sz);
		int numWidth = 5;
		int maxWidth = 40;
		SB sb =  new SB(lines.size() * 128);
		
		int mismatchIndex = findMismatchIndex(left, right, eqTest);
		
		// left
		for(int i=0; i<sz; i++)
		{
			sb.clear();
			sb.a(num(i, numWidth));
			sb.sp();
			sb.a(frame(get(left, i, ""), maxWidth, ""));
			lines.add(sb.getAndClear());
		}
		
		// right
		for(int i=0; i<sz; i++)
		{
			sb.clear();
			sb.a(num(i, numWidth));
			sb.sp();
			sb.a(frame(get(right, i, ""), maxWidth, ""));
			
			String sep = sep(i == mismatchIndex);
			append(lines, i, sep, sb.getAndClear());
		}
		
		// result
		sb.clear();
		sb.sp(numWidth + 1).a(frame(nameLeft, maxWidth, "LEFT"));
		sb.a(sep(false));
		sb.sp(numWidth).a(frame(nameRight, maxWidth, "RIGHT")).nl();

		for(String s: lines)
		{
			sb.a(s).nl();
		}
		print(sb);
		
		return new RuntimeException("Mismatch at index " + mismatchIndex + ": " + get(left, mismatchIndex, "N/A") + " " + get(right, mismatchIndex, "N/A"));
	}
}
