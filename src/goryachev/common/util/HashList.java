// Copyright © 2005-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.util.ArrayList;
import java.util.HashMap;



// unsynchonized HashMap-ArrayList combo
// essentially a linear list with hash index
public class HashList<K,V>
{
	private HashMap<K,V> hashMap;
	private ArrayList<V> arrayList;
	
	
	public HashList(int initialCapacity)
	{
		hashMap = new HashMap<K,V>(initialCapacity);
		arrayList = new ArrayList<V>(initialCapacity);
	}
	
	
	public HashList()
	{
		this(32);
	}
	
	
	public void put(K key, V d)
	{
		hashMap.put(key,d);
		arrayList.add(d);
	}
	
	
	// get object by its hash key
	public Object get(String key)
	{
		return hashMap.get(key);
	}
	
	
	// get object by its list index
	public V get(int index)
	{
		try
		{
			return arrayList.get(index);
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			return null;
		}
	}


	public int size()
	{
		return arrayList.size();
	}


	public boolean containsKey(K key)
	{
		return hashMap.containsKey(key);
	}
}
