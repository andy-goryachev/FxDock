// Copyright (c) 2015-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


// TODO add weak references
public class Broadcast
{
	public static interface Handler<K>
	{
		public void handleEvent(K key);
	}
	
	//
	
	private static CMultiMap<Object,Object> handlers = new CMultiMap();
	
	
	private static synchronized Object[] getHandlers(Object k)
	{
		CList<Object> hs = handlers.get(k);
		if(hs == null)
		{
			return null;
		}
		else
		{
			return hs.toArray();
		}
	}
	
	
	public static synchronized <K extends Enum> void subscribe(K key, Handler<K> h)
	{
		handlers.put(key, h);
	}
	
	
	public static synchronized <K extends Object> void subscribe(Class<? extends K> type, Handler<K> h)
	{
		handlers.put(type, h);
	}
	
	
	public static synchronized <K> void remove(Handler<K> h)
	{
		for(Object k: handlers.keySet())
		{
			CList<Object> hs = handlers.get(k);
			hs.remove(h);
		}
	}
	
	
	public static void fire(Object ev)
	{
		Object key = (ev instanceof Enum) ? ev : ev.getClass();
		Object[] hs = getHandlers(key);
		if(hs != null)
		{
			int sz = hs.length;
			for(int i=0; i<sz; i++)
			{
				try
				{
					((Handler)hs[i]).handleEvent(ev);
				}
				catch(Exception e)
				{
					Log.err(e);
				}
			}
		}
	}
}
