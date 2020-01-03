// Copyright © 2013-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.io;
import java.io.IOException;
import java.io.InputStream;


/** allows to read a chunk from an input stream */
public class LimitingInputStream
	extends InputStream
{
	private final InputStream stream;
	private final long length;
	private long read;
	
	
	public LimitingInputStream(InputStream stream, long length)
	{
		this.stream = stream;
		this.length = length;
	}


	public int read() throws IOException
	{
		if(read < length)
		{
			int c = stream.read();
			read++;
			return c;
		}
		else
		{
			return -1;
		}
	}


	public int read(byte b[], int off, int len) throws IOException
	{
		int rv;
		long toread = length - read;
		if(toread >= len)
		{
			rv = stream.read(b, off, len);
			if(rv >= 0)
			{
				read += rv;
			}
		}
		else
		{
			if(toread > 0)
			{
				rv = stream.read(b, off, (int)toread);
				if(rv >= 0)
				{
					read += rv;
				}
			}
			else
			{
				rv = -1;
			}
		}
		return rv;
	}
}
