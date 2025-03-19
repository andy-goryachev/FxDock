// Copyright © 2024-2025 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.input;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;


/**
 * Function Identifier which corresponds to a method in the Control behavior.
 */
public final class Func
{
	private Object name;
	
	
	public Func()
	{
		name = new Throwable().getStackTrace()[1];
	}
	
	
	@Override
	public String toString()
	{
		if(name instanceof StackTraceElement s)
		{
			name = resolveName(s);
		}
		return name.toString();
	}
	
	
	private String resolveName(StackTraceElement st)
	{
		String className = st.getClassName();
		try
		{
			Class c = Class.forName(className);
			Field[] fs = c.getDeclaredFields();
			for(Field f: fs)
			{
				int m = f.getModifiers();
				if(Modifier.isStatic(m) && Modifier.isFinal(m))
				{
					Object v = f.get(null);
					if(v == this)
					{
						return className + ':' + f.getName();
					}
				}
			}
		}
		catch(Exception e)
		{ }
		
		return className + ':' + st.getLineNumber(); 
	}
}
