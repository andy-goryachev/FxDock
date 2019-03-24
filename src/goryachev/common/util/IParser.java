// Copyright © 2017-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


/**
 * A Parser interface.
 */
public interface IParser<T>
{
	public T parse(String s) throws Exception;
}
