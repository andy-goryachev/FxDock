// Copyright © 2013-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


public class Progress
{
	public static final Progress UNKNOWN = new Progress(0.0);
	public static final Progress DONE = new Progress(1.0);
	
	//
	
	private double progress;
	
	
	public Progress(double progress)
	{
		this.progress = progress;
	}
	
	
	public Progress(int count, int total)
	{
		this.progress = (total == 0 ? 0.0 : count/(double)total);
	}
	
	
	public Progress(long count, long total)
	{
		this.progress = (total == 0 ? 0.0 : count/(double)total);
	}
	
	
	public double getProgress()
	{
		return progress;
	}
	
	
	public boolean equals(Object x)
	{
		if(x == this)
		{
			return true;
		}
		else if(x instanceof Progress)
		{
			return progress == ((Progress)x).progress;
		}
		else 
		{
			return false;
		}
	}
	
	
	public int hashCode()
	{
		int h = FH.hash(0, Progress.class);
		return FH.hash(h, progress);
	}
	
	
	public String getPercentString()
	{
		return getPercentString(2);
	}
	
	
	public String getPercentString(int significantDigits)
	{
		return CKit.getPercentString(progress, significantDigits);
	}

	
	public String toString()
	{
		return "Progress:" + progress;
	}
}
