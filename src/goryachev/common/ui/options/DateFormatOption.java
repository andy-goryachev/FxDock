// Copyright (c) 2010-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.options;
import goryachev.common.util.CSettings;
import java.text.SimpleDateFormat;
import java.util.Collection;


public class DateFormatOption
	extends COption<SimpleDateFormat>
{
	public final static String FORMAT_DDMMYYYY_DASH = "dd-MM-yyyy";
	public final static String FORMAT_DDMMYYYY_DOT = "dd.MM.yyyy";
	public final static String FORMAT_DDMMYYYY_SLASH = "dd/MM/yyyy";
	public final static String FORMAT_MMDDYYYY_SLASH = "MM/dd/yyyy";
	public final static String FORMAT_YYYYMMDD_DASH = "yyyy-MM-dd";
	public final static String FORMAT_YYYYMMDD_DOT = "yyyy.MM.dd";
	

	public DateFormatOption(String id, CSettings settings, Collection<COption<?>> list)
	{
		super(id, settings, list);
	}


	public DateFormatOption(String id)
	{
		super(id);
	}


	public SimpleDateFormat defaultValue()
	{
		return new SimpleDateFormat(FORMAT_YYYYMMDD_DOT);
	}


	public SimpleDateFormat parseProperty(String s)
	{
		return new SimpleDateFormat(s);
	}


	public String toProperty(SimpleDateFormat f)
	{
		return f == null ? null : f.toPattern();
	}
}
