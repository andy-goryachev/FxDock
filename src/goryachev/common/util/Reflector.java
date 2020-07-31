// Copyright © 2015-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import goryachev.common.log.Log;
import java.lang.reflect.Method;


/** Reflection helper */
public class Reflector
{
	protected static final Log log = Log.get("Reflector");
	
	
	/** returns an accessible method, wrapped in an exception-eating wrapper */
	public static CMethod method(Class c, String name, Class<?> ... args)
	{
		try
		{
			Method m = c.getDeclaredMethod(name, args);
			m.setAccessible(true);
			return new CMethod(m);
		}
		catch(Exception e)
		{
			throw new Error(e);
		}
	}
	
	
	/** invokes a specified method via reflection */
	public static <T> T invoke(Class<T> returnType, String methodName, Object object, Object ... args)
	{
		if(object == null)
		{
			return null;
		}
		
		try
		{
			Class c = object.getClass();
			
			// looking for exact match
			
			int sz = args.length;
			Class[] argTypes = new Class[sz];
			for(int i=0; i<sz; i++)
			{
				Object a = args[i];
				argTypes[i] = (a == null ? null : a.getClass());
			}
			
			try
			{
				Method m = c.getDeclaredMethod(methodName, argTypes);
				Object rv = m.invoke(object, args);
				if(rv != null)
				{
					if(returnType.isAssignableFrom(rv.getClass()))
					{
						return (T)rv;
					}
				}
			}
			catch(Exception e)
			{
				log.error(e);
			}
		}
		catch(Exception e)
		{
			log.error(e);
		}
		
		return null;
	}
}
