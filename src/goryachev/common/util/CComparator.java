// Copyright © 2009-2016 Andy Goryachev <andy@goryachev.com>
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
	
	private static Collator COLLATOR = Collator.getInstance();
	
	
	protected int compareText(Object a, Object b)
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
	
	
	/** uses letters and numbers only */
	public static int compareNatural(Object a, Object b)
	{
		String sa = toString(a);
		String sb = toString(b);
		return NaturalSort.compare(sa, sb);
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
	
	
	public static int compareLong(long a, long b)
	{
		if(a < b)
		{
			return -1;
		}
		else if(a > b)
		{
			return 1;
		}
		else
		{
			return 0;
		}
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
			long x = a - b;
			if(x < 0)
			{
				return -1;
			}
			else if(x > 0)
			{
				return 1;
			}
			else
			{
				return 0;
			}
		}
	}
	
	
	public static int compareDouble(double a, double b)
	{
		if(a < b)
		{
			return -1;
		}
		else if(a > b)
		{
			return 1;
		}
		else
		{
			return 0;
		}
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
			double x = a - b;
			if(x < 0)
			{
				return -1;
			}
			else if(x > 0)
			{
				return 1;
			}
			else
			{
				return 0;
			}
		}
	}
	
	
	public static int compareFloat(float a, float b)
	{
		if(a < b)
		{
			return -1;
		}
		else if(a > b)
		{
			return 1;
		}
		else
		{
			return 0;
		}
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
			float x = a - b;
			if(x < 0)
			{
				return -1;
			}
			else if(x > 0)
			{
				return 1;
			}
			else
			{
				return 0;
			}
		}
	}
	
	
	protected Collator collator()
	{
		return COLLATOR;
	}
	
	
	public static String toString(Object x)
	{
		return x == null ? null : x.toString();
	}
	
	
	public void sort(List<? extends T> items)
	{
		if(items != null)
		{
			Collections.sort(items, this);
		}
	}
	
	
	public void sort(T[] items)
	{
		if(items != null)
		{
			Arrays.sort(items, this);
		}
	}
	
	
	public static void sortStrings(List<String> items)
	{
		if(items != null)
		{
			Collections.sort(items, COLLATOR);
		}
	}
}
