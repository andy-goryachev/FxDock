// Copyright © 2006-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.NoSuchElementException;


// Hashtable implementation with integer keys
public class IntHashtable<T>
	implements Cloneable, Serializable
{
	private Entry<T> table[];
	private int count;
	private int threshold;
	private float loadFactor;
	private static final int MASK = 0x7FFFFFFF;
	private static final long serialVersionUID = 1;

	
	// Constructs a new, empty hashtable with the specified initial 
	// capacity and the specified load factor.
	// @param initialCapacity the initial number of buckets
	// @param loadFactor a number between 0.0 and 1.0, it defines
	//		the threshold for rehashing the hashtable into
	//		a bigger one.
	// @exception IllegalArgumentException If the initial capacity
	// is less than or equal to zero.
	// @exception IllegalArgumentException If the load factor is
	// less than or equal to zero.
	public IntHashtable(int initialCapacity, float loadFactor)
	{
		if((initialCapacity <= 0) || (loadFactor <= 0.0))
		{
			throw new IllegalArgumentException();
		}
		this.loadFactor = loadFactor;
		table = new Entry[initialCapacity];
		threshold = (int)(initialCapacity*loadFactor);
	}
	

	// Constructs a new, empty hashtable with the specified initial 
	// capacity.
	// @param initialCapacity the initial number of buckets
	public IntHashtable(int initialCapacity)
	{
		this(initialCapacity,0.75f);
	}
	
	
	// Constructs a new, empty hashtable. A default capacity and load factor
	// is used. Note that the hashtable will automatically grow when it gets
	// full.
	public IntHashtable()
	{
		this(101,0.75f);
	}
	
	
	// Returns the number of elements contained in the hashtable. 
	public int size()
	{
		return count;
	}
	
	
	// Returns true if the hashtable contains no elements.
	public boolean isEmpty()
	{
		return count == 0;
	}
	
	
	// Returns an enumeration of the hashtable's keys.
	// @see IntHashtable#elements
	public synchronized Enumeration keys()
	{
		return new Enumerator(table,true);
	}
	
	
	// Returns an enumeration of the elements. Use the Enumeration methods 
	// on the returned object to fetch the elements sequentially.
	// @see IntHashtable#keys
	public synchronized Enumeration elements()
	{
		return new Enumerator(table,false);
	}
	
	
	// Returns true if the specified object is an element of the hashtable.
	// This operation is more expensive than the containsKey() method.
	// @param value the value that we are looking for
	// @exception NullPointerException If the value being searched 
	// for is equal to null.
	// @see IntHashtable#containsKey
	public synchronized boolean contains(Object value)
	{
		if(value == null)
		{
			throw new NullPointerException();
		}
		Entry tab[] = table;
		for(int i=tab.length; i-- > 0 ; )
		{
			for(Entry<Object> e=tab[i]; e!=null; e=e.next)
			{
				if(e.value.equals(value))
				{
					return true;
				}
			}
		}
		return false;
	}


	// Returns true if the collection contains an element for the key.
	// @param key the key that we are looking for
	// @see IntHashtable#contains
	public synchronized boolean containsKey(int key)
	{
		Entry tab[] = table;
		int hash = key;
		int index = (hash & MASK) % tab.length;
		for(Entry<Object> e=tab[index]; e != null ; e=e.next)
		{
			if((e.hash == hash) && (e.key == key))
			{
				return true;
			}
		}
		return false;
	}
	
	
	// Gets the object associated with the specified key in the 
	// hashtable.
	// @param key the specified key
	// @returns the element for the key or null if the key
	// 		is not defined in the hash table.
	// @see IntHashtable#put
	public synchronized T get(int key)
	{
		Entry tab[] = table;
		int hash = key;
		int index = (hash & MASK) % tab.length;
		for(Entry<T> e=tab[index]; e != null; e=e.next)
		{
			if((e.hash == hash) && (e.key == key))
			{
				return e.value;
			}
		}
		return null;
	}
	
	
	// Rehashes the content of the table into a bigger table.
	// This method is called automatically when the hashtable's
	// size exceeds the threshold.
	protected void rehash()
	{
		int oldCapacity = table.length;
		Entry oldTable[] = table;
		
		int newCapacity = oldCapacity*2 + 1;
		Entry newTable[] = new Entry[newCapacity];
		
		threshold = (int)(newCapacity*loadFactor);
		table = newTable;
		
		for(int i=oldCapacity; i-->0; )
		{
			for(Entry<Object> old=oldTable[i]; old != null ; )
			{
				Entry<Object> e = old;
				old = old.next;
				
				int index = (e.hash & MASK) % newCapacity;
				e.next = newTable[index];
				newTable[index] = e;
			}
		}
	}
	
	
	// Puts the specified element into the hashtable, using the specified
	// key.  The element may be retrieved by doing a get() with the same key.
	// The key and the element cannot be null. 
	// @param key the specified key in the hashtable
	// @param value the specified element
	// is equal to null.
	// @see IntHashtable#get
	// @return the old value of the key, or null if it did not have one.
	//
	// Note: null values are permitted, as long as there is no call to contains()
	//
	public synchronized Object put(int key, Object value)
	{
		// Make sure the value is not null.
//		if(value == null)
//		{
//			throw new NullPointerException();
//		}
		
		// Makes sure the key is not already in the hashtable.
		Entry tab[] = table;
		int hash = key;
		int index = (hash & MASK) % tab.length;
		for(Entry<Object> e=tab[index]; e != null; e = e.next)
		{
			if((e.hash == hash) && (e.key == key))
			{
				Object old = e.value;
				e.value = value;
				return old;
			}
		}

		if(count >= threshold)
		{
			// Rehash the table if the threshold is exceeded.
			rehash();
			return put(key,value);
		}
		
		// Creates the new entry.
		Entry<Object> e = new Entry<Object>();
		e.hash = hash;
		e.key = key;
		e.value = value;
		e.next = tab[index];
		tab[index] = e;
		++count;
		return null;
	}
	
	
	// Removes the element corresponding to the key. Does nothing if the
	// key is not present.
	// @param key the key that needs to be removed
	// @return the value of key, or null if the key was not found.
	public synchronized Object remove(int key)
	{
		Entry tab[] = table;
		int hash = key;
		int index = (hash & MASK) % tab.length;
		for(Entry<Object> e=tab[index], prev=null; e != null; prev=e, e=e.next)
		{
			if((e.hash == hash) && (e.key == key))
			{
				if(prev != null)
				{
					prev.next = e.next;
				}
				else
				{
					tab[index] = e.next;
				}
				--count;
				return e.value;
			}
		}
		return null;
	}


	// Clears the hash table so that it has no more elements in it.
	public synchronized void clear()
	{
		Entry tab[] = table;
		for(int index=tab.length; --index >= 0;)
		{
			tab[index] = null;
		}
		count = 0;
	}
	
	
	// Creates a clone of the hashtable. A shallow copy is made,
	// the keys and elements themselves are NOT cloned. This is a
	// relatively expensive operation.
	public synchronized Object clone()
	{
		try
		{
			IntHashtable t = (IntHashtable)super.clone();
			t.table = new Entry[table.length];
			for(int i = table.length; i-- > 0;)
			{
				t.table[i] = (table[i] != null) ? (Entry)table[i].clone() : null;	
			}
			return t;
		}
		catch(CloneNotSupportedException e)
		{
			// This shouldn't happen, since we are Cloneable.
			throw new InternalError();
		}
	}


	public synchronized String toString()
	{
		int max = size() - 1;
		StringBuffer buf = new StringBuffer();
		Enumeration k = keys();
		Enumeration e = elements();
		buf.append("{");
		
		for(int i=0; i<=max; ++i)
		{
			String s1 = k.nextElement().toString();
			String s2 = e.nextElement().toString();
			buf.append(s1).append("=").append(s2);
			if(i < max)
			{
				buf.append(", ");
			}
		}
		buf.append("}");
		return buf.toString();
	}
	
	
	//
		
		
	protected static class Entry<T> implements Serializable
	{
		int hash;
		int key;
		T value;
		Entry next;
		private static final long serialVersionUID = 1;
		
		
		protected Object clone()
		{
			Entry<T> entry = new Entry<T>();
			entry.hash = hash;
			entry.key = key;
			entry.value = value;
			entry.next = (next != null) ? (Entry) next.clone() : null;
			return entry;
		}
	}
		
	
	//
	
		
	protected static class Enumerator implements Enumeration
	{
		boolean keys;
		int index;
		Entry table[];
		Entry entry;
		
		
		public Enumerator(Entry table[], boolean keys)
		{
			this.table = table;
			this.keys = keys;
			this.index = table.length;
		}
		
		
		public boolean hasMoreElements()
		{
			if(entry != null)
			{
				return true;
			}
			
			while(index-- > 0)
			{
				if((entry = table[index]) != null)
				{
					return true;
				}
			}
			
			return false;
		}


		public Object nextElement()
		{
			if(entry == null)
			{
				while((index-- > 0) && ((entry = table[index]) == null))
				{
				}
			}
			
			if(entry != null)
			{
				Entry<Object> e = entry;
				entry = e.next;
				return keys ? Integer.valueOf(e.key) : e.value;
			}
			
			throw new NoSuchElementException("IntHashtableEnumerator");
		}
	}
}