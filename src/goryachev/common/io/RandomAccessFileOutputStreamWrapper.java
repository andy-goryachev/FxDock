// Copyright © 2013-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.common.io;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;


public class RandomAccessFileOutputStreamWrapper
	extends OutputStream
{
	private final RandomAccessFile f;


	public RandomAccessFileOutputStreamWrapper(RandomAccessFile f)
	{
		this.f = f;
	}


	@Override
	public void write(int b) throws IOException
	{
		f.write(b);
	}


	@Override
	public void write(byte b[]) throws IOException
	{
		f.write(b, 0, b.length);
	}


	@Override
	public void write(byte b[], int off, int len) throws IOException
	{
		f.write(b, off, len);
	}


	@Override
	public void flush() throws IOException
	{
	}


	@Override
	public void close() throws IOException
	{
		f.close();
	}
}
