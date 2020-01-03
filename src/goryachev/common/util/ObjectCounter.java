// Copyright © 2013-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.util.Collection;


/** counts the number of objects in a map, as determined by hashCode() and equals() */ 
public class ObjectCounter<T>
{
	private CMap<T,Integer> counts;
	
	
	public ObjectCounter()
	{
		this(32);
	}
	
	
	public ObjectCounter(int size)
	{
		counts = new CMap(size);
	}
	
	
	protected ObjectCounter(CMap<T,Integer> m)
	{
		this.counts = new CMap(m);
	}
	
	
	public ObjectCounter copy()
	{
		return new ObjectCounter(counts);
	}
	
	
	public void add(T x)
	{
		Integer ct = counts.get(x);
		if(ct == null)
		{
			counts.put(x, Integer.valueOf(1));
		}
		else
		{
			int n = Integer.valueOf(ct) + 1;
			if(n < Integer.MAX_VALUE)
			{
				counts.put(x, Integer.valueOf(n));
			}
		}
	}
	
	
	public void remove(T x)
	{
		Integer ct = counts.get(x);
		if(ct == null)
		{
			// do nothing
		}
		else
		{
			int n = Integer.valueOf(ct) - 1;
			if(n >= 0)
			{
				counts.put(x, n);
			}
		}
	}
	
	
	public void addAll(T[] xs)
	{
		if(xs != null)
		{
			for(T x: xs)
			{
				add(x);
			}
		}
	}
	
	
	public void addAll(Collection<T> xs)
	{
		if(xs != null)
		{
			for(T x: xs)
			{
				add(x);
			}
		}
	}
	
	
	public CList<T> getKeys()
	{
		return new CList(counts.keySet());
	}
	
	
	public int getCount(T x)
	{
		return Parsers.parseInt(counts.get(x), 0);
	}
	
	
	public int size()
	{
		return counts.size();
	}
	
	
	public int getTotalCount()
	{
		int ct = 0;
		for(Integer x: counts.values())
		{
			ct += x;
		}
		return ct;
	}
	
	
	public boolean equals(Object x)
	{
		if(x == this)
		{
			return true;
		}
		else if(x instanceof ObjectCounter)
		{
			return counts.equals(((ObjectCounter)x).counts);
		}
		else
		{
			return false;
		}
	}
	
	
	public int hashCode()
	{
		return FH.hash(ObjectCounter.class, counts);
	}


	public T getTop()
	{
		T top = null;
		int count = 0;
		
		for(T x: counts.keySet())
		{
			int ct = counts.get(x);
			if(ct > count)
			{
				count = ct;
				top = x;
			}
		}

		return top;
	}
}
