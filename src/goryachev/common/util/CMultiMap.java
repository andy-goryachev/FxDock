// Copyright © 2013-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.util.Map;
import java.util.Set;


/** 
 * A map-type class that maps keys to a list of values.
 * This class is not thread-safe. 
 */
public class CMultiMap<K,V>
{
	private final CMap<K,CList<V>> map;
	
	
	public CMultiMap()
	{
		this(32);
	}
	
	
	public CMultiMap(int size)
	{
		map = new CMap<>(size);
	}
	
	
	/** Returns the number of keys */
	public int size()
	{
		return map.size();
	}
	
	
	/** Returns the total number of items */
	public int getTotalItemCount()
	{
		int count = 0;
		for(K k: map.keySet())
		{
			CList<V> list = map.get(k);
			count += list.size();
		}
		return count;
	}
	
	
	/** Returns the number of items under specified key */
	public int getItemCount(K key)
	{
		CList<V> list = map.get(key);
		if(list == null)
		{
			return 0;
		}
		else
		{
			return list.size();
		}
	}
	
	
	/** 
	 * Adds a value to the list of items stored under the specified key.  
	 * Duplicate values are permitted.
	 * Returns true if the map changed as a result of this call. 
	 */
	public boolean put(K key, V val)
	{
		CList<V> c = map.get(key);
		if(c == null)
		{
			c = new CList<>();
			map.put(key, c);
		}
		return c.add(val);
	}
	
	
	/** Returns the list of values under specified key */
	public CList<V> get(K key)
	{
		return map.get(key);
	}
	
	
	/** Returns true if there is at least one value stored under the specified key */ 
	public boolean containsKey(K k)
	{
		return map.containsKey(k);
	}
	
	
	/** Returns set of keys */
	public Set<K> keySet()
	{
		return map.keySet();
	}
	
	
	public CList<K> keys()
	{
		return new CList<>(keySet());
	}
	
	
	/** Collects all the values in the map.  The order of values is not defined and may vary */
	public CList<V> collectValues()
	{
		CList<V> list = new CList<>(size() * 2);
		for(K k: map.keySet())
		{
			CList<V> c = map.get(k);
			list.addAll(c);
		}
		return list;
	}
	
	
	/** removes all values for a given key */
	public CList<V> remove(K key)
	{
		return map.remove(key);
	}
	
	
	/** removes key-value pair from the map */
	public void remove(K key, V val)
	{
		CList<V> list = map.get(key);
		if(list != null)
		{
			list.remove(val);
			
			if(list.size() == 0)
			{
				map.remove(key);
			}
		}
	}


	public void putAll(CMultiMap<K,V> m)
	{
		for(K k: m.keySet())
		{
			for(V v: m.get(k))
			{
				put(k, v);
			}
		}
	}
}
