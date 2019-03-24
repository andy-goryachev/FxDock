// Copyright © 2013-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.common.io;
import goryachev.common.util.CKit;
import java.io.ByteArrayOutputStream;


public class BitWriter
	extends BitStreamWriter
{
	private ByteArrayOutputStream stream;
	
	
	public BitWriter()
	{
		this(32);
	}
	
	
	public BitWriter(int sizeInBytes)
	{
		stream = new ByteArrayOutputStream(sizeInBytes);
		setOutputStream(stream);
	}
	
	
	/** invokes close() and returns the resulting array */
	public byte[] toByteArray()
	{
		CKit.close(this);
		
		return stream.toByteArray();
	}
}
