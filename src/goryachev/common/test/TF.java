// Copyright © 2013-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.common.test;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.Dump;
import goryachev.common.util.SB;


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
	
	
	public static void fail()
	{
		throw new TestException("test failed");
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
}
