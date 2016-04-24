// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.options.edit;
import goryachev.common.ui.options.TimeFormatOption;
import goryachev.common.util.CLookup;
import java.text.SimpleDateFormat;


public class TimeFormatOptionEditor
	extends ChoiceOptionEditor<SimpleDateFormat>
{
	private CLookup lookup;
	
	
	public TimeFormatOptionEditor(TimeFormatOption op)
	{
		super(op);
		
		Long t = System.currentTimeMillis();
		
		String time12 = new SimpleDateFormat(TimeFormatOption.FORMAT_12H).format(t);
		String time12s = new SimpleDateFormat(TimeFormatOption.FORMAT_12H_SECONDS).format(t);
		String time24 = new SimpleDateFormat(TimeFormatOption.FORMAT_24H).format(t);
		String time24s = new SimpleDateFormat(TimeFormatOption.FORMAT_24H_SECONDS).format(t);
		
		lookup = new CLookup
		(
			time12,  TimeFormatOption.FORMAT_12H,
			time12s, TimeFormatOption.FORMAT_12H_SECONDS,
			time24,  TimeFormatOption.FORMAT_24H,
			time24s, TimeFormatOption.FORMAT_24H_SECONDS
		);
		
		setChoices(new String[]
		{
			time24,
			time24s,
			time12,
			time12s
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
}
