// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.options.edit;
import goryachev.common.ui.options.DateFormatOption;
import goryachev.common.util.CLookup;
import goryachev.common.util.TXT;
import java.text.SimpleDateFormat;


public class DateFormatOptionEditor
	extends ChoiceOptionEditor<SimpleDateFormat>
{
	private CLookup lookup;
	
	
	public DateFormatOptionEditor(DateFormatOption op)
	{
		super(op);
		
		String d = TXT.get("DateFormatOptionEditor.abbreviated.day", "Day");
		String m = TXT.get("DateFormatOptionEditor.abbreviated.month", "Month");
		String y = TXT.get("DateFormatOptionEditor.abbreviated.year", "Year");
		
		Long t = System.currentTimeMillis();
		
		String dmyDash = d + "-" + m + "-" + y + " (" + new SimpleDateFormat(DateFormatOption.FORMAT_DDMMYYYY_DASH).format(t) + ")";
		String dmyDot = d + "." + m + "." + y + " (" + new SimpleDateFormat(DateFormatOption.FORMAT_DDMMYYYY_DOT).format(t) + ")";
		String dmySlash = d + "/" + m + "/" + y + " (" + new SimpleDateFormat(DateFormatOption.FORMAT_DDMMYYYY_SLASH).format(t) + ")";
		String mdySlash = m + "/" + d + "/" + y + " (" + new SimpleDateFormat(DateFormatOption.FORMAT_MMDDYYYY_SLASH).format(t) + ")";
		String ymdDash = y + "-" + m + "-" + d + " (" + new SimpleDateFormat(DateFormatOption.FORMAT_YYYYMMDD_DASH).format(t) + ")";
		String ymdDot = y + "." + m + "." + d + " (" + new SimpleDateFormat(DateFormatOption.FORMAT_YYYYMMDD_DOT).format(t) + ")";
		
		lookup = new CLookup
		(
			dmyDash,  DateFormatOption.FORMAT_DDMMYYYY_DASH,
			dmyDot,   DateFormatOption.FORMAT_DDMMYYYY_DOT,
			dmySlash, DateFormatOption.FORMAT_DDMMYYYY_SLASH,
			mdySlash, DateFormatOption.FORMAT_MMDDYYYY_SLASH,
			ymdDash,  DateFormatOption.FORMAT_YYYYMMDD_DASH,
			ymdDot,   DateFormatOption.FORMAT_YYYYMMDD_DOT
		);
		
		setChoices(new String[]
		{
			mdySlash,
			ymdDash,
			ymdDot,
			dmyDash,
			dmyDot,
			dmySlash
		});
	}


	protected SimpleDateFormat parseEditorValue(String s)
	{
		String spec = (String)lookup.lookup(s);
		return spec == null ? null : new SimpleDateFormat(spec);
	}


	protected String toEditorValue(SimpleDateFormat f)
	{
		return f.toPattern();
	}
	
	
	public String getSearchString()
	{
		return null;
	}
	
	
	public void setEditorValue(SimpleDateFormat f)
	{
		String v = (f == null ? null : f.toPattern());
		v = (String)lookup.lookup(v);
		combo.setSelectedItem(v);
	}
}
