// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import java.awt.Graphics;
import javax.swing.JComponent;


public abstract class CSkin
{
	public abstract void paint(Graphics g, JComponent c);
	
	//
	
	public CSkin()
	{ }
	
	
	public static void set(JComponent c, CSkin s)
	{
		c.putClientProperty(CSkin.class, s);
	}
	
	
	public static CSkin get(JComponent c)
	{
		return (CSkin)c.getClientProperty(CSkin.class);
	}
}
