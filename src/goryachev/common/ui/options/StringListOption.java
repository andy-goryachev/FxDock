// Copyright (c) 2011-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.options;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.SB;


public class StringListOption
	extends COption<CList<String>>
{
	private static final char DIVIDER = '\0';
	private String[] defaultValues;
	
	
	public StringListOption(String name)
	{
		super(name);
	}
	
	
	public StringListOption(String name, String ... defaultValues)
	{
		super(name);
		this.defaultValues = defaultValues;
	}
	
	
	// should not return null
	public CList<String> get()
	{
		CList<String> v = super.get();
		if(v == null)
		{
			v = new CList();
		}
		return v;
	}


	public CList<String> parseProperty(String s)
	{
		CList<String> a = new CList();
		try
		{
			if(s != null)
			{
				for(String x: CKit.split(s, DIVIDER))
				{
					a.add(x);
				}
			}
		}
		catch(Exception e)
		{ }
		return a;
	}


	public String toProperty(CList<String> list)
	{
		try
		{
			SB sb = new SB();
			for(String s: list)
			{
				if(sb.getLength() > 0)
				{
					sb.a(DIVIDER);
				}
				sb.a(s);
			}

			return sb.toString();
		}
		catch(Exception e)
		{
			return null;
		}
	}


	public CList<String> defaultValue()
	{
		if(defaultValues != null)
		{
			return new CList(defaultValues);
		}
		return null;
	}
}
