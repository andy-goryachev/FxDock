// Copyright © 2005-2023 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


/** SW measures elapsed time since instantioation or reset() */
public class SW
{
	private long start;


	public SW()
	{
		reset();
	}


	public void reset()
	{
		start = System.nanoTime();
	}


	/** returns elapsed time in nanoseconds */
	public long getElapsedTimeNano()
	{
		return System.nanoTime() - start;
	}


	/** returns elapsed time in milliseconds */
	public long getElapsedTimeMilli()
	{
		return getElapsedTimeNano() / 1000000;
	}


	public String toString()
	{
		long elapsed = getElapsedTimeNano();
		return CKit.formatTimePeriod(elapsed / 1000000L);
	}
}
