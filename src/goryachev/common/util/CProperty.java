// Copyright © 2015-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import goryachev.common.log.Log;
import java.lang.ref.WeakReference;


/** fx-like object property capable of dealing with normal and weak listeners. */
public class CProperty<T>
{
	@FunctionalInterface
	public static interface Listener<T>
	{
		public void onCPropertyChange(T old, T cur);
	}
	
	//
	
	protected static final Log log = Log.get("CProperty");
	private T value;
	private CList<Object> listeners;
	
	
	public CProperty(T value)
	{
		set(value);
	}
	
	
	public CProperty()
	{
	}
	
	
	public void set(T v)
	{
		if(CKit.notEquals(value, v))
		{
			T old = value;
			value = v;
			
			fireEvent(old, v);
		}
	}
	
	
	public T get()
	{
		return value;
	}
	
	
	protected void fireEvent(T old, T cur)
	{
		CList<Object> ls = getListeners();
		if(ls != null)
		{
			for(Object x: ls)
			{
				if(x instanceof WeakReference)
				{
					Object v = ((WeakReference)x).get();
					if(v == null)
					{
						// weak reference must have been removed
						synchronized(this)
						{
							listeners.remove(x);
						}
						
						continue;
					}
					else
					{
						x = v;
					}
				}
				
				if(x instanceof Listener)
				{
					try
					{
						((Listener)x).onCPropertyChange(old, cur);
					}
					catch(Throwable e)
					{
						log.error(e);
					}
				}
			}
		}
	}
	
	
	protected synchronized CList<Object> getListeners()
	{
		if(listeners != null)
		{
			return new CList(listeners);
		}
		return null;
	}
	
	
	protected CList<Object> listeners()
	{
		if(listeners == null)
		{
			listeners = new CList();
		}
		return listeners;
	}
	
	
	public synchronized void addListener(Listener<T> li)
	{
		listeners().add(li);
	}
	
	
	public void addListener(boolean fireImmediately, Listener<T> li)
	{
		addListener(li);
		
		if(fireImmediately)
		{
			fireEvent(null, get());
		}
	}
	
	
	public synchronized void addWeakListener(Listener<T> li)
	{
		listeners().add(new WeakReference(li));
	}
	
	
	public void addWeakListener(boolean fireImmediately, Listener<T> li)
	{
		addWeakListener(li);
		
		if(fireImmediately)
		{
			fireEvent(null, get());
		}
	}
	
	
	public synchronized void removeListener(Listener<T> li)
	{
		if(listeners != null)
		{
			for(int i=listeners.size()-1; i>=0; i--)
			{
				Object x = listeners.get(i);
				if(x == li)
				{
					listeners.remove(i);
					return;
				}
				else if(x instanceof WeakReference)
				{
					Object w = ((WeakReference)x).get();
					if(w == null)
					{
						listeners.remove(i);
						continue;
					}
					else if(w == li)
					{
						listeners.remove(i);
						return;
					}
				}
			}
		}
	}
}
