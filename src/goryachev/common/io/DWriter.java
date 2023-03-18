// Copyright © 2011-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.common.io;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/** Conventient binary data writer */
public class DWriter
	extends OutputStream
{
	protected OutputStream out;
	
	
	public DWriter(OutputStream out)
	{
		this.out = out;
	}
	
	
	public DWriter(File f) throws Exception
	{
		this(new BufferedOutputStream(new FileOutputStream(f)));
	}
	
	
	public void writeByteArray(byte[] b) throws IOException
	{
		CIOTools.writeByteArray(out, b);
	}


	public void write(int b) throws IOException
	{
		out.write(b);
	}


	public void write(byte b[], int off, int len) throws IOException
	{
		out.write(b, off, len);
	}


	public void flush() throws IOException
	{
		out.flush();
	}


	public void writeBoolean(boolean x) throws IOException
	{
		out.write(x ? 1 : 0);
	}


	public void writeByte(int x) throws IOException
	{
		out.write(x);
	}


	public void writeChar(int x) throws IOException
	{
		out.write((x >>> 8) & 0xff);
		out.write(x & 0xff);
	}
	

	public void writeShort(int x) throws IOException
	{
		out.write(x >>> 8);
		out.write(x);
	}


	public void writeInt(int x) throws IOException
	{
		out.write(x >>> 24);
		out.write(x >>> 16);
		out.write(x >>>  8);
		out.write(x);
	}
	
	
	/** writes a single byte as a signed 8 bit int (range -128..127) */
	public void writeInt8(int x) throws IOException
	{
		out.write(x);
	}
	
	
	/** writes a single byte as an unsigned 8 bit int (range 0..255) */
	public void writeUInt8(int x) throws IOException
	{
		out.write(x);
	}

	
	public void writeIntArray(int[] b) throws IOException
	{
		if(b == null)
		{
			writeInt(-1);
		}
		else
		{
			writeInt(b.length);
			for(int i=0; i<b.length; i++)
			{
				writeInt(b[i]);
			}
		}
	}


	public void writeLong(long x) throws IOException
	{
		out.write((int)(x >>> 56) & 0xff);
		out.write((int)(x >>> 48) & 0xff);
		out.write((int)(x >>> 40) & 0xff);
		out.write((int)(x >>> 32) & 0xff);
		out.write((int)(x >>> 24) & 0xff);
		out.write((int)(x >>> 16) & 0xff);
		out.write((int)(x >>> 8) & 0xff);
		out.write((int)x & 0xff);
	}


	public void writeFloat(float x) throws IOException
	{
		writeInt(Float.floatToIntBits(x));
	}


	public void writeDouble(double x) throws IOException
	{
		writeLong(Double.doubleToLongBits(x));
	}


	public void writeString(String s) throws IOException
	{
		CIOTools.writeString(out, s);
	}


	public void close() throws IOException
	{
		out.close();
	}
}
