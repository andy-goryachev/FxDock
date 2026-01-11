// Copyright © 2025-2025 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.Base64;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;


/**
 * JSON Writer for simple toString() output.
 */
public class JW
{
	private enum Phase
	{
		ARRAY,
		IDLE,
		NAME,
		OBJECT
	}
	
	private static class State
	{
		public final Phase phase;
		public boolean separator;
		
		public State(Phase phase)
		{
			this.phase = phase;
		}
	}
	
	private final SB sb = new SB();
	private final CList<State> state;
	private Function<Object,String> formatter;
	
	
	public JW()
	{
		state = new CList<>();
		state.add(new State(Phase.IDLE));
	}
	
	
	/**
	 * Convenience method which converts an array of values into a JSON string.
	 */
	public static String arr(Object ... values)
	{
		return new JW().array(values).toString();
	}
	
	
	/**
	 * Convenience method which converts a single object comprised of a number of name-value pairs into a JSON string.
	 */
	public static String obj(Object ... nameValuePairs)
	{
		return new JW().object().values(nameValuePairs).toString();
	}
	
	
	/**
	 * Appends an array.
	 */
	public JW array(Object ... items)
	{
		array();
		for(Object v: items)
		{
			value(v);
		}
		end();
		return this;
	}
	
	
	public JW withFormat(Function<Object,String> formatter)
	{
		this.formatter = formatter;
		return this;
	}
	
	
	public JW withNumberFormat(String pattern)
	{
		DecimalFormat f = new DecimalFormat(pattern);
		this.formatter = (v) ->
		{
			if(v instanceof Number n)
			{
				return f.format(n);
			}
			else
			{
				return v.toString();
			}
		};
		return this;
	}
	

