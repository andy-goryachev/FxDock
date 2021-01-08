// Copyright © 2012-2021 Andy Goryachev <andy@goryachev.com>
package goryachev.common.io;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class PTools
{
	private PTools()
	{
	}
	
	
	/** deepEquals based on the subset of possible types supported by goryachev.io primitive streams */
	public static boolean deepEquals(Object a, Object b)
	{
		if(a == null)
		{
			return (b == null);
		}
		else if(b == null)
		{
			return false;
		}
		
		Class ca = a.getClass();
		Class cb = b.getClass();
		if(ca.isArray() && cb.isArray())
		{
			Class ta = ca.getComponentType();
			Class tb = cb.getComponentType();
			
			if(ta.isPrimitive() || tb.isPrimitive())
			{
				if(ta.equals(tb))
				{
					if(ta == byte.class)
					{
						return Arrays.equals((byte[])a, (byte[])b);
					}
					else if(ta == char.class)
					{
						return Arrays.equals((char[])a, (char[])b);
					}
					else if(ta == short.class)
					{
						return Arrays.equals((short[])a, (short[])b);
					}
					else if(ta == short.class)
					{
						return Arrays.equals((short[])a, (short[])b);
					}
					else if(ta == int.class)
					{
						return Arrays.equals((int[])a, (int[])b);
					}
					else if(ta == long.class)
					{
						return Arrays.equals((long[])a, (long[])b);
					}
					else if(ta == float.class)
					{
						return Arrays.equals((float[])a, (float[])b);
					}
					else if(ta == double.class)
					{
						return Arrays.equals((double[])a, (double[])b);
					}

					else
					{
						return false;
					}
				}
				else
				{
					return false;
				}
			}
			else
			{
				return deepEqualsArray((Object[])a, (Object[])b);
			}
		}
		else if(ca == cb)
		{
			if(List.class.isAssignableFrom(ca))
			{
				return deepEqualsList((List)a, (List)b);
			}
			else if(Map.class.isAssignableFrom(ca))
			{
				return deepEqualsMap((Map)a, (Map)b);
			}
			else
			{
				return a.equals(b);
			}
		}
		else
		{
			return false;
		}
	}
	

	public static boolean deepEqualsArray(Object[] a, Object[] b)
	{
		int len = a.length;
		if(b.length != len)
		{
			return false;
		}

		for(int i=0; i<len; i++)
		{
			Object xa = a[i];
			Object xb = b[i];
			
			if(!deepEquals(xa, xb))
			{
				return false;
			}
		}

		return true;
	}
	
	
	public static boolean deepEqualsList(List a, List b)
	{
		int sz = a.size();
		if(b.size() != sz)
		{
			return false;
		}

		for(int i=0; i<sz; i++)
		{
			Object xa = a.get(i);
			Object xb = b.get(i);
			
			if(!deepEquals(xa, xb))
			{
				return false;
			}
		}

		return true;
	}
	
	
	public static boolean deepEqualsMap(Map a, Map b)
	{
		int sz = a.size();
		if(b.size() != sz)
		{
			return false;
		}

		for(Object k: a.keySet())
		{
			Object xa = a.get(k);
			Object xb = b.get(k);
			
			if(!deepEquals(xa, xb))
			{
				return false;
			}
		}

		return true;
	}


	/** 
	 * Deep cloning of a persistent object, achieved by streaming the said object to a byte array and
	 * subsequent deserialization.
	 */
	public static Object deepClone(Object x) throws Exception
	{
		ByteArrayOutputStream ba = new ByteArrayOutputStream();
		PrimitiveOutputStream out = new PrimitiveOutputStream(ba);
		Persister.store(x, out);
		out.close();
		
		byte[] b = ba.toByteArray();
		ba = null;
		
		PrimitiveInputStream in = new PrimitiveInputStream(b);
		return Persister.read(in);
	}
}
