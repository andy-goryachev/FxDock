// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.util.Calendar;
import java.util.TimeZone;


public class CalendarTools
{
	public static String getMonth(int month)
	{
		switch(month)
		{
		case  0: return TXT.get("CalendarTools.january", "January");
		case  1: return TXT.get("CalendarTools.february", "February");
		case  2: return TXT.get("CalendarTools.march", "March");
		case  3: return TXT.get("CalendarTools.april", "April");
		case  4: return TXT.get("CalendarTools.may", "May");
		case  5: return TXT.get("CalendarTools.june", "June");
		case  6: return TXT.get("CalendarTools.july", "July");
		case  7: return TXT.get("CalendarTools.august", "August");
		case  8: return TXT.get("CalendarTools.september", "September");
		case  9: return TXT.get("CalendarTools.october", "October");
		case 10: return TXT.get("CalendarTools.november", "November");
		case 11: return TXT.get("CalendarTools.december", "December");
		}
		return null;
	}
	
	
	public static String getShortMonth(int month)
	{
		switch(month)
		{
		case  0: return TXT.get("CalendarTools.short.january", "Jan");
		case  1: return TXT.get("CalendarTools.short.february", "Feb");
		case  2: return TXT.get("CalendarTools.short.march", "Mar");
		case  3: return TXT.get("CalendarTools.short.april", "Apr");
		case  4: return TXT.get("CalendarTools.short.may", "May");
		case  5: return TXT.get("CalendarTools.short.june", "Jun");
		case  6: return TXT.get("CalendarTools.short.july", "Jul");
		case  7: return TXT.get("CalendarTools.short.august", "Aug");
		case  8: return TXT.get("CalendarTools.short.september", "Sep");
		case  9: return TXT.get("CalendarTools.short.october", "Oct");
		case 10: return TXT.get("CalendarTools.short.november", "Nov");
		case 11: return TXT.get("CalendarTools.short.december", "Dec");
		}
		return null;
	}
	
	
	/** returns short day of week using Monday=0 system */
	public static final String getShortDayOfWeek(int d)
	{
		switch(d)
		{
		case 0: return TXT.get("CalendarTools.short.monday", "Mo");
		case 1: return TXT.get("CalendarTools.short.tuesday", "Tu");
		case 2: return TXT.get("CalendarTools.short.wednesday", "We");
		case 3: return TXT.get("CalendarTools.short.thursday", "Th");
		case 4: return TXT.get("CalendarTools.short.friday", "Fr");
		case 5: return TXT.get("CalendarTools.short.saturday", "Sa");
		case 6: return TXT.get("CalendarTools.short.sunday", "Su");
		}
		return null;
	}
	
	
	/** returns short day of week using Calendar.DAY_OF_WEEK numbers */
	public static final String getShortDayOfWeekCalendar(int d)
	{
		switch(d)
		{
		case Calendar.MONDAY: return getShortDayOfWeek(0);
		case Calendar.TUESDAY: return getShortDayOfWeek(1);
		case Calendar.WEDNESDAY: return getShortDayOfWeek(2);
		case Calendar.THURSDAY: return getShortDayOfWeek(3);
		case Calendar.FRIDAY: return getShortDayOfWeek(4);
		case Calendar.SATURDAY: return getShortDayOfWeek(5);
		case Calendar.SUNDAY: return getShortDayOfWeek(6);
		}
		return null;
	}
	
	
	public static final String getDayOfWeek(int d)
	{
		switch(d)
		{
		case 0: return TXT.get("CalendarTools.long.monday", "Monday");
		case 1: return TXT.get("CalendarTools.long.tuesday", "Tuesday");
		case 2: return TXT.get("CalendarTools.long.wednesday", "Wednesday");
		case 3: return TXT.get("CalendarTools.long.thursday", "Thursday");
		case 4: return TXT.get("CalendarTools.long.friday", "Friday");
		case 5: return TXT.get("CalendarTools.long.saturday", "Saturday");
		case 6: return TXT.get("CalendarTools.long.sunday", "Sunday");
		}
		return null;
	}


	/** sets the calendar time to 00:00:00.000 */
	public static void set0000(Calendar c)
	{
		c.set(Calendar.MILLISECOND, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.HOUR_OF_DAY, 0);
	}


	/** returns true if two calendars point to the same day/month/year */
	public static boolean isSameDate(Calendar a, Calendar b)
	{
		if(a.get(Calendar.DAY_OF_MONTH) == b.get(Calendar.DAY_OF_MONTH))
		{
			if(a.get(Calendar.MONTH) == b.get(Calendar.MONTH))
			{
				if(a.get(Calendar.YEAR) == b.get(Calendar.YEAR))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	
	public static String formatTimeZoneOffset(TimeZone tz)
	{
		if(tz == null)
		{
			return null;
		}
		
		int offset = -tz.getOffset(System.currentTimeMillis());

		SB sb = new SB();
		if(offset >= 0)
		{
			sb.a('+');
		}
		else
		{
			sb.a('-');
			offset = -offset;
		}

		offset /= 60000;

		sb.a(CKit.formatTwoDigits(offset / 60));
		sb.append(':');
		sb.a(CKit.formatTwoDigits(offset % 60));
		return sb.toString();
	}
}
