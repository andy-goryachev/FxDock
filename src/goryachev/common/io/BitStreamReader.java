// Copyright © 2013-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.common.io;
import goryachev.common.util.CKit;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;


public class BitStreamReader
	extends BitStreamCommon
	implements Closeable
{
	private InputStream inp;
	private int count;
	private int bits;


	public BitStreamReader(InputStream inp)
	{
		this.inp = inp;
	}


	public void close() throws IOException
	{
		CKit.close(inp);
	}


	public int readBits(int bitCount) throws Exception
	{
		if((bitCount <= 0) || (bitCount > 31))
		{
			throw new IllegalArgumentException("invalid bitCount: " + bitCount);
		}
		
		int rv = 0;
		if(inp == null)
		{
			return -1;
		}

		while(bitCount > count)
		{
			rv |= (bits << (bitCount - count));
			bitCount -= count;
			
			if((bits = inp.read()) == -1)
			{
				return -1;
			}

			count = BITS_PER_BYTE;
		}

		if(bitCount > 0)
		{
			rv |= (bits >> (count - bitCount));
			bits &= MASK[count - bitCount];
			count -= bitCount;
		}
		
		return rv;
	}
}
