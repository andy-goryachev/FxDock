// Copyright (c) 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.util.Iterator;


/**
 * String Stream.
 */
public class SStream
	implements Iterable<String>
{
	private final CList<String> list;
	private int pos;
	
	
	public SStream()
	{
		list = new CList<String>();
	}
	
	
	public SStream(String[] ss)
	{
		list = new CList<String>(ss);
	}
	
	
	public int size()
	{
		return list.size();
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


	public Iterator<String> iterator()
	{
		return list.iterator();
	}
	
	
	public String nextString()
	{
		if(pos < (list.size() - 1))
		{
			return list.get(pos++);
		}
		return null;
	}
	
	
	public double nextDouble(double defaultValue)
	{
		return Parsers.parseDouble(nextString(), defaultValue);
	}
	
	
	public double nextDouble()
	{
		return nextDouble(-1.0);
	}
	
	
	public int nextInt(int defaultValue)
	{
		return Parsers.parseInt(nextString(), defaultValue);
	}
	
	
	public int nextInt()
	{
		return nextInt(-1);
	}
	
	
	public String[] toArray()
	{
		return list.toArray(new String[list.size()]);
	}
}
