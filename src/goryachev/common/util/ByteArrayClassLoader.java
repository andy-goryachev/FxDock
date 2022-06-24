// Copyright © 2018-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


/**
 * ClassLoader which loads classes from a byte[].
 */
public class ByteArrayClassLoader
	extends ClassLoader
{
	public Class<?> load(String name, byte[] b)
	{
		return defineClass(name, b, 0, b.length);
	}
}