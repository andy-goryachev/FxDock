// Copyright © 2009-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.text.Collator;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public abstract class CComparator<T>
	implements Comparator<T>
{
	public abstract int compare(T a, T b);

	//
	
	private Collator collator;
	
	
	public CComparator()
	{
	}
	
	
	public CComparator(Collator c)
	{
		this.collator = c;
	}
	
	
	/** use collate() */
	@Deprecated
	protected int compareText(Object a, Object b)
	{
		return collate(a, b);
	}
	
	
	protected int collate(Object a, Object b)
	{
		String sa = toString(a);
		String sb = toString(b);

		if(sa == null)
		{
			return sb == null ? 0 : -1;
		}
		else if(sb == null)
		{
			return 1;
		}
		else
		{
			return collator().compare(sa, sb);
		}
	}
	
	
	/** compares string representation of objects using "natural" order https://en.wikipedia.org/wiki/Natural_sort_order */
	public static int compareNatural(Object a, Object b)
	{
		String sa = toString(a);
		String sb = toString(b);
		return NaturalSort.compare(sa, sb);
	}
	
	
	/** compares strings using "natural" order https://en.wikipedia.org/wiki/Natural_sort_order */
	public static int compareNatural(String a, String b)
	{
		return NaturalSort.compare(a, b);
	}
	
	
	public static int compareAsStrings(Object a, Object b)
	{
		String sa = toString(a);
		String sb = toString(b);

		if(sa == null)
		{
			return sb == null ? 0 : -1;
		}
		else if(sb == null)
		{
			return 1;
		}
		else
		{
			return sa.compareTo(sb);
		}
	}
	
	
	public static int compareInt(int a, int b)
	{
		return Integer.compare(a, b);
	}
	
	
	public static int compareLong(long a, long b)
	{
		return Long.compare(a, b);
	}
	
	
	public static int compareLong(Long a, Long b)
	{
		if(a == null)
		{
			if(b == null)
			{
				return 0;
			}
			else
			{
				return -1;
			}
		}
		else if(b == null)
		{
			return 1;
		}
		else
		{
			return Long.compare(a.longValue(), b.longValue());
		}
	}
	
	
	public static int compareDouble(double a, double b)
	{
		return Double.compare(a, b);
	}
	
	
	public static int compareDouble(Double a, Double b)
	{
		if(a == null)
		{
			if(b == null)
			{
				return 0;
			}
			else
			{
				return -1;
			}
		}
		else if(b == null)
		{
			return 1;
		}
		else
		{
			return Double.compare(a.doubleValue(), b.doubleValue());
		}
	}
	
	
	public static int compareFloat(float a, float b)
	{
		return Float.compare(a, b);
	}
	
	
	public static int compareFloat(Float a, Float b)
	{
		if(a == null)
		{
			if(b == null)
			{
				return 0;
			}
			else
			{
				return -1;
			}
		}
		else if(b == null)
		{
			return 1;
		}
		else
		{
			return Float.compare(a.floatValue(), b.floatValue());
		}
	}
	
	
	protected Collator collator()
	{
		if(collator == null)
		{
			collator = Collator.getInstance();
		}
		return collator;
	}
	
	
	public static String toString(Object x)
	{
		return x == null ? null : x.toString();
	}
	
	
	public List<? extends T> sort(List<? extends T> items)
	{
		if(items != null)
		{
			Collections.sort(items, this);
		}
		return items;
	}
	
	
	public T[] sort(T[] items)
	{
		if(items != null)
		{
			Arrays.sort(items, this);
		}
		return items;
	}
	
	
	public static List<String> sortStrings(List<String> items)
	{
		if(items != null)
		{
			Collections.sort(items, Collator.getInstance());
		}
		return items;
	}
}
