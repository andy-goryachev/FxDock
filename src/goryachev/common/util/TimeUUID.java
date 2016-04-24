// Copyright (c) 2014-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicLong;


/** time-based UUID: 8 bytes of system time + 32 bytes of sha-256 of sequence-, user-, machine-, time- specific data */
public class TimeUUID
{
	private long time;
	private final byte[] bytes;
	private static final AtomicLong sequence = new AtomicLong();
	
	
	public TimeUUID(long time, byte[] b)
	{
		this.time = time;
		this.bytes = b;
	}
	
	
	public static TimeUUID parse(Object x) throws Exception
	{
		if(x == null)
		{
			return null;
		}
		else if(x instanceof TimeUUID)
		{
			return (TimeUUID)x;
		}
		else
		{
			String s = x.toString().trim();
			if(s.length() != 81)
			{
				return null;
			}
			
			if(s.charAt(16) != '-')
			{
				return null;
			}
			
			long time = Hex.parseLong(s, 0, 16);
			if(time < 0)
			{
				return null;
			}
			
			byte[] b = Hex.parseByteArray(s, 17, 64);
			if(b.length != 32)
			{
				return null;
			}
			
			return new TimeUUID(time, b);
		}
	}
	
	
	public long timestamp()
	{
		return time;
	}
	

	public String toString()
	{
		return Hex.toHexString(time, 16) + "-" + Hex.toHexString(bytes);
	}
	
	
	public boolean equals(Object x)
	{
		if(x == this)
		{
			return true;
		}
		else if(x instanceof TimeUUID)
		{
			TimeUUID t = (TimeUUID)x;
			return 
				(time == t.time) &&
				CKit.equals(bytes, t.bytes);
		}
		else
		{
			return false;
		}
	}
	
	
	public int hashCode()
	{
		return CKit.hashCode(TimeUUID.class, time, bytes);
	}
	
	
	/** generates a time uuid string */
	public static String nowString()
	{
		return now().toString();
	}
	
	
	/** generates a time uuid */
	public static TimeUUID now()
	{
		long time = System.currentTimeMillis();
		
		byte[] b = new byte[16];
		new SecureRandom().nextBytes(b);
		
		// mix in enough location-specific information and random information to ensure 
		// low probability of collision
		CDigest d = new CDigest.SHA256();
		d.update(time);
		d.update(System.nanoTime());
		d.update(System.getProperty("user.name"));
		d.update(System.getProperty("user.home"));
		d.update(sequence.incrementAndGet());
		d.update(b);
		
		b = d.digest();
		
		return new TimeUUID(time, b);
	}
}
