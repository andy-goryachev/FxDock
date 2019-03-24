// Copyright © 2011-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.common.io;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;


public class PrimitiveInputStream
	implements PrimitiveInput
{
	private InputStream in;
	
	
	public PrimitiveInputStream(InputStream in) throws Exception
	{
		this.in = in;
		
		if(readRawByte() != Persister.SIGNATURE)
		{
			throw new Exception("not a PrimitiveStream");
		}
	}
	
	
	public PrimitiveInputStream(File f) throws Exception
	{
		this(new BufferedInputStream(new FileInputStream(f)));
	}
	
	
	public PrimitiveInputStream(byte[] b) throws Exception
	{
		this(new ByteArrayInputStream(b));
	}
	
	
	public Object readObject() throws Exception
	{
		return Persister.read(this);
	}
	
	
	public void close() throws IOException
	{
		in.close();
	}
	
	
	protected int streamRead() throws Exception
	{
		return in.read();
	}
	

	public byte readRawByte() throws Exception
	{
		int ch = in.read();
		if(ch < 0)
		{
			throw new EOFException();
		}
		return (byte)(ch);
	}
	

	public int readRawInt() throws Exception
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


	public long readRawLong() throws Exception
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


	public float readRawFloat() throws Exception
	{
		return Float.intBitsToFloat(readRawInt());
	}


	public double readRawDouble() throws Exception
	{
		return Double.longBitsToDouble(readRawLong());
	}


	public void readRawBytes(byte b[]) throws Exception
	{
		readRawBytes(b, 0, b.length);
	}


	public void readRawBytes(byte b[], int off, int len) throws Exception
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
	

	public boolean readRawBoolean() throws Exception
	{
		switch(readRawByte())
		{
		case 0: return Boolean.FALSE;
		case 1: return Boolean.TRUE;
		default: throw new Exception();
		}
	}
	
	
	public char readRawChar() throws IOException
	{
		int ch1 = in.read();
		int ch2 = in.read();
		if(ch2 < 0)
		{
			throw new EOFException();
		}
		return (char)((ch1 << 8) + ch2);
	}


	public short readRawShort() throws IOException
	{
		int ch1 = in.read();
		int ch2 = in.read();
		if(ch2 < 0)
		{
			throw new EOFException();
		}
		return (short)((ch1 << 8) + ch2);
	}
	
	
	public int readInt() throws Exception
	{
		return readInteger();
	}
	
	
	public Integer readInteger() throws Exception
	{
		return (Integer)readObject();
	}
	
	
	public boolean readBool() throws Exception
	{
		return readBoolean();
	}
	
	
	public Boolean readBoolean() throws Exception
	{
		return (Boolean)readObject();
	}
	
	
	public Byte readByte() throws Exception
	{
		return (Byte)readObject();
	}
	
	
	public byte[] readByteArray() throws Exception
	{
		return (byte[])readObject();
	}
	
	
	public Short readShort() throws Exception
	{
		return (Short)readObject();
	}
	
	
	public String readString() throws Exception
	{
		return (String)readObject();
	}
	
	
	public Long readLong() throws Exception
	{
		return (Long)readObject();
	}
	
	
	public Float readFloat() throws Exception
	{
		return (Float)readObject();
	}
	
	
	public Double readDouble() throws Exception
	{
		return (Double)readObject();
	}
	
	
	public BigInteger readBigInteger() throws Exception
	{
		return (BigInteger)readObject();
	}
	
	
	public BigDecimal readBigDecimal() throws Exception
	{
		return (BigDecimal)readObject();
	}
}
