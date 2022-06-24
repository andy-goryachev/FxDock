// Copyright © 2016-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.lang.reflect.Field;


/**
 * A "typesafe" java.lang.reflect.Field wrapper that throws RuntimeExceptions.
 */
public final class CField<T>
{
	private final Field field;
	
	
	public CField(Field f)
	{
		this.field = f;
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
				throw new RuntimeException(e);
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
				throw new RuntimeException(e);
			}
		}
	}
}
