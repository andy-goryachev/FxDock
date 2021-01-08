// Copyright © 2011-2021 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


public class CLookup
{
	private CMap<Object,Object> values;
	
	
	public CLookup()
	{
		values = new CMap();
	}
	
	
	public CLookup(Object ... pairs)
	{
		int sz = pairs.length;
		int cap = sz * 2;
		values = new CMap(cap);
		if(cap/2 != sz)
		{
			throw new RuntimeException("number of objects must be even: " + sz);
		}
		
		for(int i=0; i<sz; )
		{
			Object k = pairs[i++];
			Object v = pairs[i++];
			add(k, v);
		}
	}
	
	
	public void add(Object k, Object v)
	{
		Object old = values.put(k, v);
		if(old != null)
		{
			throw new RuntimeException("duplicate: " + old + " and " + v);
		}
		
		old = values.put(v, k);
		if(old != null)
		{
			throw new RuntimeException("duplicate: " + old + " and " + k);
		}
	}
	
	
	public Object lookup(Object x)
	{
		return values.get(x);
	}
	
	
	public Object lookup(Object x, Object defaultValue)
	{
		Object rv = values.get(x);
		if(rv != null)
		{
			return rv;
		}
		return defaultValue;
	}
	
	
	public Object[] toArray()
	{
		return values.keySet().toArray();
	}
}
