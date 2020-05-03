// Copyright © 2013-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.test;
import goryachev.common.util.CJob;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.CSorter;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.List;
import java.util.Vector;


public class TestRunner
{
	private final CList<Class> classes = new CList<>();
	private int failed;
	protected final Vector<TestCase> cases = new Vector<>();
	protected long started;
	protected long ended;
	
	
	public TestRunner()
	{
	}


	public static void run(List<Class> tests)
	{
		initLog();

		TestRunner r = new TestRunner();
		r.print("Testing started " + (new Date()) + ".\n");

		for(Class c: tests)
		{
			r.add(c);
		}

		r.executeTests();
		r.printResults();
		
		System.out.flush();
		System.err.flush();
		
		CKit.sleep(10);
		
		// command returns the number of failed tests (or 0 if all passed)
		System.exit(-r.failed);
	}
	
	
	public static void initLog()
	{
		// TODO
	}


	public void add(Class c)
	{
		checkConstructor(c);

		classes.add(c);
	}
	
	
	@SuppressWarnings("unchecked")
	protected void checkConstructor(Class c)
	{
		try
		{
			if(c.getConstructor() != null)
			{
				if(c.getConstructors().length == 1)
				{
					return;
				}
			}
		}
		catch(Exception e)
		{
			throw new TestException("Test class must define a single no-arg constructor: " + CKit.getSimpleName(c), e);
		}
	}
	
	
	protected void extract(Class c, CList<RunEntry> list, Class<? extends Annotation> type, boolean needsStatic)
	{
		for(Method m: c.getDeclaredMethods())
		{
			int mods = m.getModifiers();
			if(Modifier.isPublic(mods))
			{
				Annotation a = m.getAnnotation(type);
				if(a != null)
				{
					if(Modifier.isStatic(mods) != needsStatic)
					{
						if(needsStatic)
						{
							throw new TestException("method " + CKit.getSimpleName(c) + "." + m.getName() + "() must be static");
						}
						else
						{
							throw new TestException("method " + CKit.getSimpleName(c) + "." + m.getName() + "() must not be static");
						}
					}

					list.add(new RunEntry(m, a));
				}
			}
		}
		
		CSorter.sort(list);
	}

	
	protected void print(String s)
	{
		System.out.print(s);
	}
	

	protected void executeTests()
	{
		CList<CJob> jobs = new CList<>();
		
		started = System.currentTimeMillis();
		
		for(final Class c: classes)
		{
			CJob job = new CJob("test " + CKit.getSimpleName(c))
			{
				protected void process() throws Exception
				{
					try
					{
						executeTestClass(this, c);
					}
					catch(Throwable e)
					{
						e.printStackTrace();
					}
				}
			};
			jobs.add(job);
			job.submit();
		}
		
		CJob.waitForAll(jobs);
		
		ended = System.currentTimeMillis();
	}
	
	
	protected void executeTestClass(CJob parent, final Class c) throws Exception
	{
		final CList<RunEntry> beforeAll = new CList<>();
		final CList<RunEntry> before = new CList<>();
		final CList<RunEntry> tests = new CList<>();
		final CList<RunEntry> after = new CList<>();
		final CList<RunEntry> afterAll = new CList<>();
		
		extract(c, beforeAll, BeforeClass.class, true);
		extract(c, before, Before.class, false);
		extract(c, tests, Test.class, false);
		extract(c, after, After.class, false);
		extract(c, afterAll, AfterClass.class, true);

		if(tests.size() == 0)
		{
			System.out.println("No tests in " + CKit.getSimpleName(c));
			return;
		}
			
		for(RunEntry m: beforeAll)
		{
			m.invoke(null);
		}
		
		CList<CJob> jobs = new CList<>();
		
		// individual tests
		for(final RunEntry m: tests)
		{
			CJob job = new CJob(parent, "test " + CKit.getSimpleName(c) + "." + m)
			{
				protected void process() throws Exception
				{
					executeInstance(c, m, before, after);
				}
			};
			jobs.add(job);
			job.submit();
		}
		
		CJob.waitForAll(jobs);
		
		for(RunEntry m: afterAll)
		{
			m.invoke(null);
		}
	}
	
	
	protected void executeInstance(Class c, RunEntry testMethod, CList<RunEntry> before, CList<RunEntry> after)
	{
		String name = CKit.getSimpleName(c) + "." + testMethod;
		final TestCase tc = new TestCase(name);
		cases.add(tc);
		tc.started();

		try
		{
			Object instance = c.newInstance();
			tc.setTestInstance(instance);
			
			for(RunEntry m: before)
			{
				m.invoke(instance);
			}
			
			try
			{
				testMethod.invoke(instance);
				testMethod.checkNoException();
			}
			catch(InvocationTargetException e)
			{
				Throwable err = e.getTargetException();
				if(testMethod.isUnexpected(err))
				{
					if(err instanceof Exception)
					{
						throw (Exception)err;
					}
					else
					{
						throw new Exception(err);
					}
				}
			}
			catch(Exception e)
			{
				throw e;
			}
			
			for(RunEntry m: after)
			{
				m.invoke(instance);
			}
			
			tc.stopped();
			testSuccess(tc);
		}
		catch(Throwable e)
		{
			tc.stopped();
			tc.setFailed(e);
			testFailed(tc);
		}
	}


	protected void testSuccess(TestCase tc)
	{
		print(".");
	}
	
	
	protected void testFailed(TestCase tc)
	{
		print("E");
	}


	public void printResults()
	{
		int count = cases.size();
		
		for(TestCase c: cases)
		{
			if(c.isFailed())
			{
				failed++;
			}
		}
		
		print("\n\n");
		
		String elapsed = CKit.formatTimePeriod(ended - started);
		
		if(failed == 0)
		{
			print("OK");
		}
		else
		{
			print("FAILED " + failed);
		}
		print(" (" + count + " tests) " + elapsed + "\n");
		
		if(failed > 0)
		{
			for(TestCase c: cases)
			{
				if(c.isFailed())
				{
					print("\n");
					
					print(c.getName() + "\n");
					print(c.getText());
					print("\n");
					print(CKit.stackTrace(c.getFailure()));
					
					print("\n");
				}
			}
		}
	}
	
	
	//
	
	
	public static class RunEntry
		implements Comparable<RunEntry>
	{
		private final Method method;
		private final Annotation annotation;
	
	
		public RunEntry(Method method, Annotation annotation)
		{
			this.method = method;
			this.annotation = annotation;
		}
		
	
		public String toString()
		{
			return getName();
		}
		
		
		public String getName()
		{
			return method.getName();
		}
	
	
		public boolean isUnexpected(Throwable e)
		{
			Class<? extends Throwable> expected = ((Test)annotation).expected();
			if(expected == null)
			{
				return true;
			}
			
			return !expected.isAssignableFrom(e.getClass());
		}
		
		
		public void checkNoException() throws Exception
		{
			Class<? extends Throwable> expected = ((Test)annotation).expected();
			if(expected == Test.NoThrowable.class)
			{
				return;
			}
			else if(expected != null)
			{
				throw new Exception("This test case is expected to throw an " + CKit.getSimpleName(expected));
			}
		}
	
	
		public void invoke(Object x) throws Exception
		{
			method.invoke(x);
		}
	
	
		public int compareTo(RunEntry x)
		{
			return getName().compareTo(x.getName());
		}
	}
}
