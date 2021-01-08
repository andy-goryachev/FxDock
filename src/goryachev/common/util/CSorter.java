// Copyright © 2013-2021 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.text.Collator;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/** class that sorts anything */
public class CSorter
{
	/** universal sort by toString() value */
	public static void sort(Object[] a)
	{
		if(a != null)
		{
			Arrays.sort(a, comparator(false));
		}
	}
	
	
	/** universal sort by toString() value */
	public static void sort(List<?> a)
	{
		if(a != null)
		{
			Collections.sort(a, comparator(false));
		}
	}
	
	
	/** universal sort by toString() value in reverse order */
	public static void sortReverse(Object[] a)
	{
		if(a != null)
		{
			Arrays.sort(a, comparator(true));
		}
	}
	
	
	/** universal sort by toString() value in reverse order */
	public static void sortReverse(List<?> a)
	{
		if(a != null)
		{
			Collections.sort(a, comparator(true));
		}
	}
	
	
	/** universal text collation by toString() value */
	public static void collate(Object[] a)
	{
		if(a != null)
		{
			Arrays.sort(a, collator());
		}
	}
	
	
	/** universal text collation by toString() value */
	public static void collate(List<?> a)
	{
		if(a != null)
		{
			Collections.sort(a, collator());
		}
	}
	
	
	public static int compare(int a, int b)
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
	
	
	public static int compare(long a, long b)
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
	
	
	public static int compare(float a, float b)
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
	
	
	public static int compare(double a, double b)
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
	
	
	public static Comparator<Object> comparator()
	{
		return comparator(false);
	}
	
	
	public static Comparator<Object> comparator(final boolean reverse)
	{
		return new Comparator<Object>()
		{
			public int compare(Object a, Object b)
			{
				int rv = smartCompare(a,b);
				return reverse ? -rv : rv;
			}
		};
	}
	
	
	protected static String toStringValue(Object x)
	{
		if(x == null)
		{
			return null;
		}
		else
		{
			return x.toString();
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public static int smartCompare(Object a, Object b)
	{
		if(a instanceof Comparable)
		{
			if(b instanceof Comparable)
			{
				// both are non-null
				if(a.getClass().isAssignableFrom(b.getClass()))
				{
					return ((Comparable)a).compareTo(b);
				}
				else if(b.getClass().isAssignableFrom(a.getClass()))
				{
					return ((Comparable)b).compareTo(a);
				}
				// incompatible Comparable
			}
		}
		
		String sa = toStringValue(a);
		String sb = toStringValue(b);
		
		if(sa == null)
		{
			if(sb == null)
			{
				return 0;
			}
			else
			{
				return -1;
			}
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
	
	
	protected static Comparator<Object> collator()
	{
		return new Comparator<Object>()
		{
			private Collator collator = Collator.getInstance();
			
			
			public int compare(Object a, Object b)
			{
				String sa = toStringValue(a);
				String sb = toStringValue(b);
				
				if(sa == null)
				{
					if(sb == null)
					{
						return 0;
					}
					else
					{
						return -1;
					}
				}
				else if(sb == null)
				{
					return 1;
				}
				else
				{
					return collator.compare(sa, sb);
				}
			}
		};
	}
}
