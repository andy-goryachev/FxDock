// Copyright (c) 2008-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;
import java.awt.Color;
import javax.swing.Icon;


public class AChoice
	implements HasProperty
{
	private String id;
	private String name;
	private Color foreground;
	private Color background;
	private Icon icon;
	
	
	public AChoice(String id, String name, Icon icon, Color foreground, Color background)
	{
		this.id = id;
		this.name = name;
		this.icon = icon;
		this.foreground = foreground;
		this.background = background;
	}
	
	
	public AChoice(String id, String name, Icon icon)
	{
		this.id = id;
		this.name = name;
		this.icon = icon;
	}

	
	public AChoice(String id, String name)
	{
		this.id = id;
		this.name = name;
	}
	
	
	public AChoice(String id)
	{
		this(id,id);
	}
	
	
	public String getProperty()
	{
		return id;
	}
	
	
	public String getName()
	{
		return name;
	}
	
	
	public String toString()
	{
		return getName();
	}
	
	
	public Color getForeground()
	{
		return foreground;
	}
	
	
	public Color getBackground()
	{
		return background;
	}
	
	
	public Icon getIcon()
	{
		return icon;
	}
}
