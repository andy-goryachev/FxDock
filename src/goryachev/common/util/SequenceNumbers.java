// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.util.concurrent.atomic.AtomicLong;


/** Simple friendly sequence number generator */
public class SequenceNumbers
{
	private static AtomicLong number = new AtomicLong();
	
	
	public static long get()
	{
		return number.getAndIncrement();
	}
}
