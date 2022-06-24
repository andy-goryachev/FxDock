// Copyright © 2011-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class CMap<K,V>
	extends HashMap<K,V>
{
	public CMap()
	{
	}
	
	
	public CMap(int capacity)
	{
		super(capacity);
	}
	
	
	public CMap(Map<? extends K, ? extends V> m)
	{
		super(m);
	}
	
	
	public CList<K> keys()
	{
		return new CList<>(keySet());
	}
	
	
	/** removes values identified by the supplied keys.  passing null has no effect */
	public void removeAll(Collection<K> keys)
	{
		if(keys != null)
		{
			for(K k: keys)
			{
				remove(k);
			}
		}
	}
	
	
	public V set(K k, V v)
	{
		if(v == null)
		{
			return remove(k);
		}
		else
		{
			return put(k, v);
		}
	}
	
	
	public Object clone()
	{
		return copyCMap();
	}
	
	
	public CMap<K,V> copyCMap()
	{
		return new CMap<>(this);
	}
	
	
	public void loadArray(Object ... xx)
	{
		if(xx != null)
		{
			int sz = xx.length;
			if((sz < 0) || CKit.isOdd(sz))
			{
				throw new IllegalArgumentException("array must contain even number of elements");
			}
			
			for(int i=0; i<sz; )
			{
				K k = (K)xx[i++];
				V v = (V)xx[i++];
				put(k, v);
			}
		}
	}
}
