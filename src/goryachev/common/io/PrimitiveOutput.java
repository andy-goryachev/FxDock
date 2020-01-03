// Copyright © 2011-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.io;
import java.io.Closeable;
import java.io.IOException;


public interface PrimitiveOutput
	extends Closeable
{
	public void write(Object x) throws Exception;
	
	public void close() throws IOException;
}
