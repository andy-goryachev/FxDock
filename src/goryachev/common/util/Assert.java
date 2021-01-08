// Copyright © 2013-2021 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.io.File;
import java.io.FileNotFoundException;


/** Assertions */
public class Assert
{
	public static <T> T notNull(T x)
	{
		if(x == null)
		{
			throw new IllegalArgumentException("must not be null");
		}
		return x;
	}
	
	
	public static <T> T notNull(T x, String name)
	{
		if(x == null)
		{
			throw new IllegalArgumentException(name + " must not be null");
		}
		return x;
	}


	public static void greaterThanZero(int x, String name)
	{
		if(x <= 0)
		{
			throw new IllegalArgumentException(name + " must be greater than 0 (" + x + ")");
		}
	}
	
	
	public static String notBlank(String x)
	{
		if(CKit.isBlank(x))
		{
			throw new IllegalArgumentException("must not be blank");
		}
		return x;
	}
	
	
	public static String notBlank(String x, String name)
	{
		if(CKit.isBlank(x))
		{
			throw new IllegalArgumentException(name + " must not be blank");
		}
		return x;
	}


	public static void assertTrue(boolean value)
	{
		if(!value)
		{
			throw new IllegalArgumentException("must be true");
		}
	}
	
	
	public static void assertFalse(boolean value)
	{
		if(value)
		{
			throw new IllegalArgumentException("must be false");
		}
	}


	public static void assertEquals(Object a, Object b)
	{
		if(CKit.notEquals(a, b))
		{
			throw new IllegalArgumentException("must be equal: " + a + " != " + b);
		}
	}
	
	
	public static void assertEndsWith(String text, String suffix)
	{
		if(text != null)
		{
			if(suffix != null)
			{
				if(text.endsWith(suffix))
				{
					return;
				}
			}
		}
		
		throw new IllegalArgumentException("[" + text + "] must end with [" + suffix + "]");
	}


	public static <T extends Comparable<T>> void isLessThanOrEqual(T min, T max, String nameMin, String nameMax)
	{
		if(min.compareTo(max) > 0)
		{
			throw new IllegalArgumentException(nameMin + " must be less than or equal to " + nameMax);
		}
	}
	
	
	public static void folderExists(File f) throws Exception
	{
		if(!FileTools.isFolderExist(f))
		{
			throw new FileNotFoundException("Folder does not exist: " + f);
		}
	}
	
	
	public static void fileExists(File f) throws Exception
	{
		if(!FileTools.isFileExist(f))
		{
			throw new FileNotFoundException("File does not exist: " + f);
		}
	}
}