	/**
	 * Appends a series of name-value pairs.
	 */
	public JW values(Object ... nameValuePairs)
	{
		int sz = nameValuePairs.length;
		for(int i=0; i<sz; )
		{
			String name = (String)nameValuePairs[i++];
			Object value = nameValuePairs[i++];
			value(name, value);
		}
		return this;
	}
	
	
	public JW append(Object x)
	{
		sb.append(x);
		return this;
	}
	
	
	public JW object()
	{
		State st = state();
		switch(st.phase)
		{
		case ARRAY:
			separator(st);
			break;
		case IDLE:
			break;
		case NAME:
			state.removeLast();
			break;
		case OBJECT:
			throw new IllegalStateException("object after object?");
		}
		setPhase(Phase.OBJECT);
		sb.append("{");
		return this;
	}
	
	
	public JW array()
	{
		State st = state();
		switch(st.phase)
		{
		case ARRAY:
			separator(st);
			break;
		case IDLE:
			break;
		case NAME:
			break;
		case OBJECT:
			throw new IllegalStateException("array after object?");
		}
		setPhase(Phase.ARRAY);
		sb.append("[");
		return this;
	}
	
	
	public JW end()
	{
		State st = state.removeLast();
		switch(st.phase)
		{
		case ARRAY:
			sb.append("]");
			break;
		case IDLE:
			throw new IllegalStateException("no object/array to end");
		case NAME:
			throw new IllegalStateException("expecting value");
		case OBJECT:
			sb.append("}");
			break;
		}
		return this;
	}
	
	
	public JW value(Object value)
	{
		State st = state.getLast();
		switch(st.phase)
		{
		case ARRAY:
			separator(st);
			appendValue(value);
			break;
		case IDLE:
			throw new IllegalStateException("object name expected");
		case NAME:
			appendValue(value);
			state.removeLast();
			break;
		case OBJECT:
			throw new IllegalStateException("name expected");
		}
		return this;
	}
	
	
	public JW valueBase64(byte[] bytes)
	{
		if(bytes == null)
		{
			return value(null);
		}
		else
		{
			String s = Base64.getEncoder().encodeToString(bytes);
			return value(s);
		}
	}
	
	
	public JW valueHexString(byte[] bytes, int maxLength)
	{
		if(bytes == null)
		{
			return value(null);
		}
		else
		{
			int len = bytes.length;
			String s;
			if((len > maxLength) && (maxLength > 3))
			{
				int sz = maxLength / 2;
				s = Hex.toHexString(bytes, 0, sz) + ".." + Hex.toHexString(bytes, len - sz, sz);
			}
			else
			{
				s = Hex.toHexString(bytes);
			}
			return value(s);
		}
	}
	
	
	public JW valueHexString(byte[] bytes)
	{
		return valueHexString(bytes, Integer.MAX_VALUE);
	}
	
	
	public JW value(String name, Object value)
	{
		State st = state.getLast();
		switch(st.phase)
		{
		case ARRAY:
			// or maybe just an inline object?
			throw new IllegalStateException("name/value in an array");
		case NAME:
			throw new IllegalStateException("expecting a value");
		case IDLE:
			setPhase(Phase.OBJECT);
			sb.append("{");
			// fall through
		case OBJECT:
			separator(st);
			sb.append("\"");
			appendText(name);
			sb.append("\":");
			appendValue(value);
			break;
		}
		return this;
	}
	
	
	public JW name(String name)
	{
		State st = state.getLast();
		switch(st.phase)
		{
		case ARRAY:
			throw new IllegalStateException("name not allowed in an array");
		case IDLE:
			throw new IllegalStateException("object expected");
		case NAME:
			throw new IllegalStateException("expecting a value");
		case OBJECT:
			separator(st);
			sb.append("\"");
			appendText(name);
			sb.append("\":");
			setPhase(Phase.NAME);
		}
		return this;
	}
	
	
	@Override
	public String toString()
	{
		terminate();
		return sb.toString();
	}
	
	
	private void terminate()
	{
		for(;;)
		{
			State st = state();
			switch(st.phase)
			{
			case ARRAY:
				end();
				break;
			case IDLE:
				return;
			case NAME:
				throw new IllegalStateException("missing value after name");
			case OBJECT:
				end();
				break;
			default:
				throw new IllegalStateException("?" + st);
			}
		}
	}
	
	
	private void setPhase(Phase ph)
	{
		state.add(new State(ph));
	}
	
	
	private void separator(State st)
	{
		if(st.separator)
		{
			sb.append(",");
		}
		else
		{
			st.separator = true;
		}
	}
	
	
	private State state()
	{
		return state.getLast();
	}
	
	
	private void appendText(CharSequence text)
	{
		int len = text.length();
		for(int i=0; i<len; i++)
		{
			char c = text.charAt(i);
			switch(c)
			{
			case '"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '/':
				sb.append("\\/");
				break;
			default:
				if(c < 0x20)
				{
					sb.append("\\u");
					sb.hex4(c);
				}
				else
				{
					sb.append(c);
				}
				break;
			}
		}
	}
	
	
	private void appendValue(Object x)
	{
		if(x == null)
		{
			sb.append("null");
		}
		else if(formatter == null)
		{
			if(x instanceof Number n)
			{
				if(x instanceof Double d)
				{
					long v = (long)d.doubleValue();
					if(v == d)
					{
						sb.append(v);
						return;
					}
				}
				else if(x instanceof Float d)
				{
					int v = (int)d.floatValue();
					if(v == d)
					{
						sb.append(v);
						return;
					}
				}
				sb.append(n.toString());
			}
			else if(x instanceof CharSequence s)
			{
				sb.append("\"");
				appendText(s);
				sb.append("\"");
			}
			else if(x instanceof Map m)
			{
				setPhase(Phase.OBJECT);
				sb.append("{");
				for(Map.Entry<?,?> en: ((Map<?,?>)m).entrySet())
				{
					String k = (String)en.getKey();
					Object v = en.getValue();
					value(k, v);
				}
				sb.append("}");
				state.removeLast();
			}
			else if(x instanceof Collection c)
			{
				setPhase(Phase.ARRAY);
				sb.append("[");
				for(Object v: c)
				{
					value(v);
				}
				sb.append("]");
				state.removeLast();
			}
			else if(x.getClass().isArray())
			{
				setPhase(Phase.ARRAY);
				sb.append("[");
				int sz = Array.getLength(x);
				for(int i=0; i<sz; i++)
				{
					Object v = Array.get(x, i);
					value(v);
				}
				sb.append("]");
				state.removeLast();
			}
			else if(x.getClass().isPrimitive())
			{
				sb.append(x.toString());
			}
			else if(x instanceof Boolean)
			{
				sb.append(x.toString());
			}
			else
			{
				String s = x.toString();
				sb.append("\"");
				appendText(s);
				sb.append("\"");
			}
		}
		else
		{
			if(x instanceof Number n)
			{
				String s = formatter.apply(n);
				sb.append(s);
			}
			else
			{
				String s = formatter.apply(x);
				sb.append("\"");
				appendText(s);
				sb.append("\"");
			}
		}
	}
}
