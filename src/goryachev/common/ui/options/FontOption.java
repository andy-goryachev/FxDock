// Copyright (c) 2008-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.options;
import goryachev.common.util.CPlatform;
import java.awt.Font;


public class FontOption
	extends COption<Font>
{
	private String name;
	private int style;
	private float size;
	
	
	public FontOption(String property)
	{
		super(property);
	}
	
	
	public FontOption(String property, String name, float size)
	{
		this(property, name, size, Font.PLAIN);
	}
	
	
	public FontOption(String property, String name, float size, int style)
	{
		this(property);
		this.name = name;
		this.size = size;
		this.style = style;
	}
	
	
	public Font defaultValue()
	{
		if(name == null)
		{
			if(CPlatform.isWindows())
			{
				return createFont("Tahoma", Font.PLAIN, 11);
			}
			else if(CPlatform.isMac())
			{
				return createFont("Helvetica", Font.PLAIN, 12);
			}
			else
			{
				return createFont("san-serif", Font.PLAIN, 12);
			}
		}
		else
		{
			return createFont(name, style, size);
		}
	}
	
	
	public static Font createFont(String name, int style, float size)
	{
		int intSize = (int)size;
		Font f = new Font(name,style,intSize);
		
		// there is no public Font(String,int,float) constructor
		if(size != intSize)
		{
			f = f.deriveFont(size);
		}
		return f;
	}

	
	public String toProperty(Font f)
	{
		return f.getName() + "," + f.getStyle() + "," + f.getSize2D();
	}


	public Font parseProperty(String s)
	{
		String[] ss = s.split(",");
		
		int style;
		try
		{
			style = Integer.parseInt(ss[1]);
		}
		catch(Exception e)
		{
			style = Font.PLAIN;
		}
		
		float size;
		try
		{
			size = Float.parseFloat(ss[2]);
		}
		catch(Exception e)
		{
			if(CPlatform.isWindows())
			{
				size = 11;
			}
			else
			{
				size = 12;
			}
		}
		
		return createFont(ss[0], style, size);
	}
}
