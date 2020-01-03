// Copyright © 2013-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.util.Random;


/**
 * Various random number utilities. 
 */
public class Randomizer
{
	private static final Random random = new Random();
	
	
	protected static synchronized int nextInt(int n)
	{
		return random.nextInt(n);
	}
	

	/** returns a random number in the range [center-variance ... center+variance[ */
	public static int getInt(int center, int variance)
	{
		return center - variance + nextInt(variance + variance);
	}


	/** returns a random number in the range [center*(1/2) ... center*(3/4)[ */
	public static int getInt(int center)
	{
		return getInt(center, center / 2);
	}
}
