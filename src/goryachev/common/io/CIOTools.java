// Copyright © 2013-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.io;
import goryachev.common.util.CKit;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/** 
 * Common I/O tools.
 * 
 * TODO use this class for DWriter and DReader
 */
public class CIOTools
{
	private CIOTools()
	{
	}


	public static void writeByte(OutputStream out, int d) throws IOException
	{
		out.write(d);
	}
	
	
	public static int readByte(InputStream in) throws IOException
	{
		int c = in.read();
		if(c < 0)
		{
			throw new EOFException();
		}
		return c & 0xff;
	}
	
	
	public static void writeByteArray(OutputStream out, byte[] b) throws IOException
	{
		if(b == null)
		{
			writeInt(out, -1);
		}
		else
		{
			writeInt(out, b.length);
			out.write(b);
		}
	}
	
	
	public static byte[] readByteArray(InputStream in) throws IOException
	{
		return readByteArray(in, Integer.MAX_VALUE);
	}
	
	
	public static byte[] readByteArray(InputStream in, int max) throws IOException
	{
		int count = readInt(in);
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
			readFully(in, b);
			return b;
		}
	}
	

	public static void writeInt(OutputStream out, int d) throws IOException
	{
		writeByte(out, d >>> 24);
		writeByte(out, d >>> 16);
		writeByte(out, d >>>  8);
		writeByte(out, d);
	}
	
	
	public static int readInt(InputStream in) throws IOException
	{
		int d = (readByte(in) << 24);
		d |= (readByte(in) << 16);
		d |= (readByte(in) << 8);
		d |= readByte(in);
		return d;
	}
	
	
	public static void writeLong(OutputStream out, long d) throws IOException
	{
		writeByte(out, (int)(d >>> 56));
		writeByte(out, (int)(d >>> 48));
		writeByte(out, (int)(d >>> 40));
		writeByte(out, (int)(d >>> 32));
		writeByte(out, (int)(d >>> 24));
		writeByte(out, (int)(d >>> 16));
		writeByte(out, (int)(d >>>  8));
		writeByte(out, (int)d);
	}
	
	
	public static long readLong(InputStream in) throws IOException
	{
		long d = ((long)readByte(in) << 56);
		d |= ((long)readByte(in) << 48);
		d |= ((long)readByte(in) << 40);
		d |= ((long)readByte(in) << 32);
		d |= ((long)readByte(in) << 24);
		d |= ((long)readByte(in) << 16);
		d |= ((long)readByte(in) << 8);
		d |= readByte(in);
		return d;
	}

	
	public static void readChars(InputStream in, char[] cs) throws IOException
	{
		int sz = cs.length;
		for(int i=0; i<sz; i++)
		{
			int c = readByte(in) << 8;
			c |= readByte(in);
			cs[i] = (char)c;
		}
	}
	
	
	public static void writeString(OutputStream out, String s) throws IOException
	{
		if(s == null)
		{
			writeInt(out, -1);
		}
		else
		{
			byte[] b = s.getBytes(CKit.CHARSET_UTF8);
			int len = b.length;
			
			writeInt(out, len);
			out.write(b);
		}
	}
	
	
	public static String readString(InputStream in) throws IOException
	{
		int len = readInt(in);
		if(len < 0)
		{
			return null;
		}
		else
		{
			byte[] b = new byte[len];
			readFully(in, b);
			return new String(b, CKit.CHARSET_UTF8);
		}
	}
	
	
	public static void readFully(InputStream in, byte[] b) throws IOException
	{
		readFully(in, b, 0, b.length);
	}


	public static void readFully(InputStream in, byte[] b, int off, int len) throws IOException
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
}
