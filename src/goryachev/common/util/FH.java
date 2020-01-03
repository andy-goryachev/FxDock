// Copyright © 2016-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.util.Arrays;


/**
 * Fast Hashing to be used in hashCode().
 * 
 * This code is based on standard java polynomial hash value computation algorithm.
 */
public class FH
{
	public static int hash(Class c)
	{
		return c.hashCode();
	}
	
	
	public static int hash(int h, Object val)
	{
		if(val == null)
		{
			return h;
		}
		
		return 31 * h + val.hashCode();
	}
	
	
	public static int hash(Object a, Object b)
	{
		int h;
		h = hash(0, a);
		h = hash(h, b);
		return h;
	}
	
	
	public static int hash(Object a, Object b, Object c)
	{
		int h;
		h = hash(0, a);
		h = hash(h, b);
		h = hash(h, c);
		return h;
	}
	
	
	public static int hash(Object[] a)
	{
		return Arrays.hashCode(a);
	}
	
	
	public static int hash(int h, Object[] a)
	{
		return 31 * h + Arrays.hashCode(a);
	}

	
	public static int hash(int h, boolean val)
	{
		return 31 * h + (val ? 1231 : 1237);
	}
	
	
	public static int hash(int h, byte val)
	{
		return 31 * h + val;
	}
	
	
	public static int hash(int h, char val)
	{
		return 31 * h + val;
	}
	
	
	public static int hash(int h, short val)
	{
		return 31 * h + val;
	}
	
	
	public static int hash(int h, int val)
	{
		return 31 * h + val;
	}
	
	
	public static int hash(int h, float val)
	{
		return 31 * h + Float.floatToIntBits(val);
	}
	
	
	public static int hash(int h, long val)
	{
		return 31 * h + (int)(val ^ (val >>> 32));
	}
	
	
	public static int hash(int h, double val)
	{
		long v = Double.doubleToLongBits(val);
		return 31 * h + (int)(v ^ (v >>> 32));
	}
	
	
	public static int hash(int h, boolean[] val)
	{
		return 31 * h + Arrays.hashCode(val);
	}
	
	
	public static int hash(int h, byte[] val)
	{
		return 31 * h + Arrays.hashCode(val);
	}
	
	
	public static int hash(int h, char[] val)
	{
		return 31 * h + Arrays.hashCode(val);
	}
	
	
	public static int hash(int h, short[] val)
	{
		return 31 * h + Arrays.hashCode(val);
	}
	
	
	public static int hash(int h, int[] val)
	{
		return 31 * h + Arrays.hashCode(val);
	}
	
	
	public static int hash(int h, float[] val)
	{
		return 31 * h + Arrays.hashCode(val);
	}
	
	
	public static int hash(int h, long[] val)
	{
		return 31 * h + Arrays.hashCode(val);
	}
	
	
	public static int hash(int h, double[] val)
	{
		return 31 * h + Arrays.hashCode(val);
	}
}
