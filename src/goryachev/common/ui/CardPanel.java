// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.LayoutManager;
import java.util.HashMap;
import javax.swing.JPanel;


public class CardPanel
	extends JPanel
{
	private HashMap<Component,String> map = new HashMap();
	private Component current;
	private static int id;
	
	
	public CardPanel()
	{
		super(new CardLayout());
	}


	public void setLayout(LayoutManager x)
	{
		if(x instanceof CardLayout)
		{
			super.setLayout(x);
		}
	}
	
	
	public Component add(Component c)
	{
		String name = map.get(c);
		if(name == null)
		{
			name = String.valueOf(id++);
			map.put(c, name);
			return super.add(name, c);
		}
		else
		{
			// already there
			return c;
		}
	}


	public void show(Component c)
	{
		String name = map.get(c);
		if(name != null)
		{
			current = c;
			getCardLayout().show(this, name);
		}
	}


	public CardLayout getCardLayout()
	{
		return (CardLayout)getLayout();
	}
	
	
	public Component getCurrentCard()
	{
		return current;
	}
}
