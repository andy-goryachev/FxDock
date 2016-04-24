// Copyright (c) 2010-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import java.awt.Window;
import java.util.HashMap;


/** 
 * Application-wide window tracker
 * Closes the app when the last registered window is closed.
 * Also provides a way to look up a window by associated key.
 */ 
public class WindowTracker
{
	// String->Window, Window->String
	private static HashMap<Object,Object> map = new HashMap();
	private static Runnable onExit;
	
	
	public static void opened(Window w)
	{
		map.put(w,null);
	}
	
	
	public static void closed(Window w)
	{
		Object key = map.get(w);
		if(key != null)
		{
			map.remove(key);
		}
		map.remove(w);
		
		int openWindows = 0;
		for(Object x: map.keySet())
		{
			if(x instanceof Window)
			{
				++openWindows;
			}
		}
		
		if(openWindows == 0)
		{
			if(onExit == null)
			{
				Application.exit();
			}
			else
			{
				onExit.run();
			}
		}
	}
	
	
	// null removes the registration
	public static void register(String key, Window w)
	{
		if(key == null)
		{
			Object oldKey = map.get(w);
			if(oldKey != null)
			{
				map.remove(oldKey);
			}
		}
		else
		{
			map.put(key,w);
		}
	}
	
	
	public static Window lookup(String key)
	{
		Object x = map.get(key);
		if(x instanceof Window)
		{
			return (Window)x;
		}
		return null;
	}
	
	
	public static void registerOnExit(Runnable r)
	{
		onExit = r;
	}
}
