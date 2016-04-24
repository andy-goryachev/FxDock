// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JRadioButton;


public class CRadioButton
	extends JRadioButton
{
	public CRadioButton()
	{
		init();
	}
	
	
	public CRadioButton(Icon icon)
	{
		super(icon);
		init();
	}
	
	
	public CRadioButton(Action a)
	{
		super(a);
		init();
	}
	
	
	public CRadioButton(String text)
	{
		super(text);
		init();
	}
	
	
	public CRadioButton(Icon icon, boolean selected)
	{
		super(icon, selected);
		init();
	}
	
	
	public CRadioButton(String text, boolean selected)
	{
		super(text, selected);
		init();
	}
	
	
	public CRadioButton(String text, Icon icon)
	{
		super(text, icon);
		init();
	}
	
	
	public CRadioButton(String text, Icon icon, boolean selected)
	{
		super(text, icon, selected);
		init();
	}
	
	
	private void init()
	{
		setOpaque(false);
	}
}
