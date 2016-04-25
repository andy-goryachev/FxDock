// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


/**
 * String Stream.
 */
public class SStream
{
	private final CList<String> list;
	
	
	public SStream()
	{
		list = new CList<String>();
	}
	
	
	public SStream(String[] ss)
	{
		list = new CList<String>(ss);
	}
	
	
	public void add(Object x)
	{
		list.add(x == null ? null : x.toString());
	}
	
	
	public void add(double x)
	{
		if((long)x == x)
		{
			list.add(Long.toString((long)x));
		}
		else
		{
			list.add(Double.toString(x));
		}
	}
}
