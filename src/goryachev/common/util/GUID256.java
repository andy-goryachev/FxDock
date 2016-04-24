// Copyright (c) 2014-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.security.SecureRandom;


public class GUID256
{
	private static byte[] seed;
	

	public static synchronized String get()
	{
		if(seed == null)
		{
			seed = new byte[32];
			new SecureRandom().nextBytes(seed);
		}
		
		CDigest d = new CDigest.SHA256();
		d.update(seed);
		d.update(System.currentTimeMillis());
		d.update(System.nanoTime());
		byte[] b = d.digest();
		return Hex.toHexString(b);
	}
}
