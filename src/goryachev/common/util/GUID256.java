// Copyright © 2014-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicLong;


/** 
 * Generates (hopefully) globally unique identifiers.
 */
public class GUID256
{
	private static final int INITIAL_RANDOMNESS_BYTES = 32;
	private static byte[] machineID;
	private static AtomicLong seq = new AtomicLong();
	

	public static BKey generateKey()
	{
		return new BKey(generateBytes());
	}
	
	
	public static SKey generateSKey()
	{
		return new SKey(generateHexString());
	}
	
	
	/** generates globally uniqueue byte array */
	public static byte[] generateBytes()
	{
		CDigest d = new CDigest.SHA256();
		d.update(machineID());
		d.updateWithType(System.currentTimeMillis());
		d.updateWithType(System.nanoTime());
		d.updateWithType(seq.incrementAndGet());
		return d.digest();
	}
	
	
	/** generates GUID as a hex string */
	public static String generateHexString()
	{
		return Hex.toHexString(generateBytes());
	}
	
	
	/** generates GUID as a decimal string */
	public static String generateDecimalString()
	{
		return new BigInteger(1, generateBytes()).toString();
	}
	
	
	private synchronized static byte[] machineID()
	{
		if(machineID == null)
		{
			// let's throw some degree of randomness
			byte[] b = new byte[INITIAL_RANDOMNESS_BYTES];
			new SecureRandom().nextBytes(b);
			
			CDigest d = new CDigest.SHA256();
			d.update(b);
			
			// java runtime
			Runtime r = Runtime.getRuntime();
			d.updateWithType(r.availableProcessors());
			d.updateWithType(r.freeMemory());
			d.updateWithType(r.hashCode());
			
			// machine-specific parameters
			d.updateWithType(System.getProperty("java.runtime.version"));
			d.updateWithType(System.getProperty("java.class.path"));
			d.updateWithType(System.getProperty("os.arch"));
			d.updateWithType(System.getProperty("os.name"));
			d.updateWithType(System.getProperty("os.version"));
			
			// user-specific parameters
			d.updateWithType(System.getProperty("user.country"));
			d.updateWithType(System.getProperty("user.dir"));
			d.updateWithType(System.getProperty("user.home"));
			d.updateWithType(System.getProperty("user.language"));
			d.updateWithType(System.getProperty("user.name"));
			d.updateWithType(System.getProperty("user.timezone"));
			
			machineID = d.digest();
		}
		return machineID;
	}
}
