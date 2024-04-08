// Copyright © 2011-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.common.io;
import java.io.ByteArrayInputStream;


/**
 * ByteArrayInputStream with getPosition();
 */
public class DByteArrayInputStream
	extends ByteArrayInputStream
{
	public DByteArrayInputStream(byte[] b)
	{
		super(b);
	}
	
	
	public int getPosition()
	{
		return pos;
	}
}