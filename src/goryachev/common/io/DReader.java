// Copyright (c) 2011-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.io;
import goryachev.common.util.CKit;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


/** Conventient binary data reader */
public class DReader
	implements Closeable
{
	protected InputStream in;
	
	
	public DReader(InputStream in)
	{
		this.in = in;
	}
	
	
	public DReader(byte[] b)
	{
		this(new ByteArrayInputStream(b));
	}
	
	
	public DReader(File f) throws Exception
	{
		this(new BufferedInputStream(new FileInputStream(f)));
	}
	

	public void readFully(byte[] b) throws IOException
	{
		readFully(b, 0, b.length);
	}


	public void readFully(byte[] b, int off, int len) throws IOException
	{
		if(len < 0)
		{
			throw new IndexOutOfBoundsException();
		}
		
		int n = 0;
		while(n < len)
		{
			int count = in.read(b, off + n, len - n);
			if(count < 0)
			{
				throw new EOFException();
			}
			n += count;
		}
	}
	
	
	public byte[] readByteArray(int max) throws IOException
	{
		int count = readInt();
		if(count < 0)
		{
			if(count == -1)
			{
				return null;
			}
			else
			{
				throw new IOException("unexpected byte array size " + count);
			}
		}
		else
		{
			if(count >= max)
			{
				throw new IOException("expecting no more than " + max + " bytes, received " + count);
			}
			byte[] b = new byte[count];
			readFully(b);
			return b;
		}
	}
	

	public boolean readBoolean() throws IOException
	{
		int ch = in.read();
		if(ch < 0)
		{
			throw new EOFException();
		}
		return (ch != 0);
	}


	public byte readByte() throws IOException
	{
		int ch = in.read();
		if(ch < 0)
		{
			throw new EOFException();
		}
		return (byte)(ch);
	}
	
	
	public int readByteRaw() throws IOException
	{
		return in.read();
	}


	public char readChar() throws IOException
	{
		int ch1 = in.read();
		int ch2 = in.read();
		if(ch2 < 0)
		{
			throw new EOFException();
		}
		return (char)((ch1 << 8) + ch2);
	}


	public short readShort() throws IOException
	{
		int ch1 = in.read();
		int ch2 = in.read();
		if(ch2 < 0)
		{
			throw new EOFException();
		}
		return (short)((ch1 << 8) + ch2);
	}


	public int readInt24() throws IOException
	{
		int ch1 = in.read();
		int ch2 = in.read();
		int ch3 = in.read();
		if(ch3 < 0)
		{
			throw new EOFException();
		}
		return ((ch1 << 16) + (ch2 << 8) + ch3);
	}


	public int readInt() throws IOException
	{
		return readInt(in);
	}
	
	
	public static int readInt(InputStream in) throws IOException
	{
		int ch1 = in.read();
		int ch2 = in.read();
		int ch3 = in.read();
		int ch4 = in.read();
		if(ch4 < 0)
		{
			throw new EOFException();
		}
		return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + ch4);
	}

	
	public int[] readIntArray(int max) throws IOException
	{
		int count = readInt();
		if(count < 0)
		{
			if(count == -1)
			{
				return null;
			}
			else
			{
				throw new IOException("unexpected int array size " + count);
			}
		}
		else
		{
			if(count >= max)
			{
				throw new IOException("expecting no more than " + max + " bytes, received " + count);
			}
			
			int[] b = new int[count];
			for(int i=0; i<count; i++)
			{
				b[i] = readInt();
			}
			return b;
		}
	}
	

	public long readLong() throws IOException
	{
		long d = ((long)in.read() << 56);
		d |= ((long)in.read() << 48);
		d |= ((long)in.read() << 40);
		d |= ((long)in.read() << 32);
		d |= ((long)in.read() << 24);
		d |= ((long)in.read() << 16);
		d |= ((long)in.read() << 8);
		int c = in.read();
		if(c < 0)
		{
			throw new EOFException();
		}
		d |= c;
		return d;
	}


	public float readFloat() throws IOException
	{
		return Float.intBitsToFloat(readInt());
	}


	public double readDouble() throws IOException
	{
		return Double.longBitsToDouble(readLong());
	}


	public String readString() throws IOException
	{
		int len = readInt();
		if(len < 0)
		{
			return null;
		}
		else
		{
			byte[] b = new byte[len];
			readFully(b);
			return new String(b, CKit.CHARSET_UTF8);
		}
	}
	
	
	public void close() throws IOException
	{
		in.close();
	}
}
