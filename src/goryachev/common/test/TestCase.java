// Copyright © 2013-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.common.test;
import goryachev.common.util.SB;


public class TestCase
{
	private final String name;
	private Object test;
	private Throwable failure;
	private SB text = new SB();
	private long startTime;
	private long stopTime;
	protected static final ThreadLocal<TestCase> currentTestCase = new ThreadLocal<>();
	
	
	public TestCase(String name)
	{
		this.name = name;
		currentTestCase.set(this);
	}
	
	
	public String getName()
	{
		return name;
	}
	
	
	public static String getNameCurrent()
	{
		TestCase tc = get();
		return tc == null ? "" : tc.getName();
	}
	
	
	public void setTestInstance(Object test)
	{
		this.test = test;
	}
	
	
	public Object getTestInstance()
	{
		return test;
	}
	
	
	public void setFailed(Throwable e)
	{
		failure = e;
	}
	
	
	public boolean isFailed()
	{
		return failure != null;
	}
	
	
	public Throwable getFailure()
	{
		return failure;
	}
	
	
	public static TestCase get()
	{
		return currentTestCase.get();
	}


	public static void print(Object x)
	{
		TestCase tc = get();
		if(tc == null)
		{
			System.out.println(x);	
		}
		else
		{
			tc.appendText(String.valueOf(x));
		}
	}
	
	
	public synchronized void appendText(String s)
	{
		text.append(s);
		text.append("\n");
	}
	
	
	public synchronized String getText()
	{
		return text.toString();
	}


	public void started()
	{
		startTime = System.currentTimeMillis();
	}
	
	
	public long getStartTime()
	{
		return startTime;
	}
	
	
	public void stopped()
	{
		stopTime = System.currentTimeMillis();
	}
	
	
	public long getStopTime()
	{
		return stopTime;
	}
}
