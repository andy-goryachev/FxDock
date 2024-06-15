// Copyright © 2012-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.function.Consumer;


/**
 * Unsynchronized List of WeakListeners.
 */
public class WeakList<T>
{
	private CList<WeakReference<T>> list;
	
	
	public WeakList()
	{
		this(8);
	}
	
	
	public WeakList(int size)
	{
		list = new CList<>(size);
	}
	
	
	public void gc()
	{
		int sz = list.size();
		for(int i=sz-1; i>=0; i--)
		{
			WeakReference<T> ref = list.get(i);
			T item = ref.get();
			if(item == null)
			{
				list.remove(i);
			}
		}
	}


	public CList<T> asList()
	{
		int sz = list.size();
		CList<T> rv = new CList<>(sz);
		for(int i=sz-1; i>=0; i--)
		{
			WeakReference<T> ref = list.get(i);
			T item = ref.get();
			if(item == null)
			{
				list.remove(i);
			}
			else
			{
				rv.add(item);
			}
		}
		return rv;
	}
	
	
	public void forEach(Consumer<T> client)
	{
		int sz = list.size();
		for(int i=sz-1; i>=0; i--)
		{
			WeakReference<T> ref = list.get(i);
			T item = ref.get();
			if(item == null)
			{
				list.remove(i);
			}
			else
			{
				client.accept(item);
			}
		}
	}
	
	
	public T get(int ix)
	{
		return list.get(ix).get();
	}


	public void add(T item)
	{
		list.add(new WeakReference<>(item));
	}
	
	
	public void add(int index, T item)
	{
		list.add(index, new WeakReference<>(item));
	}
	
	
	public int size()
	{
		return list.size();
	}
	
	
	public int indexOf(T item)
	{
		Objects.nonNull(item);
		int sz = list.size();
		for(int i=0; i<sz; i++)
		{
			WeakReference<T> ref = list.get(i);
			T v = ref.get();
			if(v != null)
			{
				if(item.equals(v))
				{
					return i;
				}
			}
		}
		return -1;
	}
	
	
	public void remove(T item)
	{
		int sz = list.size();
		for(int i=sz-1; i>=0; i--)
		{
			WeakReference<T> ref = list.get(i);
			T x = ref.get();
			if(x == null)
			{
				list.remove(i);
			}
			else if(item == x)
			{
				list.remove(i);
			}
		}
	}
	
	
	public T remove(int ix)
	{
		WeakReference<T> ref = list.remove(ix);
		return ref.get();
	}
	
	
	public void clear()
	{
		list.clear();
	}
	
	
	public WeakReference<T> getRef(int ix)
	{
		return list.get(ix);
	}
	
	
	@Override
	public String toString()
	{
		int sz = list.size();
		SB sb = new SB(sz * 8);
		sb.append("[");
		
		for(int i=0; i<sz; i++)
		{
			if(i > 0)
			{
				sb.append(",");
			}
			
			WeakReference<T> ref = list.get(i);
			T item = ref.get();
			if(item == null)
			{
				sb.append("<null>");
			}
			else
			{
				sb.append(item);
			}
		}
		
		sb.append("]");
		return sb.toString();
	}
}
