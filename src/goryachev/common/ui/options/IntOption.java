// Copyright (c) 2005-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.options;
import goryachev.common.util.CSettings;
import java.util.Collection;


public class IntOption
	extends COption<Integer>
{
	private Integer defaultValue;
	private Integer minValue;
	private Integer maxValue;


	public IntOption(String key, CSettings options, Collection<COption<?>> list)
	{
		super(key, options, list);
	}


	public IntOption(String key, int defaultValue)
	{
		super(key);
		this.defaultValue = defaultValue;
	}
	
	
	public IntOption(String key, int defaultValue, int minValue, int maxValue)
	{
		this(key, defaultValue);
		setRange(minValue, maxValue);
	}
	
	
	public void setRange(int min, int max)
	{
		minValue = min;
		maxValue = max;
	}
	

	public Integer defaultValue()
	{
		return defaultValue;
	}


	public Integer parseProperty(String s)
	{
		Integer v;
		try
		{
			v = Integer.parseInt(s);
		}
		catch(Exception e)
		{ 
			v = defaultValue();
		}

		if(minValue != null)
		{
			if(v < minValue)
			{
				return minValue;
			}
		}
		
		if(maxValue != null)
		{
			if(v > maxValue)
			{
				return maxValue;
			}
		}

		return v;
	}


	public String toProperty(Integer val)
	{
		return String.valueOf(val);
	}
	
	
	public int getInt()
	{
		return get();
	}
}
