// Copyright © 2014-2024 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;


/** Convenience wrapper for java GregorianCalendar class */
public class CCalendar
{
	protected final GregorianCalendar cal;
	
	
	protected CCalendar()
	{
		this.cal = new GregorianCalendar();
	}
	
	
	public CCalendar(Locale loc)
	{
		this.cal = new GregorianCalendar(loc);
	}
	
	
	public CCalendar(TimeZone tz)
	{
		this.cal = new GregorianCalendar(tz);
	}
	
	
	public CCalendar(TimeZone tz, Locale loc)
	{
		this.cal = new GregorianCalendar(tz, loc);
	}
	
	
	public int getMilliseconds()
	{
		return cal.get(Calendar.MILLISECOND);
	}
	
	
	public void setMilliseconds(int x)
	{
		cal.set(Calendar.MILLISECOND, x);
	}
	
	
	public void addMilliseconds(int x)
	{
		cal.add(Calendar.MILLISECOND, x);
	}
	
	
	public int getSecond()
	{
		return cal.get(Calendar.SECOND);
	}
	
	
	public void setSecond(int x)
	{
		cal.set(Calendar.SECOND, x);
	}
	
	
	public void addSecond(int x)
	{
		cal.add(Calendar.SECOND, x);
	}
	
	
	public int getMinute()
	{
		return cal.get(Calendar.MINUTE);
	}
	
	
	public void setMinute(int x)
	{
		cal.set(Calendar.MINUTE, x);
	}
	
	
	public void addMinute(int x)
	{
		cal.add(Calendar.MINUTE, x);
	}
	
	
	public int getHour()
	{
		return cal.get(Calendar.HOUR_OF_DAY);
	}
	
	
	public void setHour(int x)
	{
		cal.set(Calendar.HOUR_OF_DAY, x);
	}
	
	
	public void addHour(int x)
	{
		cal.add(Calendar.HOUR_OF_DAY, x);
	}
	
	
	public int getDay()
	{
		return cal.get(Calendar.DAY_OF_MONTH);
	}
	
	
	public void setDay(int x)
	{
		cal.set(Calendar.DAY_OF_MONTH, x);
	}
	
	
	public void addDay(int x)
	{
		cal.add(Calendar.DAY_OF_MONTH, x);
	}
	
	
	public int getMonth()
	{
		return cal.get(Calendar.MONTH);
	}
	
	
	/** sets Calendar class month!  Calendar.JANUARY = 0 */
	public void setMonth(int x)
	{
		cal.set(Calendar.MONTH, x);
	}
	
	
	/** sets human month (January = 1) */
	public void setMonthHuman(int x)
	{
		cal.set(Calendar.MONTH, x - 1);
	}
	
	
	public void addMonth(int x)
	{
		cal.add(Calendar.MONTH, x);
	}
	
	
	public int getYear()
	{
		return cal.get(Calendar.YEAR);
	}
	
	
	public void setYear(int x)
	{
		cal.set(Calendar.YEAR, x);
	}
	
	
	public void addYear(int x)
	{
		cal.add(Calendar.YEAR, x);
	}
	
	
	public int getDayOfWeek()
	{
		return cal.get(Calendar.DAY_OF_WEEK);
	}
	
	
	public void setTime(long t)
	{
		cal.setTimeInMillis(t);
	}
	
	
	/** @return time in milliseconds */
	public long getTime()
	{
		return cal.getTimeInMillis();
	}
	
	
	/** @return time in milliseconds */
	public long getTimeInMillis()
	{
		return cal.getTimeInMillis();
	}
	
	
	public void setTimeInMillis(long t)
	{
		cal.setTimeInMillis(t);
	}
	
	
	@Deprecated // TODO rename setStartOfDay
	public void set0000()
	{
		CalendarTools.set0000(cal);
	}
	
	
	public int getLastDayOfMonth()
	{
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	
	public void setTimeZone(TimeZone tz)
	{
		cal.setTimeZone(tz);
	}
	
	
	public String getDisplayName(int field, int style)
	{
		// WARNING: we don't control translation for the current locale
		return cal.getDisplayName(field, style, Locale.getDefault());
	}
	
	
	@Override
	public String toString()
	{
		return cal.toString();
	}
}
