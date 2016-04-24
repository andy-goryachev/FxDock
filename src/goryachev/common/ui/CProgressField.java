// Copyright (c) 2011-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import javax.swing.JLabel;


public class CProgressField
	extends JLabel
{
	private int size;
	
	
	public CProgressField(int size)
	{
		this.size = size;
		setAnimation(false);
		setVerticalAlignment(CENTER);
	}
	
	
	public void setAnimation(boolean on)
	{
		if(on)
		{
			setIcon(Theme.waitIcon(size));
		}
		else
		{
			setIcon(new CIcon(size));
		}
	}
	
	
	public void setIconSize(int size)
	{
		if(size < 0)
		{
			throw new IllegalArgumentException();
		}
		
		this.size = size;
	}
	
	
	public int getIconSize()
	{
		return size;
	}
}
