// Copyright © 2018-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import goryachev.common.test.TF;
import goryachev.common.test.Test;


/**
 * Test JavaVersion.
 */
public class TestJavaVersion
{
	public static void main(String[] a)
	{
		TF.run();
	}
	
	
//	@Test
	public void test() throws Exception
	{
		// "1.8.0_131-b11"	
		// "9.0.4+11"
		D.p(JavaVersion.getJavaRuntimeVersion());
		D.describe(JavaVersion.getJavaRuntimeVersion().toIntArray());
		
		// "1.8"
		// "9"
		D.p(JavaVersion.getJavaSpecificationVersion());
		D.describe(JavaVersion.getJavaSpecificationVersion().toIntArray());
		
		// "9.0.4"
		// "1.8.0_131"	
		D.p(JavaVersion.getJavaVersion());
		D.describe(JavaVersion.getJavaVersion().toIntArray());
	}
	
	
	@Test
	public void testParse() throws Exception
	{
		tp("1.8.0_131-b11", 1, 8, 0);
		tp("9", 9);
		tp("9.0.4", 9, 0, 4);
	}
	
	
	private void tp(String text, int ... vers)
	{
		int[] parsed = JavaVersion.parse(text).toIntArray();
		TF.eq(parsed, vers);
	}
}
