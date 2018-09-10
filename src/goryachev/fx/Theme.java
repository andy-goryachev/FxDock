// Copyright © 2017-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.util.CMap;
import goryachev.common.util.SB;
import goryachev.fx.internal.StandardThemes;
import javafx.scene.paint.Color;


/**
 * Color Theme.
 */
public class Theme
{
	public enum Key
	{
		/** affirm button type background */
		AFFIRM("accept", Color.class),
		/** base color for all objects */
		BASE("base", Color.class),
		/** checkbox, etc. color */
		CONTROL("control", Color.class),
		/** destructive button type background */
		DESTRUCT("destruct", Color.class),
		/** focus outline color */
		FOCUS("focus", Color.class),
		OUTLINE("outline", Color.class),
		SELECTED_TEXT_BG("selectedTextBG", Color.class),
		SELECTED_TEXT_FG("selectedTextFG", Color.class),
		TEXT_BG("textBG", Color.class),
		TEXT_FG("textFG", Color.class),
		;
		
		public final String name;
		public final Class type;
		
		Key(String name, Class type)
		{
			this.name = name;
			this.type = type;
		}
	}
	
	//
	
	public final Color affirm;
	public final Color base;
	public final Color control;
	public final Color destruct;
	public final Color focus;
	public final Color outline;
	public final Color selectedTextBG;
	public final Color selectedTextFG;
	public final Color textBG;
	public final Color textFG;
	private final CMap<Key,Object> data;
	
	private static Theme current;
	
	
	public Theme(CMap<Key,Object> data)
	{	
		this.data = data;
		
		affirm = c(Key.AFFIRM);
		base = c(Key.BASE);
		control = c(Key.CONTROL);
		destruct = c(Key.DESTRUCT);
		focus = c(Key.FOCUS);
		outline = c(Key.OUTLINE);
		selectedTextBG = c(Key.SELECTED_TEXT_BG);
		selectedTextFG = c(Key.SELECTED_TEXT_FG);
		textBG = c(Key.TEXT_BG);
		textFG = c(Key.TEXT_FG);
	}
	
	
	protected Color c(Key k)
	{
		if(k.type != Color.class)
		{
			throw new Error("Key must have Color type: " + k);
		}
		
		Object v = data.get(k);
		if(v instanceof Color)
		{
			return (Color)v;
		}
		return Color.RED;
	}
	

	public static Theme current()
	{
		if(current == null)
		{
			CMap<Key,Object> m = loadFromSettings();
			if(m == null)
			{
				m = StandardThemes.createDefaultLightTheme();
			}
			check(m);
			current = new Theme(m);
		}
		return current;
	}
	
	
	private static CMap<Key,Object> loadFromSettings()
	{
		// TODO first standard names
		// TODO use keys to load values
		return null;
	}
	

	private static void check(CMap<Key,Object> m)
	{
		SB sb = null;
		for(Key k: Key.values())
		{
			Object v = m.get(k);
			String s = null;
			if(v == null)
			{
				if(sb == null)
				{
					sb = new SB();
				}
				else
				{
					sb.nl();
				}
				sb.a("undefined ").a(k);
			}
		}
		
		if(sb != null)
		{
			throw new Error(sb.toString());
		}
	}	
}
