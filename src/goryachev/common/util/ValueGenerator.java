// Copyright © 2017-2025 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


/**
 * Value Generator.
 */
@FunctionalInterface
public interface ValueGenerator<T>
{
	public T generate() throws Throwable;
}
