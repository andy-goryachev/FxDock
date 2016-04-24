// Copyright (c) 2010-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.options;
import java.awt.Component;


public abstract class ComponentOption
{
	public abstract String toProperty();
	
	public abstract void parseProperty(String s);
	
	//
	
	public ComponentOption(String key, Component c)
	{
		// TODO
	}
}
