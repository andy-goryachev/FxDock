// Copyright © 2012-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import goryachev.common.util.api.IMessageDigest;
import goryachev.common.util.api.IMessageDigestBlake2b;
import java.io.InputStream;


/** A Message Digest with convenience methods. */
public class CDigest
{
	private final IMessageDigest md;
	
	
	protected CDigest(IMessageDigest md)
	{
		this.md = md;
	}
	
	
	public static CDigest sha256()
	{
		IMessageDigest md = IMessageDigest.getInstance("SHA-256");
		return new CDigest(md);
	}
	
	
	public static CDigest sha512()
	{
		IMessageDigest md = IMessageDigest.getInstance("SHA-512");
		return new CDigest(md);
	}
	
	
	public static CDigest blake2b(int bits)
	{
		try
		{
			IMessageDigestBlake2b md = Lookup.get(IMessageDigestBlake2b.class);
			return new CDigest(md);
		}
		catch(Exception e)
		{
			throw new Error(e);
		}
	}
	
	
	public void update(byte b)
	{
		md.update(b);
	}
	
	
	public void updateByte(int b)
	{
		md.update((byte)b);
	}
	
	
	public void update(byte[] b)
	{
		md.update(b, 0, b.length);
	}
	
	
	public void update(byte[] b, int offset, int len)
	{
		md.update(b, offset, len);
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
	
	
	public void updateChar(char c)
	{
		md.update((byte)(c >> 8));
		md.update((byte)c);
	}
	
	
	public void updateCharArray(char[] cs)
	{
		if(cs == null)
		{
			updateInt(-1);
		}
		else
		{
			int sz = cs.length;
			updateInt(sz);
			
			for(int i=0; i<sz; i++)
			{
				char c = cs[i];
				updateChar(c);
			}
		}
	}
	
	
	public void updateString(String s)
	{
		if(s == null)
		{
			updateInt(-1);
		}
		else
		{
			updateInt(s.length());
			byte[] b = s.getBytes(CKit.CHARSET_UTF8);
			md.update(b, 0, b.length);
		}
	}
	
	
	public byte[] digest()
	{
		return md.digest();
	}
	
	
	public void reset()
	{
		md.reset();
	}
	
	
	public byte[] compute(byte[] bytes)
	{
		update(bytes);
		return digest();
	}
	
	
	public byte[] compute(InputStream in) throws Exception
	{
		byte[] buf = new byte[4096];
		for(;;)
		{
			CKit.checkCancelled();
			
			int rd = in.read(buf);
			if(rd < 0)
			{
				return digest();
			}
			else if(rd > 0)
			{
				update(buf, 0, rd);
			}
		}
	}
}
