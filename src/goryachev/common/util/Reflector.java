// Copyright (c) 2015-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.lang.reflect.Method;


public class Reflector
{
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
				Log.err(e);
			}
		}
		catch(Exception e)
		{
			Log.err(e);
		}
		
		return null;
	}
}
