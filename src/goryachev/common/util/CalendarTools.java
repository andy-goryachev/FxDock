// Copyright © 2012-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.util.Calendar;
import java.util.TimeZone;


public class CalendarTools
{
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
