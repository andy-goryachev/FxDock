// Copyright © 2013-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.io;
import goryachev.common.util.CKit;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;


public class BitStreamWriter
	extends BitStreamCommon
	implements Closeable, Flushable
{
	protected OutputStream out;
	private int bits;
	private int count = BITS_PER_BYTE;


	public BitStreamWriter(OutputStream out)
	{
		this.out = out;
	}
	
	
	protected BitStreamWriter()
	{
	}
	
	
	protected void setOutputStream(OutputStream out)
	{
		this.out = out;
	}
	
	
	public void write(int bitCount, int value) throws Exception
	{
		if((bitCount <= 0) || (bitCount > 31))
		{
			throw new IllegalArgumentException("invalid bitCount: " + bitCount);
		}

		value &= MASK[bitCount];

		while(bitCount >= count)
		{
			bits = (bits << count) | (value >> (bitCount - count));
			
			out.write(bits);

			value &= MASK[bitCount - count];
			bitCount -= count;
			count = BITS_PER_BYTE;
			bits = 0;
		}

		if(bitCount > 0)
		{
			bits = ((bits << bitCount) | value);
			count -= bitCount;
		}
	}


	public void flush() throws IOException
	{
		if(count != BITS_PER_BYTE)
		{
			out.write((bits << count));
			bits = 0;
			count = BITS_PER_BYTE;
		}

		out.flush();
	}


	public void close() throws IOException
	{
		try
		{
			flush();
		}
		finally
		{
			CKit.close(out);
		}
	}
}
