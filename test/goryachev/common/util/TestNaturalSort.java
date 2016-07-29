// Copyright © 2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import goryachev.common.test.TF;
import goryachev.common.test.Test;


/**
 * Test Natural Sort.
 * 
 * https://en.wikipedia.org/wiki/Natural_sort_order
 */
public class TestNaturalSort
{
	public static void main(String[] args)
	{
		TF.run();
	}
	
	
	// https://blog.codinghorror.com/sorting-for-humans-natural-sort-order/
	@Test
	public void testCodingHorror()
	{
		ts(new String[]
		{
	        "z1.doc",
	        "z2.doc",
	        "z3.doc",
	        "z4.doc",
	        "z5.doc",
	        "z6.doc",
	        "z7.doc",
	        "z8.doc",
	        "z9.doc",
	        "z10.doc",
	        "z11.doc",
	        "z12.doc",
	        "z13.doc",
	        "z14.doc",
	        "z15.doc",
	        "z16.doc",
	        "z17.doc",
	        "z18.doc",
	        "z19.doc",
	        "z20.doc",
	        "z100.doc",
	        "z101.doc",
	        "z102.doc",
		});
	}
	
	
	@Test
	public void testSingle()
	{
//		t("51 Clasteron", "51B Clasteron", -1);
	}
	
	
	// http://www.davekoelle.com/alphanum.html
	@Test
	public void testAlphaNum()
	{
		ts(new String[]
		{
	        "10X Radonius",
	        "20X Radonius",
	        "20X Radonius Prime",
	        "30X Radonius",
	        "40X Radonius",
	        "200X Radonius",
//	        "10.4X Radonius", // looks like it's a US decimal point
//	        "20.5X Radonius",
//	        "20.2X Radonius Prime",
//	        "30.6X Radonius",
//	        "40.6X Radonius",
	        "1000X Radonius Maximus",
	        "Allegia 50 Clasteron",
	        "Allegia 51 Clasteron",
	        "Allegia 51B Clasteron",
	        "Allegia 52 Clasteron",
	        "Allegia 60 Clasteron",
	        "Allegia 500 Clasteron",
	        "Alpha 2",
	        "Alpha 2A",
	        "Alpha 2A-900",
	        "Alpha 2A-8000",
	        "Alpha 100",
	        "Alpha 200",
	        "Callisto Morphamax",
	        "Callisto Morphamax 500",
	        "Callisto Morphamax 600",
	        "Callisto Morphamax 700",
	        "Callisto Morphamax 5000",
	        "Callisto Morphamax 7000",
	        "Callisto Morphamax 7000 SE",
	        "Callisto Morphamax 7000 SE2",
//	        "Callisto Morphamax 500.8",
//	        "Callisto Morphamax 600.3",
//	        "Callisto Morphamax 700.5",
//	        "Callisto Morphamax 5000.56",
//	        "Callisto Morphamax 7000.8",
	        "QRS-60 Intrinsia Machine",
	        "QRS-60F Intrinsia Machine",
	        "QRS-62 Intrinsia Machine",
	        "QRS-62F Intrinsia Machine",
	        "Xiph Xlater 5",
	        "Xiph Xlater 40",
	        "Xiph Xlater 50",
	        "Xiph Xlater 58",
	        "Xiph Xlater 300",
	        "Xiph Xlater 500",
	        "Xiph Xlater 2000",
	        "Xiph Xlater 5000",
	        "Xiph Xlater 10000"
		});
	}
	
	
	@Test
	public void testFin()
	{
		ts(new String[]
		{
	        "$ Change",
	        "$ change",
	        " % Change",
	        "% Change",
	        "%Change",
		});
	}


	protected static int sign(int x)
	{
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
	
	
	public void t(String a, String b, int expected)
	{
		int v = NaturalSort.compare(a, b);
		if(sign(v) != sign(expected))
		{
			throw new Error("[" + a + "], [" + b + "] = " + sign(v) + ", expected:" + sign(expected));
		}
	}
	
	
	/** tests all possible combinations, assuming the original array is sorted to natural order */
	public void ts(String[] ss)
	{
		int sz = ss.length;
		for(int ia=0; ia<sz; ia++)
		{
			String a = ss[ia];
			
			int v = NaturalSort.compare(a, a);
			TF.eq(0, v);
			
			for(int ib=ia; ib<sz; ib++)
			{
				String b = ss[ib];
				t(a, b, ia-ib);
			}
		}
	}
}
