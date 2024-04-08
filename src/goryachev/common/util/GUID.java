// Copyright © 2014-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicLong;


/** 
 * Generates (hopefully) globally unique identifiers.
 */
public class GUID
{
	private static final int INITIAL_RANDOMNESS_BYTES = 32;
	private static final int SEP = 0xff;
	private static byte[] machineID;
	private static AtomicLong seq = new AtomicLong();
	
	
	/** generates globally uniqueue byte array, using SHA-256 message digest (32 bytes long) */
	public static byte[] generate()
	{
		CDigest d = CDigest.sha256();
		return generate(d);
	}
	
	
	/** generates globally uniqueue byte array using the specified message digest */
	public static byte[] generate(CDigest d)
	{
		d.update(machineID());
		d.updateByte(SEP);
		d.updateLong(System.currentTimeMillis());
		d.updateByte(SEP);
		d.updateLong(System.nanoTime());
		d.updateByte(SEP);
		d.updateLong(seq.incrementAndGet());
		return d.digest();
	}
	
	
	private static byte[] machineID()
	{
		if(machineID == null)
		{
			synchronized(GUID.class)
			{
				if(machineID == null)
				{
					// let's throw some degree of randomness
					byte[] b = new byte[INITIAL_RANDOMNESS_BYTES];
					new SecureRandom().nextBytes(b);
					
					CDigest d = CDigest.sha256();
					d.update(b);
					
					// java runtime
					Runtime r = Runtime.getRuntime();
					d.updateInt(r.availableProcessors());
					d.updateByte(SEP);
					d.updateLong(r.freeMemory());
					d.updateByte(SEP);
					d.updateInt(r.hashCode());
					d.updateByte(SEP);
					
					// machine-specific parameters
					d.updateString(System.getProperty("java.runtime.version"));
					d.updateByte(SEP);
					d.updateString(System.getProperty("java.class.path"));
					d.updateByte(SEP);
					d.updateString(System.getProperty("os.arch"));
					d.updateByte(SEP);
					d.updateString(System.getProperty("os.name"));
					d.updateByte(SEP);
					d.updateString(System.getProperty("os.version"));
					d.updateByte(SEP);
					
					// user-specific parameters
					d.updateString(System.getProperty("user.country"));
					d.updateByte(SEP);
					d.updateString(System.getProperty("user.dir"));
					d.updateByte(SEP);
					d.updateString(System.getProperty("user.home"));
					d.updateByte(SEP);
					d.updateString(System.getProperty("user.language"));
					d.updateByte(SEP);
					d.updateString(System.getProperty("user.name"));
					d.updateByte(SEP);
					d.updateString(System.getProperty("user.timezone"));
					
					machineID = d.digest();
				}
			}
		}
		return machineID;
	}
}
