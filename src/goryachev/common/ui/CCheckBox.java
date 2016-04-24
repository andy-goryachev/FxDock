// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBox;


public class CCheckBox
	extends JCheckBox
{
	public CCheckBox()
	{
		this(null, null, false);
	}


	public CCheckBox(Icon icon)
	{
		this(null, icon, false);
	}


	public CCheckBox(Icon icon, boolean selected)
	{
		this(null, icon, selected);
	}


	public CCheckBox(String text)
	{
		this(text, null, false);
	}


	public CCheckBox(Action a)
	{
		this();
		setAction(a);
	}


	public CCheckBox(String text, boolean selected)
	{
		this(text, null, selected);
	}


	public CCheckBox(String text, Icon icon)
	{
		this(text, icon, false);
	}


	public CCheckBox(String text, Icon icon, boolean selected)
	{
		super(text, icon, selected);
		
		// this should be a default
		setOpaque(false);
	}
}
