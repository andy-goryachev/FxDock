// Copyright © 2014-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


/** 
 * An Exception with an Object identifier and an optional value, 
 * useful for fast exception handling where "exception type" is determined by a fast comparison
 *    <pre>(e.getProblem() == CONSTANT)</pre>
 * and additional parameter can be obtained by
 *    <pre>Ex.getParameter(e)</pre>
 */
public class Ex
	extends Exception
{
	private final Object id;
	private Object parameter;
	
	
	public Ex(Object id)
	{
		this.id = id;
	}
	
	
	public Ex(Object id, Throwable cause)
	{
		super(cause);
		this.id = id;
	}
	
	
	public Ex(Object id, Object parameter)
	{
		this.id = id;
		this.parameter = parameter;
	}
	
	
	public Ex(Object id, Object value, Throwable cause)
	{
		super(cause);
		this.id = id;
		this.parameter = value;
	}


	@Override
	public String getMessage()
	{
		SB sb = new SB();
		sb.a(id);
		if(parameter != null)
		{
			sb.sp();
			sb.a(parameter);
		}
		if(getCause() != null)
		{
			sb.sp();
			sb.a(getCause());
		}
		return sb.toString();
	}


	public Object getID()
	{
		return id;
	}


	public static Object getID(Throwable e)
	{
		if(e instanceof Ex)
		{
			return ((Ex)e).getID();
		}
		return null;
	}


	public Object getParameter()
	{
		return parameter;
	}
}
