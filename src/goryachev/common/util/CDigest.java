// Copyright © 2012-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.io.InputStream;
import java.security.DigestException;
import java.security.MessageDigest;


/** Wrapped MessageDigest with a few convenience methods, as well as safe methods to compute hashes of sequential objects. */
public class CDigest
{
	public static class SHA512 extends CDigest
	{
		public SHA512() { super("SHA-512"); }
	}
	
	//
	
	public static class SHA256 extends CDigest
	{
		public SHA256() { super("SHA-256"); }
	}
	
	//
	
	public static class SHA1 extends CDigest
	{
		public SHA1() { super("SHA-1"); }
	}
	
	//
	
	public static class MD5 extends CDigest
	{
		public MD5() { super("MD5"); }
	}
	
	//
	
	private static final byte NULL = 0;
	private static final byte STRING = 1;
	private static final byte BYTES = 2;
	private static final byte INT = 3;
	private static final byte LONG = 4;
	private static final byte FLOAT = 5;
	private static final byte DOUBLE = 6;
	private static final byte CHAR = 7;
	private MessageDigest md;
	
	
	public CDigest(String algorithm)
	{
		try
		{
			md = MessageDigest.getInstance(algorithm);
		}
		catch(Exception e)
		{
			throw new Error(e);
		}
	}
	
	
	public void update(byte[] b)
	{
		md.update(b);
	}
	
	
	public void update(byte[] b, int offset, int len)
	{
		md.update(b, offset, len);
	}
	
	
	public void updateWithType(Object x)
	{
		if(x == null)
		{
			md.update(NULL);
		}
		else if(x instanceof byte[])
		{
			md.update(BYTES);
			md.update((byte[])x);
		}
		else if(x instanceof String)
		{
			md.update(STRING);
			md.update(((String)x).getBytes(CKit.CHARSET_UTF8));
		}
		else if(x instanceof Integer)
		{
			md.update(INT);
			updateInt((Integer)x);
		}
		else if(x instanceof Long)
		{
			md.update(LONG);
			updateLong((Long)x);
		}
		else if(x instanceof char[])
		{
			md.update(CHAR);
			updateCharArray((char[])x);
		}
		else if(x instanceof Float)
		{
			md.update(FLOAT);
			updateInt(Float.floatToIntBits((Float)x));
		}
		else if(x instanceof Double)
		{
			md.update(DOUBLE);
			updateLong(Double.doubleToRawLongBits((Double)x));
		}
		else
		{
			throw new Error("unsupported type: " + x.getClass());
		}
	}
	
	
	public void updateBytes(byte[] b)
	{
		md.update(b);
	}
	
	
	public void updateInt(int x)
	{
		md.update((byte)(x >> 24));
		md.update((byte)(x >> 16));
		md.update((byte)(x >>  8));
		md.update((byte)(x      ));
	}
	
	
	public void updateLong(long x)
	{
		md.update((byte)(x >> 56));
		md.update((byte)(x >> 48));
		md.update((byte)(x >> 40));
		md.update((byte)(x >> 32));
		md.update((byte)(x >> 24));
		md.update((byte)(x >> 16));
		md.update((byte)(x >>  8));
		md.update((byte)(x      ));
	}
	
	
	public void updateCharArray(char[] cs)
	{
		int sz = cs.length;
		for(int i=0; i<sz; i++)
		{
			char c = cs[i];
			md.update((byte)(c >> 8));
			md.update((byte)c);
		}
	}
	
	
	public byte[] digest()
	{
		byte[] rv = md.digest();
		md.reset();
		return rv;
	}
	
	
    /**
     * Completes the hash computation by performing final operations
     * such as padding. The digest is reset after this call is made.
     *
     * @param buf output buffer for the computed digest
     * @param off offset into the output buffer to begin storing the digest
     * @param len number of bytes within buf allotted for the digest
     *
     * @return the number of bytes placed into <code>buf</code>
     *
     * @exception DigestException if an error occurs.
     */
	public int digest(byte[] buf, int off, int len) throws DigestException
	{
		return md.digest(buf, off, len);
	}
	
	
	public String digestString()
	{
		return Hex.toHexString(digest());
	}
	
	
	public byte[] digest(Object x)
	{
		updateWithType(x);
		return digest();
	}
	
	
	public byte[] digest(byte[] x)
	{
		update(x);
		return digest();
	}
	
	
	public static byte[] sha512(Object x)
	{
		return new SHA512().digest(x);
	}
	
	
	public static byte[] sha256(Object x)
	{
		return new SHA256().digest(x);
	}
	
	
	public static byte[] sha256(byte[] x)
	{
		return new SHA256().digest(x);
	}
	
	
	public static byte[] sha256(InputStream in) throws Exception
	{
		SHA256 d = new SHA256();
		byte[] buf = new byte[4096];
		for(;;)
		{
			int rd = in.read(buf);
			if(rd < 0)
			{
				break;
			}
			else if(rd > 0)
			{
				d.update(buf, 0, rd);
			}
		}
		return d.digest();
	}
	
	
	public static byte[] md5(Object x)
	{
		return new MD5().digest(x);
	}
}
