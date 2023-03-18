// Copyright © 2011-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.common.io;


public interface StreamingOutput<T>
{
	public void writeToStream(T item);
}
