// Copyright (c) 2008-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;


public class CMenuItem
	extends JMenuItem
{
	public CMenuItem(Icon icon, String text, Accelerator acc, Action a)
	{
		super(a);
		setIcon(icon);
		setText(text);	
		acc.set(this);
		init();
	}
	
	
	public CMenuItem(String text, Accelerator acc, Action a)
	{
		super(a);
		setText(text);
		acc.set(this);
		init();
	}
	
	
	public CMenuItem(String text, KeyStroke ks, Action a)
	{
		super(a);
		setText(text);
		setAccelerator(ks);
		init();
	}
	
	
	public CMenuItem(Accelerator acc, Action a)
	{
		super(a);
		acc.set(this);
		init();
	}
	
	
	public CMenuItem(Icon icon, String text, Action a)
	{
		super(a);
		setIcon(icon);
		setText(text);	
		init();
	}
	
	
	public CMenuItem(Icon icon, Action a)
	{
		super(a);
		setIcon(icon);
		init();
	}
	
	
	public CMenuItem(String text, Action a)
	{
		super(a);
		setText(text);
		init();
	}
	
	
	public CMenuItem(Action a)
	{
		super(a);
		init();
	}
	
	
	public CMenuItem(String text)
	{
		super(text);
	}
	
	
	public CMenuItem()
	{
	}
	

	private void init()
	{
		Icon icon = getIcon();
		if(icon == null)
		{
			setIcon(new CIcon(16));
		}
		UI.setMnemonic(this);
	}
}
