// Copyright (c) 2011-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JToggleButton;


public class CButtonGroup
	extends ButtonGroup
	implements ItemListener
{
	protected void onButtonStateChange() { }
	
	//
	
	public CButtonGroup()
	{
	}


	public CButtonGroup(AbstractButton ... buttons)
	{
		for(AbstractButton b: buttons)
		{
			add(b);
		}
	}


	public void add(AbstractButton b)
	{
		b.addItemListener(this);
		super.add(b);
	}


	public void itemStateChanged(ItemEvent ev)
	{
		if(ev.getStateChange() == ItemEvent.SELECTED)
		{
			onButtonStateChange();
		}
	}
	
	
	public boolean hasChoice()
	{
		for(AbstractButton b: buttons)
		{
			if(b instanceof JToggleButton)
			{
				if(((JToggleButton)b).isSelected())
				{
					return true;
				}
			}
		}
		return false;
	}
}
