// Copyright © 2015-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.test;


/**
 * TestException is an exception with modified stack trace: 
 * it starts with the method marked with @Test annotation.
 * 
 * This class subclasses RuntimeException so as not to add throws keywords 
 * to the test methods.
 */
public class TestException
	extends RuntimeException
{
	public TestException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	
	public TestException(Throwable cause)
	{
		super(cause);
	}
	
	
	public TestException(String message)
	{
		super(message);
	}
	
	
	public TestException()
	{
	}


	public StackTraceElement[] getStackTrace()
	{
		StackTraceElement[] ss = super.getStackTrace();
		
		String testPrefix = TestException.class.getPackage().getName();
		
		for(int i=0; i<ss.length; i++)
		{
			StackTraceElement em = ss[i];
			String name = em.getClassName();
			if(!name.startsWith(testPrefix))
			{
				// found first non-test method
				int ct = ss.length - i;
				StackTraceElement[] rv = new StackTraceElement[ct];
				System.arraycopy(ss, i, rv, 0, ct);
				return rv;
			}
		}
		
		return ss;
	}
}
