// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.options;
import goryachev.common.util.CSettings;
import java.awt.Color;
import java.util.Collection;


public class ColorOption
	extends COption<Color>
{
	private Color color;
	
	
	public ColorOption(String propertyName)
	{
		super(propertyName);
	}
	
	
	public ColorOption(String propertyName, int rgb)
	{
		super(propertyName);
		color = new Color(rgb);
	}
	
	
	public ColorOption(String propertyName, int argb, boolean hasAlpha)
	{
		super(propertyName);
		color = new Color(argb, hasAlpha);
	}
	
	
	public ColorOption(String propertyName, Color c)
	{
		super(propertyName);
		color = c;
	}


	public ColorOption(String propertyName, int r, int g, int b)
	{
		super(propertyName);
		color = new Color(r, g, b);
	}


	public ColorOption(String propertyName, int r, int g, int b, int a)
	{
		super(propertyName);
		color = new Color(r, g, b, a);
	}


	public ColorOption(String propertyName, CSettings settings, Collection<COption<?>> list)
	{
		super(propertyName, settings, list);
	}


	public Color defaultValue()
	{
		return color;
	}


	public Color parseProperty(String s)
	{
		return new Color(Integer.parseInt(s, 16), true);
	}


	public String toProperty(Color value)
	{
		return Integer.toHexString(value.getRGB());
	}
}
