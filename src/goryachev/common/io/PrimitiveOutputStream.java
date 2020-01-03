// Copyright © 2011-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.io;
import goryachev.common.util.CKit;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class PrimitiveOutputStream
	implements PrimitiveOutput
{
	private OutputStream out;
	
	
	public PrimitiveOutputStream(OutputStream out) throws Exception
	{
		this.out = out;
		writeRawByte(Persister.SIGNATURE);
	}
	
	
	public PrimitiveOutputStream(File f) throws Exception
	{
		this(new BufferedOutputStream(new FileOutputStream(f)));
	}


	public void writeRawBoolean(boolean x) throws Exception
	{
		out.write(x ? 1 : 0);
	}
	
	
	public void writeRawByte(int x) throws Exception
	{
		out.write(x);
	}
	
	
	public void writeRawChar(int x) throws Exception
	{
		out.write((x >>> 8) & 0xff);
		out.write(x & 0xff);
	}

	
	public void writeRawShort(short x) throws Exception
	{
		out.write((x >>> 8) & 0xff);
		out.write(x & 0xff);
	}
	
	
	public void writeRawInt(int x) throws Exception
	{
		out.write((x >>> 24) & 0xff);
		out.write((x >>> 16) & 0xff);
		out.write((x >>> 8) & 0xff);
		out.write(x & 0xff);
	}


	public void writeRawLong(long x) throws Exception
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
	

	public void writeRawFloat(float x) throws Exception
	{
		writeRawInt(Float.floatToIntBits(x));
	}


	public void writeRawDouble(double x) throws Exception
	{
		writeRawLong(Double.doubleToLongBits(x));
	}
	
	
	public void writeRawBytes(byte[] b) throws Exception
	{
		out.write(b);
	}
	
	
	protected void writeRawString(String s) throws Exception
	{
		byte[] b = s.getBytes(CKit.CHARSET_UTF8);
		if(b.length < 127)
		{
			writeRawByte(b.length);
		}
		else
		{
			writeRawByte(Persister.STRING);
			writeRawInt(b.length);
		}
		writeRawBytes(b);
	}
	

	public void close() throws IOException
	{
		out.close();
	}

	
	public void write(Object x) throws Exception
	{
		Persister.store(x, this);
	}
}
