// Copyright © 2014-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.common.io;
import java.io.ByteArrayOutputStream;


/** convenient DWriter backed by a ByteArrayOutputStream */
public class DWriterBytes
	extends DWriter
{
	public DWriterBytes()
	{
		super(new ByteArrayOutputStream());
	}
	
	
	public byte[] toByteArray()
	{
		return getByteArrayOutputStream().toByteArray();
	}


	private ByteArrayOutputStream getByteArrayOutputStream()
	{
		return (ByteArrayOutputStream)out;
	}
}
