// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.util.CKit;
import goryachev.common.util.Progress;


// http://stackoverflow.com/questions/2779600/how-to-estimate-download-time-remaining-accurately
// http://en.wikipedia.org/wiki/Moving_average#Exponential_moving_average
// http://stackoverflow.com/questions/1881652/estimating-forecasting-download-completion-time
public class ProgressLogic
{
	public static final double FUDGE_FACTOR = 2.0;
	private long start;
	private double durationAverage;
	private double durationExp = -1;
	private double alpha;
	
	
	public ProgressLogic()
	{
		setNPeriods(2);
		start();
	}
	
	
	public void setAlpha(double a)
	{
		alpha = a;
	}
	
	
	public void setNPeriods(int n)
	{
		setAlpha(2.0 / (n + 1));
	}
	
	
	protected long time()
	{
		return System.currentTimeMillis();
	}
	
	
	public void start()
	{
		setStart(time());
	}
	
	
	public synchronized void setStart(long t)
	{
		start = t;
	}
	
	
	public void setProgress(Progress p)
	{
		setProgress(p.getProgress());
	}
	
	
	public synchronized void setProgress(double p)
	{
		if(start == 0)
		{
			start();
		}
		
		// FIX
		// average computed over all period
		durationAverage = /*FUDGE_FACTOR * */ (time() - start) / p;
		
		// exponential moving average
		if(durationExp < 0)
		{
			durationExp = durationAverage;
		}
		else
		{
			durationExp = alpha * durationAverage + (1.0 - alpha) * durationExp;
		}
	}
	
	
	/** returns estimated time to completion */
	public synchronized long getEstimatedTimeToCompletion()
	{
		return (long)(start + durationAverage - time());
		//return (long)(start + Math.max(durationExp, durationAverage) - time());
	}
	
	
	/** returns estimated time to completion, computed using exponential moving average */
	public synchronized long getEstimatedExponentialTimeToCompletion()
	{
		return (long)(start + durationExp - time());
	}
	
	
	/** returns estimated time to completion string */
	public synchronized String getEstimatedTimeRemaining()
	{
		if(durationExp < 0)
		{
			return "";
		}
		
		long est = getEstimatedTimeToCompletion();
		if(est > 30 * CKit.MS_IN_A_DAY)
		{
			return "...";
		}
		
		long err = Math.abs(est - getEstimatedExponentialTimeToCompletion());
		
//		long hrs = est/CKit.MS_IN_AN_HOUR;
//		long min = est/CKit.MS_IN_A_MINUTE;
//		long sec = est/CKit.MS_IN_A_SECOND;
		
//		est -= getElapsedTime();
		if(est < 0)
		{
			est = 0;
		}
		
		//return Theme.formatTimePeriodRough(est);
		return Theme.formatTimePeriod2(est);
	}
	
	
	public long getElapsedTime()
	{
		return System.currentTimeMillis() - start;
	}
	
	
	public String getElapsedTimeString()
	{
		if(start <= 0)
		{
			return null;
		}
		
		long t = System.currentTimeMillis() - start;
		if(t < 0)
		{
			return null;
		}
		
		return Theme.formatTimePeriod2(t);
	}
}
