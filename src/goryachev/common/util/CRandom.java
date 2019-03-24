// Copyright © 2010-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


public final class CRandom
{
	public static final String RAND_ALGORITHM = "SHA1PRNG";
	private static SecureRandom random = init();
    
	
	private static SecureRandom init()
	{
		try
		{
			return SecureRandom.getInstance(RAND_ALGORITHM);
		}
		catch(NoSuchAlgorithmException e)
		{
			// should not happen
			Log.ex(e);
			throw new Error(e);
		}
	}
	
	
	public static byte[] generateBits(int bits)
	{
		byte[] b = new byte[bits >>> 3];
		synchronized(random)
		{
			random.nextBytes(b);
		}
		return b;
	}
	
	
	public static String generateHexBits(int bits)
	{
		byte[] b = generateBits(bits);
		return Hex.toHexString(b);
	}
	
	
	public static long nextLong()
	{
		synchronized(random)
		{
			return random.nextLong();
		}
	}
}
