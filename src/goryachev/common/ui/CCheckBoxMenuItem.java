// Copyright (c) 2008-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;


public class CCheckBoxMenuItem
	extends JCheckBoxMenuItem
{
	public CCheckBoxMenuItem(Icon icon, String text, Accelerator acc, Action a)
	{
		super(a);
		setIcon(icon);
		setText(text);
		acc.set(this);
		init();
	}
	
	
	public CCheckBoxMenuItem(Icon icon, String text, Action a)
	{
		super(a);
		setIcon(icon);
		setText(text);
		init();
	}
	
	
	public CCheckBoxMenuItem(String text, Action a)
	{
		super(a);
		setText(text);
		init();
	}
	
	
	public CCheckBoxMenuItem(Action a)
	{
		super(a);
		init();
	}


	private void init()
	{
//		if(getAction() instanceof CAction)
//		{
//			setSelected(((CAction)getAction()).isSelected());
//		}
		
		UI.setMnemonic(this);
	}
	
	
//	protected void actionPropertyChanged(Action action, String propertyName)
//	{
//		if(CAction.SELECTED.equals(propertyName))
//		{
//			setSelected(((CAction)action).isSelected());
//		}
//		else
//		{
//			super.actionPropertyChanged(action, propertyName);
//		}
//	}
}
