// Copyright © 2013-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import goryachev.common.test.TF;
import goryachev.common.test.Test;
import goryachev.common.util.CKit;


public class TestCKit
{
	public static final String A = "a";
	public static final String ABBR = "abbr";
	public static final String ACRONYM = "acronym";
	public static final String ADDRESS = "address";
	public static final String APPLET = "applet";
	public static final String AREA = "area";
	public static final String B = "b";
	public static final String BASE = "base";
	public static final String BASEFONT = "basefont";

	
	public static void main(String[] a)
	{
		TF.run();
	}
	
	
	@Test
	public void testSplit() throws Exception
	{
		split("a", ",", "a");
		split("a,", ",", "a", "");
		split(",a", ",", "", "a");
		split(",", ",", "", "");
		split("a,b,c", ",", "a", "b", "c");
		
		split("a", "DEL", "a");
		split("aDEL", "DEL", "a", "");
		split("DELa", "DEL", "", "a");
		split("aDELbDELc", "DEL", "a", "b", "c");
	}
	
	
	private static void split(String text, String delim, String ... expected)
	{
		String[] res = CKit.split(text, delim);
		TF.eq(res, expected);
	}


	@Test
	public void testMod()
	{
		TF.eq(CKit.mod(-10, 4), 2);
		TF.eq(CKit.mod( -9, 4), 3);
		TF.eq(CKit.mod( -8, 4), 0);
		TF.eq(CKit.mod( -7, 4), 1);
		TF.eq(CKit.mod( -6, 4), 2);
		TF.eq(CKit.mod( -5, 4), 3);
		TF.eq(CKit.mod( -4, 4), 0);
		TF.eq(CKit.mod( -3, 4), 1);
		TF.eq(CKit.mod( -2, 4), 2);
		TF.eq(CKit.mod( -1, 4), 3);
		TF.eq(CKit.mod(  0, 4), 0);
		TF.eq(CKit.mod(  1, 4), 1);
		TF.eq(CKit.mod(  2, 4), 2);
		TF.eq(CKit.mod(  3, 4), 3);
		TF.eq(CKit.mod(  4, 4), 0);
		TF.eq(CKit.mod(  5, 4), 1);
		TF.eq(CKit.mod(  6, 4), 2);
		TF.eq(CKit.mod(  7, 4), 3);
		TF.eq(CKit.mod(  8, 4), 0);
		TF.eq(CKit.mod(  9, 4), 1);
		TF.eq(CKit.mod( 10, 4), 2);
	}
	
	
	@Test
	public void testCollectPublicStatic()
	{
		TF.eq(CKit.collectPublicStaticFields(TestCKit.class, String.class).size(), TestCKit.class.getFields().length);
	}
}
