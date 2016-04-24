// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


public abstract class CTest
{
	public abstract void test() throws Exception;
	
	
	public void err(Object x)
	{
		if(x instanceof Throwable)
		{
			((Throwable)x).printStackTrace();
		}
		else
		{
			System.err.println(x);
		}
	}
	
	
	public void fail(Object x)
	{
		throw new Rex(Parsers.parseString(x));
	}
	
	
	public void print(Object ... ss)
	{
		SB sb = new SB();
		
		for(Object s: ss)
		{
			if(sb.length() > 0)
			{
				sb.a(" ");
			}
			
			sb.a(s);
		}
		
		System.out.println(sb);
	}
	
	
	public void execute()
	{
		try
		{
			test();
			D.print("OK");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
