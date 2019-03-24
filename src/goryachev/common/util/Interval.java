// Copyright © 2012-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


public class Interval<T extends Comparable<T>>
{
	public T min;
	public T max;
	
	
	public Interval(T min, T max)
	{
		this.min = min;
		this.max = max;
	}
	
	
	// works only if two intersect
	public void combineIntersecting(Interval<T> other)
	{
		if(min.compareTo(other.min) > 0)
		{
			min = other.min;
		}
		
		if(max.compareTo(other.max) < 0)
		{
			max = other.max;
		}
	}


	public boolean isPoint()
	{
		return min.compareTo(max) == 0;
	}
	
	
	protected boolean intersects(Interval<T> other)
	{
		if(max.compareTo(other.min) < 0)
		{
			return false;
		}
		
		if(min.compareTo(other.max) > 0)
		{
			return false;
		}
		
		return true;
	}
}
