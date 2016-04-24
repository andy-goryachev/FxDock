// Copyright (c) 2014-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.util.CKit;
import goryachev.common.util.CMap;
import goryachev.common.util.Rex;
import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.text.JTextComponent;


public class FormLogic
{
	private final CMap<Component,Object> entries = new CMap();
	
	
	public FormLogic()
	{
	}
	
	
	public FormLogic(Component ... cs)
	{
		for(Component c: cs)
		{
			add(c);
		}
	}
	
	
	public void add(Component c)
	{
		Object v = getValue(c);
		entries.put(c, v);
	}
	
	
	public void reset()
	{
		for(Component c: entries.keySet())
		{
			Object v = getValue(c);
			entries.put(c, v);
		}
	}
	
	
	public boolean isModified()
	{
		for(Component c: entries.keySet())
		{
			Object v = getValue(c);
			Object prev = entries.get(c);
			if(CKit.notEquals(v, prev))
			{
				return true;
			}
		}
		return false;
	}
	
	
	public Object getValue(Component c)
	{
		if(c instanceof JTextComponent)
		{
			return ((JTextComponent)c).getText();
		}
		if(c instanceof JCheckBox)
		{
			return ((JCheckBox)c).isSelected();
		}
		if(c instanceof JRadioButton)
		{
			return ((JRadioButton)c).isSelected();
		}
		else
		{
			throw new Rex("not supported: " + CKit.className(c));
		}
	}
}
