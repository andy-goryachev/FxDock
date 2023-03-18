// Copyright © 2011-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.common.io;


public interface StreamingInput<T>
{
	public T readFromStream();
}
