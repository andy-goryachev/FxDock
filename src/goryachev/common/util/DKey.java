// Copyright © 2011-2021 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import goryachev.common.io.BitStream;
import goryachev.common.io.PrimitiveInput;
import goryachev.common.io.PrimitiveOutput;
import goryachev.common.io.PrimitiveOutputStream;
import java.io.File;
import java.util.Arrays;


/** Byte array based database identifier (key) */
@Deprecated // use BKey
public class DKey
{
	public static final int SIZE_IN_BYTES = 64;
	private byte[] sha512;
	private int hash;
	
	
	public DKey(byte[] sha512)
	{
		if(sha512.length != SIZE_IN_BYTES)
		{
			throw new RuntimeException("wrong key length: " + sha512.length);
		}
		this.sha512 = sha512.clone();
	}
	
	
	public String toString()
	{
		return asString();
	}


	public String asString()
	{
		return toHex(sha512);
	}
	
	
	public byte[] toByteArray()
	{
		return sha512.clone();
	}


	public static final String toHex(byte[] b)
	{
		SB sb = new SB();
		
		for(int i=0; i<b.length; i++)
		{
			int n = b[i];
			sb.append(toHex(n >> 4));
			sb.append(toHex(n));
		}

		return sb.toString();
	}


	private static char toHex(int n)
	{
		return "0123456789ABCDEF".charAt(n & 0x0f);
	}


	public static DKey parse(Object x) throws Exception
	{
		if(x instanceof DKey)
		{
			return (DKey)x;
		}
		else if(x instanceof String)
		{
			String s = (String)x;
			byte[] b = Parsers.parseByteArray(s);
			if(b.length == SIZE_IN_BYTES)
			{
				return new DKey(b);
			}
		}
		else if(x instanceof byte[])
		{
			return new DKey((byte[])x);
		}

		throw new Exception();
	}
	
	
	public static DKey parseQuiet(Object x)
	{
		try
		{
			return parse(x);
		}
		catch(Exception e)
		{ }
		
		return null;
	}
	
	
	public String getShortRepresentation()
	{
		SB sb = new SB();
		sb.a("Key=");
		for(int i=0; i<5; i++)
		{
			int n = sha512[i];
			sb.append(toHex(n >> 4));
			sb.append(toHex(n));
		}
		return sb.toString();
	}

	
	public static int getSize()
	{
		return SIZE_IN_BYTES;
	}
	
	
	public boolean equals(Object x)
	{
		if(x == this)
		{
			return true;
		}
		else if(x instanceof DKey)
		{
			DKey other = (DKey)x;
			if(hashCode() == other.hashCode())
			{
				return Arrays.equals(sha512, other.sha512);
			}
		}
		
		return false;
	}
	
	
	public int hashCode()
	{
		if(hash == 0)
		{
			hash = 0x55555555 ^ Arrays.hashCode(sha512);
		}
		return hash;
	}
	
	
	public static DKey load(File f) throws Exception
	{
		byte[] b = CKit.readBytes(f, getSize() + 10);
		if(b != null)
		{
			if(b.length == getSize())
			{
				return new DKey(b);
			}
		}
		throw new Exception();
	}
	
	
	public void save(File f) throws Exception
	{
		CKit.write(sha512, f);
	}
	
	
	public void writeBytes(PrimitiveOutputStream out) throws Exception
	{
		out.writeRawBytes(sha512);
	}
	
	
	public BitStream getBitStream()
	{
		return new BitStream(sha512);
	}
	
	
	public static void write(DKey k, PrimitiveOutput out) throws Exception
	{
		out.write(k == null ? null : k.sha512);
	}
	
	
	public static DKey read(PrimitiveInput in) throws Exception
	{
		byte[] b = in.readByteArray();
		return b == null ? null : new DKey(b);
	}
}
