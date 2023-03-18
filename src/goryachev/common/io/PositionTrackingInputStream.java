// Copyright © 2013-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.common.io;
import java.io.IOException;
import java.io.InputStream;


/** an input stream which keeps track of the current position, useful to wrap around a BufferedInputStream */
public class PositionTrackingInputStream
	extends InputStream
{
	private final InputStream in;
	private long pos;


	public PositionTrackingInputStream(InputStream in)
	{
		this.in = in;
	}
	
	
	public long getPosition()
	{
		return pos;
	}


	public int read() throws IOException
	{
		int r = in.read();
		if(r >= 0)
		{
			pos++;
		}
		return r;
	}


	public int read(byte b[]) throws IOException
	{
		int r = in.read(b);
		if(r > 0)
		{
			pos += r;
		}
		return r;
	}


	public int read(byte b[], int off, int len) throws IOException
	{
		int r = in.read(b, off, len);
		if(r > 0)
		{
			pos += r;
		}
		return r;
	}


	public long skip(long n) throws IOException
	{
		long r = in.skip(n);
		pos += r;
		return r;
	}


	public int available() throws IOException
	{
		return in.available();
	}


	public void close() throws IOException
	{
		in.close();
	}


	public synchronized void mark(int readlimit)
	{
		in.mark(readlimit);
	}


	public synchronized void reset() throws IOException
	{
		in.reset();
	}


	public boolean markSupported()
	{
		return in.markSupported();
	}
}
