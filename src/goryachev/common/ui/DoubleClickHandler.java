// Copyright (c) 2008-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Action;


public class DoubleClickHandler
	extends MouseAdapter
{
	private Action action;

	
	public DoubleClickHandler(Component c, Action a)
	{
		this.action = a;
		c.addMouseListener(this);
	}
	
	
	public void mouseClicked(MouseEvent ev) 
	{
		if(ev.getClickCount() == 2)
		{
			action();
		}
	}
	
	
	public void action()
	{
		action.actionPerformed(null);
	}
}
