// Copyright © 2012-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.util.Random;


/** class that generates a random stream of unique indexes in the range 0...max-1 */
public class RandomIndexGenerator
{
	public final Random random = new Random();
	private Integer[] indexes;
	private int size;
	
	
	public RandomIndexGenerator(int max)
	{
		indexes = new Integer[max];
		size = max;
	}
	
	
	protected int get(int ix)
	{
		Integer r = indexes[ix];
		if(r == null)
		{
			return ix;
		}
		else
		{
			return r;
		}
	}
	
	
	/** returns randomly chosen index or -1 if no more indexes are available */ 
	public int next()
	{
		if(size > 0)
		{
			int ix = random.nextInt(size);
			int r = get(ix);
			
			--size;
			indexes[ix] = get(size);
			return r;
		}
		return -1;
	}
}
