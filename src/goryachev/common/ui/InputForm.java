// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.util.CKit;
import goryachev.common.util.CList;
import goryachev.common.util.CMap;
import goryachev.common.util.Rex;
import java.util.List;
import javax.swing.text.JTextComponent;


/** Input form provides tracking of user modifications of a collection of entry fields */
public class InputForm
{
	public static interface Field
	{
		public Object getFieldValue();	
	}
	
	//
	
	private CMap<Object,Object> fields = new CMap();
	
	
	public InputForm()
	{
	}
	
	
	public InputForm(Object ... fields)
	{
		addFields(fields);
	}
	
	
	public void add(Object f)
	{
		if(isValidType(f))
		{
			fields.put(f, null);
		}
		else
		{
			throw new Rex("unsupported entry field type: " + CKit.simpleName(f));
		}
	}
	
	
	public void addFields(Object ... fs)
	{
		for(Object f: fs)
		{
			add(f);
		}
	}
	
	
	public List<Object> getFields()
	{
		return new CList(fields.keySet());
	}
	
	
	public boolean isModified()
	{
		for(Object f: fields.keySet())
		{
			Object old = fields.get(f);
			Object val = getValue(f);
			
			if(CKit.notEquals(old, val))
			{
				return true;
			}
		}
		return false;
	}
	
	
	public void reset()
	{
		for(Object f: fields.keySet())
		{
			Object val = getValue(f);
			fields.put(f, val);
		}
	}
	
	
	protected boolean isValidType(Object f)
	{
		if(f instanceof Field)
		{
			return true;
		}
		else if(f instanceof JTextComponent)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	protected Object getValue(Object f)
	{
		if(f instanceof Field)
		{
			return ((Field)f).getFieldValue();
		}
		else if(f instanceof JTextComponent)
		{
			return ((JTextComponent)f).getText();
		}
		else
		{
			return null;
		}
	}
}
