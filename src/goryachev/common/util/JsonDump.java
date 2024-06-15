// Copyright © 2020-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;


/**
 * Json Dump.
 */
public class JsonDump
{
	private static final String CIRCULAR_REFERENCE = "\u21ba";
	private final CSet<Object> all = new CSet();
	private final SB sb;
	private final String indent;
	private final boolean prettyPrint;
	private final Object value;
	private static final Comparator<Item> COMPARATOR = createComparator();
	
	
	public JsonDump(SB sb, String indent, boolean prettyPrint, Object v)
	{
		this.sb = sb;
		this.indent = indent;
		this.prettyPrint = prettyPrint;
		this.value = v;
	}
	
	
	public static String print(Object x)
	{
		SB sb = new SB();
		new JsonDump(sb, null, false, x).print();
		return sb.toString();
	}
	
	
	public static String prettyPrint(Object x)
	{
		SB sb = new SB();
		new JsonDump(sb, "  ", true, x).print();
		return sb.toString();
	}
	
	
	private static Comparator<Item> createComparator()
	{
		return new CComparator<Item>()
		{
			@Override
			public int compare(Item a, Item b)
			{
				return collate(a.getName(), b.getName());
			}
		};
	}


	public void print()
	{
		print(0, value);
	}
	
	
	protected boolean checkDup(Object x)
	{
		if(all.contains(x))
		{
			sb.append(CIRCULAR_REFERENCE);
			return true;
		}
		all.add(x); // TODO only for objects, or Object[]
		return false;
	}

	
	protected void print(int level, Object x)
	{
		CKit.checkCancelled();
		
		if(x == null)
		{
			sb.append("null");
		}
		else
		{
			Class c = x.getClass();
			if(c == String.class)
			{
				String s = toJsonString(x);
				sb.append(s);
			}
			else if(c == byte[].class)
			{
				printByteArray((byte[])x);
			}
			else if(isPrimitive(c))
			{
				sb.append(x.toString());
			}
			else if(c.isArray())
			{
				if(checkDup(x))
				{
					return;
				}
				printArray(level, x);
			}
			else if(x instanceof Collection)
			{
				if(checkDup(x))
				{
					return;
				}
				printCollection(level, (Collection)x);
			}
			else if(x instanceof Map)
			{
				if(checkDup(x))
				{
					return;
				}
				printMap(level, (Map)x);
			}
			else if(isForbidden(c))
			{
				// TODO maybe classname(toString)
				String s = c.getName();
				sb.append(s);
			}
			else
			{
				if(checkDup(x))
				{
					return;
				}
				printObject(level, x);
			}
		}
	}
	
	
	protected static boolean isForbidden(Class c)
	{
		String name = c.getName();
		if(name.startsWith("java."))
		{
			return true;
		}
		else if(name.startsWith("javax."))
		{
			return true;
		}
		else if(name.startsWith("java.awt."))
		{
			return true;
		}
		else if(name.startsWith("javax.swing"))
		{
			return true;
		}
		else if(name.startsWith("javafx."))
		{
			return true;
		}
		return false;
	}
	
	
	protected static boolean isPrimitive(Class c)
	{
		if(c.isPrimitive())
		{
			return true;
		}
		
		return
			(c == boolean.class) ||
			(c == Boolean.class) ||
			(c == byte.class) ||
			(c == Byte.class) ||
			(c == char.class) ||
			(c == Character.class) ||
			(c == short.class) ||
			(c == Short.class) ||
			(c == int.class) ||
			(c == Integer.class) ||
			(c == long.class) ||
			(c == Long.class) ||
			(c == float.class) ||
			(c == Float.class) ||
			(c == double.class) ||
			(c == Double.class);
	}
	
	
	public static String toJsonString(Object x)
	{
		String text = x.toString();
		int len = text.length();
		for(int i=0; i<len; i++)
		{
			char c = text.charAt(i);
			switch(c)
			{
			case ' ':
			case '"':
			case ':':
			case ',':
			case '&':
			case '\'':
			case '<':
			case '=':
			case '>':
			case '\\':
				return escapeString(text, i);
			default:
				if(c < 0x20)
				{
					return escapeString(text, i);
				}
				break;
			}
		}
		
		return text;
	}
	
	
	protected static String escapeString(String text, int start)
	{
		int len = text.length();
		SB sb = new SB(len + len);
		sb.append('"');
		sb.append(text, 0, start);
		
		for(int i=start; i<len; i++)
		{
			char c = text.charAt(i);
			switch(c)
			{
			case '\r':
				sb.append("\\r");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '"':
				sb.append("\\\"");
				break;
			case '&':
				sb.append("\\u0026");
				break;
			case '\'':
				sb.append("\\u0027");
				break;
			case '<':
				sb.append("\\u003c");
				break;
			case '=':
				sb.append("\\u003d");
				break;
			case '>':
				sb.append("\\u003e");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			default:
				if(c < 0x20)
				{
					sb.append(String.format("\\u%04x", (int)c));
				}
				else
				{
					sb.append(c);
				}
				break;
			}
		}
		
		sb.append('"');
		return sb.toString();
	}

	
	protected void printArray(int level, Object x)
	{
		sb.a("[");
		
		int len = Array.getLength(x);
		for(int i=0; i<len; i++)
		{
			if(i > 0)
			{
				sb.append(",");
			}
			
			Object v = Array.get(x, i);
			print(level + 1, v);
		}
		
		sb.a("]");
	}
	

