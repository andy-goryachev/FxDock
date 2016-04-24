// Copyright (c) 2008-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.util.HasProperty;
import java.util.Collection;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;


// TODO extend popup window to fit the content
public class CComboBox
	extends JComboBox
{
	public CComboBox()
	{
		init();
	}
	
	
	public CComboBox(Object[] items)
	{
		super(items == null ? new Object[0] : items);
		init();
	}

	
	public CComboBox(Collection<?> items)
	{
		super(items == null ? new Object[0] : items.toArray());
		init();
	}
	
	
	private void init()
	{
		setMaximumRowCount(32);
	}
	
	
	public void select(Object item)
	{
		setSelectedItem(item);
	}

	
	public void replaceAll(Object[] items)
	{
		setModel(new DefaultComboBoxModel(items));
	}
	
	
	public void replaceAll(Collection<?> items)
	{
		Object[] a = (items == null ? new Object[0] : items.toArray());
		setModel(new DefaultComboBoxModel(a));
	}
	
	
	public void addItems(Object[] items)
	{
		for(Object item: items)
		{
			addItem(item);
		}
	}
	
	
	public void addItems(Iterable<?> items)
	{
		for(Object item: items)
		{
			addItem(item);
		}
	}
	
	
	public void selectByProperty(String value)
	{
		if(value != null)
		{
			int sz = getModel().getSize();
			for(int i=0; i<sz; i++)
			{
				Object x = getModel().getElementAt(i);
				if(x instanceof HasProperty)
				{
					if(value.equals(((HasProperty)x).getProperty()))
					{
						setSelectedIndex(i);
						return;
					}
					else if(value.equals(x))
					{
						setSelectedIndex(i);
						return;
					}
				}
			}
			
			// this is questionable
			if(isEditable())
			{
				setSelectedItem(value);
			}
		}
	}
	
	
	public String getSelectedProperty()
	{
		Object x = getSelectedItem();
		if(x instanceof HasProperty)
		{
			return ((HasProperty)x).getProperty();
		}
		else 
		{
			// this is questionable
			return x == null ? null : x.toString();
		}
	}
	
	
	public JComponent getEditorComponent()
	{
		return (JComponent)getEditor().getEditorComponent();
	}
	
	
	public Object getCurrentItem()
	{
		if(isEditable())
		{
			return getEditor().getItem();
		}
		else
		{
			return getSelectedItem();
		}
	}
}
