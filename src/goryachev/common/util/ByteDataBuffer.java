// Copyright © 2009-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.io.EOFException;
import java.io.InputStream;
import java.util.Arrays;


// unsynchronized growing byte buffer
public class ByteDataBuffer
{
	private byte[] buffer;
	private int position;
	private int size;
	
	
	public ByteDataBuffer(int initialCapacity)
	{
		buffer = new byte[initialCapacity];
	}


	public ByteDataBuffer()
	{
		this(256);
	}
	
	
	// protected bacause the array data may be overwritten.
	// perhaps I should create a read-only byte buffer
	protected ByteDataBuffer(byte[] reuse)
	{
		buffer = reuse;
		size = reuse.length;
	}
	
	
	public void clear()
	{
		position = 0;
		size = 0;
	}
	
	
	// returns the byte read (value in the range 0..255) 
	// or throws EOFException is no more data is in the buffer
	protected int read() throws Exception
	{
		if(position >= size)
		{
			throw new EOFException();
		}
		else
		{
			return buffer[position++] & 0xff;
		}
	}


	public boolean readBoolean() throws Exception
	{
		return (read() != 0);
	}


	public byte readByte() throws Exception
	{
		return (byte)read();
	}


	public int readUnsignedByte() throws Exception
	{
		return read();
	}


	public short readShort() throws Exception
	{
		int b1 = read();
		int b2 = read();
		return (short)((b1 << 8) + b2);
	}


	public int readUnsignedShort() throws Exception
	{
		int b1 = read();
		int b2 = read();
		return (b1 << 8) + b2;
	}


	public char readChar() throws Exception
	{
		int b1 = read();
		int b2 = read();
		return (char)((b1 << 8) + b2);
	}


	public int readInt() throws Exception
	{
		int d;
		d  = (read() << 24);
		d |= (read() << 16);
		d |= (read() <<  8);
		d |= read();
		return d;
	}


	public long readLong() throws Exception
	{
		long d;
		d = ((long)read() << 56);
		d |= ((long)read() << 48);
		d |= ((long)read() << 40);
		d |= ((long)read() << 32);
		d |= ((long)read() << 24);
		d |= ((long)read() << 16);
		d |= ((long)read() << 8);
		d |= read();
		return d;
	}


	public float readFloat() throws Exception
	{
		return Float.intBitsToFloat(readInt());
	}


	public double readDouble() throws Exception
	{
		return Double.longBitsToDouble(readLong());
	}


	public void read(byte b[]) throws Exception
	{
		read(b, 0, b.length);
	}


	public void read(byte[] buf, int off, int len) throws Exception
	{
		if(position + len <= size)
		{
			System.arraycopy(buffer, position, buf, off, len);
			position += len;
			updateSize();
		}
		else
		{
			throw new EOFException();
		}
	}


	public void skip(int n) throws Exception
	{
		prepareFor(n);
		position += n;
		updateSize();
	}


	public void write(byte[] b) throws Exception
	{
		write(b, 0, b.length);
	}
	
	
	public void write(byte[] b, int off, int len) throws Exception
	{	
		prepareFor(len);
		System.arraycopy(b, off, buffer, position, len);
		position += len;
		updateSize();
	}


	public void write(int b) throws Exception
	{
		prepareFor(1);
		buffer[position++] = (byte)b;
		updateSize();
	}
	
	
	protected void writeUnsigned(int b)
	{
		buffer[position++] = (byte)(b & 0xFF);
	}


	public void writeBoolean(boolean d) throws Exception
	{
		write(d ? 1 : 0);
	}


	public void writeByte(int d) throws Exception
	{
		write(d);
	}


	public void writeChar(int d) throws Exception
	{
		prepareFor(2);
		writeUnsigned(d >> 8);
		writeUnsigned(d);
		updateSize();
	}


	public void writeShort(int d) throws Exception
	{
		prepareFor(2);
		writeUnsigned(d >> 8);
		writeUnsigned(d);
		updateSize();
	}


	public void writeInt(int d) throws Exception
	{
		prepareFor(4);
		writeUnsigned(d >> 24);
		writeUnsigned(d >> 16);
		writeUnsigned(d >>  8);
		writeUnsigned(d);
		updateSize();
	}


	public void writeLong(long d) throws Exception
	{
		prepareFor(8);
		writeUnsigned((int)(d >> 56));
		writeUnsigned((int)(d >> 48));
		writeUnsigned((int)(d >> 40));
		writeUnsigned((int)(d >> 32));
		writeUnsigned((int)(d >> 24));
		writeUnsigned((int)(d >> 16));
		writeUnsigned((int)(d >>  8));
		writeUnsigned((int)d);
		updateSize();
	}


	public void writeFloat(float v) throws Exception
	{
		writeInt(Float.floatToIntBits(v));
	}

	
	public void writeDouble(double v) throws Exception
	{
		writeLong(Double.doubleToLongBits(v));
	}

	
	public void writeFrom(InputStream in, int len) throws Exception
	{
		prepareFor(len);
		
		int left = len;
		while(left > 0)
		{
			int read = in.read(buffer, position, left);
			if(read < 0)
			{
				throw new EOFException();
			}
			
			left -= read;
			position += read;
		}
	}
	
	
	public void writeFrom(byte[] buf)
	{
		writeFrom(buf, 0, buf.length);
	}
	
	
	public void writeFrom(byte[] buf, int off, int len)
	{
		prepareFor(len);
		System.arraycopy(buf,off,buffer,position,len);
		position += len;
		updateSize();
	}
	
	
	protected void updateSize()
	{
		if(size < position)
		{
			size = position;
		}
	}
	
	
	// relative to position
	protected void prepareFor(int n)
	{
		n += position;
		
		if(n >= size)
		{
			if(n >= buffer.length)
			{
				// possibly check for out of memory condition
				byte[] b = new byte[n + n/2];
				System.arraycopy(buffer,0,b,0,size);
				buffer = b;				
			}
			else
			{
				// array unchanged, clear possible garbage [size...n-1]
				Arrays.fill(buffer, size, n, (byte)0);
			}
		}
	}


	public int size()
	{
		return size;
	}
	
	
	public byte[] toArray()
	{
		byte[] buf = new byte[size];
		System.arraycopy(buffer,0,buf,0,size);
		return buf;
	}
	
	
	public byte[] getInnerArray()
	{
		return buffer;
	}
	
	
	public int getPosition()
	{
		return position;
	}
	
	
	public void setPosition(int offset)
	{
		if(offset < 0)
		{
			throw new IllegalArgumentException("Negative offset " + offset);
		}

		// TODO weird.  what if position > size?
		if(position > size)
		{
			throw new IllegalArgumentException("position outside of the data array " + position + " size=" + size);
		}
		
		position = offset;
		if(size < position)
		{
			size = position;
		}
	}


	public boolean equals(int bufferOffset, byte[] b)
	{
		if(b.length > (size() - bufferOffset))
		{
			// too long
			return false;
		}
		
		int ix = bufferOffset;
		for(int i=0; i<b.length; i++)
		{
			if(buffer[ix++] != b[i])
			{
				return false;
			}
		}
		
		return true;
	}


	public static ByteDataBuffer reuseSuppliedArray(byte[] b)
	{
		return new ByteDataBuffer(b);
	}
}
