// Copyright © 2011-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.common.io;


public interface StreamingInput<T>
{
	public T readFromStream();
}
