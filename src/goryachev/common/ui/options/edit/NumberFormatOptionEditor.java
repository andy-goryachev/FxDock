// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.options.edit;
import goryachev.common.ui.options.CNumberFormat;
import goryachev.common.ui.options.NumberFormatOption;
import java.text.Format;


public class NumberFormatOptionEditor
	extends ChoiceOptionEditor<Format>
{
	// http://en.wikipedia.org/wiki/Decimal_mark
	public static final String EUROPEAN = "1.234.567,89";
	public static final String SI_FRENCH = "1 234 567,89";
	public static final String SI_ENGLISH = "1 234 567.89";
	public static final String US = "1,234,567.89";
	
	// these are not supported by DecimalFormat
	// http://stackoverflow.com/questions/5379231/displaying-currency-in-indian-numbering-format
	//public static final String ASIAN = "123,4567.89";
	//public static final String SI_ENGLISH = "1 234 567.89";
	//public static final String SOUTH_ASIAN = "12,34,567.89";
	
	
	public NumberFormatOptionEditor(NumberFormatOption op)
	{
		super(op);
		
		setChoices(new String[]
		{
			US,
			EUROPEAN,
			SI_FRENCH,
			SI_ENGLISH
		});
	}


	protected Format parseEditorValue(String s)
	{
		if(s == null)
		{
			return null;
		}
		
		if(EUROPEAN.equals(s))
		{
			return CNumberFormat.parseNumberFormat(CNumberFormat.PROPERTY_EUROPEAN);
		}
		else if(SI_FRENCH.equals(s))
		{
			return CNumberFormat.parseNumberFormat(CNumberFormat.PROPERTY_SI_FRENCH);
		}
		else if(SI_ENGLISH.equals(s))
		{
			return CNumberFormat.parseNumberFormat(CNumberFormat.PROPERTY_SI_ENGLISH);
		}
		else if(US.equals(s))
		{
			return CNumberFormat.parseNumberFormat(CNumberFormat.PROPERTY_US);
		}
		
		return null;
	}


	protected String toEditorValue(Format f)
	{
		if(f instanceof CNumberFormat)
		{
			return ((CNumberFormat)f).toPattern();
		}
		return null;
	}
	
	
	public String getSearchString()
	{
		return null;
	}
}
