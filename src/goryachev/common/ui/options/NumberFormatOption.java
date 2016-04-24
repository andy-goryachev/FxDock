// Copyright (c) 2010-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.options;
import goryachev.common.util.CSettings;
import java.text.Format;
import java.util.Collection;


public class NumberFormatOption
	extends COption<Format>
{
	public NumberFormatOption(String id, CSettings settings, Collection<COption<?>> list)
	{
		super(id, settings, list);
	}


	public NumberFormatOption(String id)
	{
		super(id);
	}


	public Format defaultValue()
	{
		return CNumberFormat.createDefault();
	}


	public Format parseProperty(String s)
	{
		return CNumberFormat.parseNumberFormat(s);
	}


	public String toProperty(Format v)
	{
		if(v instanceof CNumberFormat)
		{
			return ((CNumberFormat)v).toPattern();
		}
		return null;
	}
}