	protected void printCollection(int level, Collection coll)
	{
		sb.a("[");
		
		boolean sep = false;
		for(Object v: coll)
		{
			if(sep)
			{
				sb.append(",");
			}
			else
			{
				sep = true;
			}
			
			print(level + 1, v);
		}
		
		sb.a("]");
	}
	
	
	protected void printMap(int level, Map x)
	{
		CList<Item> items = new CList();
		for(Object k: x.keySet())
		{
			items.add(new Item(k, (k == null ? null : k.toString()))
			{
				@Override
				public String getFullyQualifiedName()
				{
					if(k == null)
					{
						return "null";
					}
					return k + "(" + k.getClass() + ")";
				}
				

				@Override
				public Object getValue()
				{
					return x.get(k);
				}
			});
		}
		
		printFields(level, items);
	}
	
	
	protected void printObject(int level, Object x)
	{
		// collect non-static fields
		CList<Field> fields = new CList();
		Class c = x.getClass();
		while(c != Object.class)
		{
			Field[] fs = c.getDeclaredFields();
			for(Field f: fs)
			{
				int m = f.getModifiers();
				if(!Modifier.isStatic(m))
				{
					f.setAccessible(true);
					fields.add(f);
				}
			}
			
			c = c.getSuperclass();
		}
		
		CList<Item> items = new CList(fields.size());
		for(Field f: fields)
		{
			items.add(new Item(f, f.getName())
			{
				@Override
				public String getFullyQualifiedName()
				{
					return f.toString();
				}
				
				
				@Override
				public Object getValue()
				{
					try
					{
						return f.get(x);
					}
					catch(Throwable e)
					{
						return e.getClass().getSimpleName();
					}
				}
			});
		}
		
		printFields(level, items);
	}
	
	
	protected void printFields(int level, CList<Item> items)
	{
		// deduplicate names
		CList<Item> duplicates = null;
		CMap<String,Item> names = new CMap(items.size());
		for(Item item: items)
		{
			String name = item.getName();
			Item old = names.get(name);
			if(old != null)
			{
				if(duplicates == null)
				{
					duplicates = new CList();
				}
				duplicates.add(old);
				duplicates.add(item);
			}
			else
			{
				names.put(name, item);
			}
		}
		
		if(duplicates != null)
		{
			for(Item item: duplicates)
			{
				String s = item.getFullyQualifiedName();
				item.setName(s);
			}
		}
		
		Collections.sort(items, COMPARATOR);
		
		sb.a("{");
		
		boolean sep = false;
		for(Item k: items)
		{
			if(sep)
			{
				sb.append(",");
			}
			else
			{
				sep = true;
			}
			
			String s = toJsonString(k.getName());
			sb.append(s).append(":");
			
			Object v = k.getValue();
			print(level + 1, v);
		}
		
		sb.a("}");
	}
	
	
	protected void printByteArray(byte[] bytes)
	{
		if(bytes.length < 80)
		{
			for(int i=0; i<bytes.length; i++)
			{
				byte b = bytes[i];
				sb.append(Hex.toHexChar(b >> 4));
				sb.append(Hex.toHexChar(b));
			}
		}
		else
		{
			for(int i=0; i<40; i++)
			{
				byte b = bytes[i];
				sb.append(Hex.toHexChar(b >> 4));
				sb.append(Hex.toHexChar(b));
			}
			
			sb.append("…");
			
			for(int i=0; i<40; i++)
			{
				byte b = bytes[bytes.length - 40 + i];
				sb.append(Hex.toHexChar(b >> 4));
				sb.append(Hex.toHexChar(b));
			}
		}
	}
	
	
	//
	
	
	protected abstract static class Item
	{
		public abstract String getFullyQualifiedName();
		
		public abstract Object getValue();
		
		//
		
		public final Object key;
		private String name;
		
		
		public Item(Object key, String name)
		{
			this.key = key;
			this.name = name;
		}


		public String getName()
		{
			return (name == null) ? key.toString() : name;
		}
		
		
		public void setName(String s)
		{
			name = s;
		}
	}
}
