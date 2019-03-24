// Copyright © 2016-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * A "typesafe" Method (reflection) equivalent that throws RuntimeExceptions.
 */
public class CMethod<T>
{
	private final Method method;
	
	
	public CMethod(Method m)
	{
		this.method = m;
	}
	
	
	public T invoke(Object obj, Object ... args)
	{
		try
		{
			return (T)method.invoke(obj, args);
		}
		catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	
	public T invokeStatic(Object ... args)
	{
		try
		{
			return (T)method.invoke(null, args);
		}
		catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			throw new RuntimeException(e);
		}
	}
}
