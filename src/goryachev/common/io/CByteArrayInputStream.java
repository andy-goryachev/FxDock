// Copyright © 2011-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.io;
import java.io.ByteArrayInputStream;


public class CByteArrayInputStream
	extends ByteArrayInputStream
{
	public CByteArrayInputStream(byte[] b)
	{
		super(b);
	}
	
	
	public int getPosition()
	{
		return pos;
	}
}