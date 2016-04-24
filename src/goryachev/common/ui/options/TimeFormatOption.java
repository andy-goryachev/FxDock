// Copyright (c) 2010-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.options;
import goryachev.common.util.CSettings;
import java.text.SimpleDateFormat;
import java.util.Collection;


public class TimeFormatOption
	extends COption<SimpleDateFormat>
{
	public final static String FORMAT_12H = "h:mm a";
	public final static String FORMAT_12H_SECONDS = "h:mm:ss a";
	public final static String FORMAT_24H = "HH:mm";
	public final static String FORMAT_24H_SECONDS = "HH:mm:ss";
	

	public TimeFormatOption(String id, CSettings settings, Collection<COption<?>> list)
	{
		super(id, settings, list);
	}


	public TimeFormatOption(String id)
	{
		super(id);
	}


	public SimpleDateFormat defaultValue()
	{
		return new SimpleDateFormat(FORMAT_24H);
	}


	public SimpleDateFormat parseProperty(String s)
	{
		return new SimpleDateFormat(s);
	}


	public String toProperty(SimpleDateFormat f)
	{
		return f.toPattern();
	}
}
