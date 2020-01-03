// Copyright © 2011-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.io;
import goryachev.common.util.CKit;


// Format:
//   long signature;
//   segments[]
//   {
//       byte id;
//       <data>
//   }
public abstract class PrimitiveHandler
{
	public abstract Object read(PrimitiveInputStream in) throws Exception;
	
	public abstract void write(Object x, PrimitiveOutputStream out) throws Exception;

	//

	private byte id;
	
	
	public PrimitiveHandler(byte id)
	{
		this.id = id;
	}
	
	
	public byte getID()
	{
		return id;
	}
	
	
	public boolean isPrimitive()
	{
		return true;
	}
	
	
	// do not remove - used in string handler, see readShortString()
	protected void storeWithID(Object x, PrimitiveOutputStream out) throws Exception
	{
		out.writeRawByte(getID());
		write(x, out);
	}
	

	public void readContainer(PrimitiveInputStream in, DContainer c) throws Exception
	{
		Object x = read(in);
		c.add(x);
	}
	
	
	protected static String readString(PrimitiveInputStream in, int sz) throws Exception
	{
		byte[] b = new byte[sz];
		in.readRawBytes(b);
		return new String(b, CKit.CHARSET_UTF8);
	}
}
