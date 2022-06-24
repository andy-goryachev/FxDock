// Copyright © 2021 Andy Goryachev <andy@goryachev.com>
package goryachev.fx.internal;
import goryachev.fx.FX;
import javafx.scene.paint.Color;


/**
 * Color Mixer performs gamma-correct runtime mixing of colors.
 */
public class ColorMixer
{
	private Object value; // Color or Color[]
	
	
	public ColorMixer()
	{
	}
	
	
	public ColorMixer(Color base)
	{
		this.value = base;
	}
	
	
	public ColorMixer(ColorMixer x)
	{
		if(x != null)
		{
			value = x.value;
		}
	}
	
	
	public void add(Color c)
	{
		if(value instanceof Color)
		{
			value = new Color[] { (Color)value, c };
		}
		else if(value instanceof Color[])
		{
			Color[] old = (Color[])value;
			Color[] cs = new Color[old.length + 1];
			System.arraycopy(old, 0, cs, 0, old.length);
			cs[old.length] = c;
			value = cs;
		}
		else
		{
			value = c;
		}
	}
	
	
	public Color getColor()
	{
		if(value instanceof Color)
		{
			return (Color)value;
		}
		else if(value instanceof Color[])
		{
			Color c = mix((Color[])value);
			value = c;
			return c;
		}
		return null; 
	}
	
	
	public static Color mix(Color[] colors)
	{
		return FX.mix(colors);
	}
}
