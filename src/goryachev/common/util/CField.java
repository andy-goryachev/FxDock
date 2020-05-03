// Copyright © 2016-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import goryachev.common.log.Log;
import java.lang.reflect.Field;


/**
 * A "typesafe" java.lang.reflect.Field equivalent that does not throw Exceptions.
 */
public final class CField<T>
{
	private final Field field;
	
	
	public CField(Class c, String name)
	{
		this.field = init(c, name);
	}
	
	
	private static Field init(Class c, String name)
	{
		try
		{
			Field f = c.getDeclaredField(name);
			f.setAccessible(true);
			return f;
		}
		catch(Throwable e)
		{
			Log.err(e);
			return null;
		}
	}
	
	
	public T get(Object obj)
	{
		if(field != null)
		{
			try
			{
				return (T)field.get(obj);
			}
			catch(Throwable e)
			{
				Log.err(e);
			}
		}
		return null;
	}
	
	
	public void set(Object obj, T value)
	{
		if(field != null)
		{
			try
			{
				field.set(obj, value);
			}
			catch(Throwable e)
			{
				Log.err(e);
			}
		}
	}
}
