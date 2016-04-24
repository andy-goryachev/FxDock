// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.options;
import goryachev.common.util.Log;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;


/** 
 * A DecimalFormat wrapper to support incompatible patterns. 
 * To be replaced later with a better format implementation, possibly based on ICU
 */ 
public class CNumberFormat
	extends Format
{
	public static final String PROPERTY_EUROPEAN = "#.##0,###";
	public static final String PROPERTY_SI_FRENCH = "# ##0,###";
	public static final String PROPERTY_SI_ENGLISH = "# ##0.###";
	public static final String PROPERTY_US = "#,##0.###";

	private final DecimalFormat format;
	private final String pattern;


	public CNumberFormat(DecimalFormat f, String pattern)
	{
		this.format = f;
		this.pattern = pattern;
	}


	public StringBuffer format(Object number, StringBuffer toAppendTo, FieldPosition pos)
	{
		if(number == null)
		{
			return toAppendTo;
		}
		return format.format(number, toAppendTo, pos);
	}


	public Object parseObject(String source, ParsePosition pos)
	{
		return format.parse(source, pos);
	}


	public String toPattern()
	{
		return pattern;
	}
	
	
	public int hashCode()
	{
		return CNumberFormat.class.hashCode() ^ format.hashCode();
	}


	public boolean equals(Object x)
	{
		if(x == this)
		{
			return true;
		}
		else if(x instanceof CNumberFormat)
		{
			return toPattern().equals(((CNumberFormat)x).toPattern());
		}
		else
		{
			return false;
		}
	}
	
	
	public static Format createDefault()
	{
		return parseNumberFormat(null);
	}

	
	// java number format does not support all possible options
	// and I don't want to bring ICU code yet
	// http://stackoverflow.com/questions/5379231/displaying-currency-in-indian-numbering-format
	public static Format parseNumberFormat(String s)
	{
		DecimalFormat f = null;
		String pattern = null;
		
		if(s != null)
		{
			try
			{
				if(PROPERTY_US.equals(s))
				{
					f = (DecimalFormat)NumberFormat.getInstance(new Locale("en", "US"));
					pattern = PROPERTY_US;
				}
				else if(PROPERTY_EUROPEAN.equals(s))
				{
					f = (DecimalFormat)NumberFormat.getInstance(new Locale("de", "DE"));
					pattern = PROPERTY_EUROPEAN;
				}
				else if(PROPERTY_SI_ENGLISH.equals(s))
				{
					f = (DecimalFormat)NumberFormat.getInstance(new Locale("fr", "FR"));
					f.getDecimalFormatSymbols().setDecimalSeparator('.');
					pattern = PROPERTY_SI_ENGLISH;
				}
				else if(PROPERTY_SI_FRENCH.equals(s))
				{
					f = (DecimalFormat)NumberFormat.getInstance(new Locale("fr", "FR"));
					pattern = PROPERTY_SI_FRENCH;
				}
			}
			catch(Exception e)
			{
				Log.err(e);
			}
		}
		
		if(f == null)
		{
			f = (DecimalFormat)NumberFormat.getInstance();
			pattern = f.toPattern();
		}
		
		return new CNumberFormat(f, pattern);
	}
}
