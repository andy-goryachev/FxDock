// Copyright © 2016-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import goryachev.common.test.TF;
import goryachev.common.test.Test;


/**
 * Test CComparator.
 */
public class TestCComparator
{
	public static void main(String[] args)
	{
		TF.run();
	}
	
	
	protected CComparator<String> c()
	{
		return new CComparator<String>()
		{
			public int compare(String a, String b)
			{
				return compareNatural(a, b);
			}
		};
	}
	
	
	@Test
	public void testNaturalOrder()
	{
		String[] ss =
		{
			"$ yo",
			"% YO",
			"zz100000000000000000000000000000000000000000000000000000000000002 z",
			"zz100000000000000000000000000000000000000000000000000000000000002yy",
			"zz 100000000000000000000000000000000000000000000000000000000000001yy",
			" 1",
			"2",
			"3 a",
			"b",
			"cc",
			"$d"
		};
		
		c().sort(ss);
		
		D.list(ss);
	}
}
