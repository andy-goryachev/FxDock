// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.ui.theme.AssignMnemonic;
import javax.swing.Box;
import javax.swing.JMenuBar;


public class CMenuBar
	extends JMenuBar
{
	public CMenuBar()
	{
	}
	
	
	public void addNotify()
	{
		AssignMnemonic.assign(this);
		super.addNotify();
	}
	
	
	public void fill()
	{
		add(Box.createHorizontalGlue());
	}
}
