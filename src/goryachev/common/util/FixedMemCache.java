// Copyright © 2013-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.util.Random;


/** simple object cache stores up to a maxItems elements */
public class FixedMemCache<K,V>
{
	private int maxItems;
	private CMap<K,V> cache = new CMap();
	private CList<K> keys = new CList();
	private Random random = new Random();
	
	
	public FixedMemCache(int maxItems)
	{
		this.maxItems = maxItems;
	}
	
	
	protected void removeRandomObject()
	{
		int sz = keys.size(); 
		if(sz > 0)
		{
			int ix = random.nextInt(sz);
			
			K key = keys.get(ix);
			keys.set(ix, keys.remove(sz-1));
			
			cache.remove(key);
		}
	}
	
	
	public synchronized V get(K key)
	{
		return cache.get(key);
	}
	
	
	public synchronized void put(K key, V value)
	{
		while(keys.size() >= maxItems)
		{
			removeRandomObject();
		}
		
		cache.put(key, value);
		keys.add(key);
	}
	
	
	public synchronized void clear()
	{
		cache.clear();
		keys.clear();
	}
}
