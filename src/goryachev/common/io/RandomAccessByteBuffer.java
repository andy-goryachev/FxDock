// Copyright © 2005-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.io;
import java.io.DataOutput;
import java.io.IOException;


// unsynchronized byte array similar to RandomAccessFile
public class RandomAccessByteBuffer
{
	byte[] buffer;
	int size;
	int position;
	

	public RandomAccessByteBuffer(int capacity)
	{
		buffer = new byte[capacity];
		size = 0;
	}
	
	
	public RandomAccessByteBuffer()
	{
		this(1024);
	}
	
	
	public byte readByte() throws IOException
	{
		checkAvailable(1);
		return buffer[position++];
	}

	
	public void write(byte[] b) throws IOException
	{
		write(b,0,b.length);
	}
	

	public void write(byte[] b, int off, int len) throws IOException
	{
		if((off<0) || (off>b.length) || (len<0) || ((off+len) > b.length) || ((off+len) < 0))
		{
			throw new IndexOutOfBoundsException();
		}
		else if(len == 0)
		{
			return;
		}
		
		prepareFor(len);
		System.arraycopy(b,off,buffer,position,len);
		position += len;
		if(size < position)
		{
			size = position;
		}
	}        
        
	
	public void writeBoolean(boolean d) throws IOException
	{
		writeByte(d ? 1 : 0);
	}
	

	public void writeByte(int d) throws IOException
	{
		prepareFor(1);
		buffer[position++] = (byte)d;
		if(size < position)
		{
			size = position;
		}
	}


	public void writeInt(int d) throws IOException
	{
		prepareFor(4);
		buffer[position++] = (byte)(d >>> 24);
		buffer[position++] = (byte)(d >>> 16);
		buffer[position++] = (byte)(d >>>  8);
		buffer[position++] = (byte)(d);
		if(size < position)
		{
			size = position;
		}
	}
	

	public void writeLong(long d) throws IOException
	{
		prepareFor(8);
		buffer[position++] = (byte)(d >>> 56);
		buffer[position++] = (byte)(d >>> 48);
		buffer[position++] = (byte)(d >>> 40);
		buffer[position++] = (byte)(d >>> 32);
		buffer[position++] = (byte)(d >>> 24);
		buffer[position++] = (byte)(d >>> 16);
		buffer[position++] = (byte)(d >>>  8);
		buffer[position++] = (byte)(d);
		if(size < position)
		{
			size = position;
		}
	}

	
	// same format as StringTools.writeASCIIString()
	public void writeASCIIString(String s) throws IOException
	{
		if(s == null)
		{
			writeByte(-1);
		}
		else
		{
			int n = s.length();
			if(n > 127)
			{
				throw new IOException("string too long");
			}
			writeByte(n);
			prepareFor(n);
			for(int i=0; i<n; i++)
			{
				buffer[position++] = (byte)s.charAt(i);
			}
			if(size < position)
			{
				size = position;
			}
		}
	}


	// same format as StringTools.writeUtfString()
	public void writeUtfString(String s) throws IOException
	{
		if(s == null)
		{
			writeInt(-1);
		}
		else
		{
			int n = s.length();
			writeInt(n);
			prepareFor(n+n);
			for(int i=0; i<n; i++)
			{
				int c = s.charAt(i);
				buffer[position++] = (byte)(c >>> 8);
				buffer[position++] = (byte)(c);
			}
			if(size < position)
			{
				size = position;
			}
		}
	}
    

	public void seek(int pos) throws IOException
	{
		if((pos < 0) || (pos > size))
		{
			throw new IOException("index out of bounds");
		}
		position = pos;
	}
	
	
	public int getPointer()
	{
		return position;
	}
	
	
	public void writeTo(DataOutput out) throws IOException
	{
		out.write(buffer,0,size);
	}
	
	
	private void checkAvailable(int n) throws IOException
	{
		if(position + n > size)
		{
			throw new IOException("not enough bytes in the buffer");
		}
	}
	
	
	private void prepareFor(int n)
	{
		n += position;
		if(n > buffer.length)
		{
			// need to grow buffer
			// new size is the largest of double the current buffer length or required size + safety margin
			byte buf[] = new byte[Math.max(buffer.length<<1,n+256)];
			System.arraycopy(buffer,0,buf,0,size);
			buffer = buf;
		}
	}
	
	
	public int size()
	{
		return size;
	}
	
	
	public byte[] toArray()
	{
		byte[] a = new byte[size];
		System.arraycopy(buffer,0,a,0,size);
		return a;
	}
}
