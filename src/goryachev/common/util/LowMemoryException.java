// Copyright © 2017-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


/**
 * RuntimeException thrown when CKit.isLowMemory() detects a low memory condition.
 */
public class LowMemoryException
	extends RuntimeException
{
	public LowMemoryException()
	{
	}
}
