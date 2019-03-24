// Copyright © 2017-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.util.Keep;
import goryachev.common.util.SB;
import goryachev.fx.internal.StandardThemes;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import javafx.scene.paint.Color;


/**
 * Color Theme.
 */
@Keep
public class Theme
{
	public Color affirm;
	public Color base;
	public Color control;
	public Color destruct;
	public Color focus;
	public Color outline;
	public Color selectedTextBG;
	public Color selectedTextFG;
	public Color textBG;
	public Color textFG;
	
	private static Theme current;
	
	
	public Theme()
	{	
	}
	

	public static Theme current()
	{
		if(current == null)
		{
			Theme t = loadFromSettings();
			if(t == null)
			{
				// TODO how to detect dark OS theme?
				t = StandardThemes.createLightTheme();
			}
			check(t);
			current = t;
		}
		return current;
	}
	
	
	private static Theme loadFromSettings()
	{
		// TODO first standard names
		// TODO use keys to load values
		return null;
	}
	

	private static void check(Theme t)
	{
		SB sb = null;
		Field[] fs = Theme.class.getDeclaredFields();
		for(Field f: fs)
		{
			int m = f.getModifiers();
			if(Modifier.isPublic(m) && !Modifier.isStatic(m))
			{
				Object v;
				try
				{
					v = f.get(t);
				}
				catch(Exception e)
				{
					v = null;
				}
				
				if(v == null)
				{
					if(sb == null)
					{
						sb = new SB();
						sb.append("Missing theme values: ");
					}
					else
					{
						sb.a(",");
					}
					sb.append(f.getName());
				}
			}
		}
		
		if(sb != null)
		{
			throw new Error(sb.toString());
		}
	}
	
	
	/** creates a light/dark compatible gray color, based on the intensity of the textBG */
	public Color gray(int gray)
	{
		if(isLight())
		{
			return Color.rgb(gray, gray, gray);
		}
		else
		{
			return Color.rgb(255 - gray, 255 - gray, 255 - gray);
		}
	}	
	
	
	public boolean isLight()
	{
		// this is good enough for now
		return (textBG.getBrightness() > 0.5);
	}
	
	
	public boolean isDark()
	{
		return !isLight();
	}
}
