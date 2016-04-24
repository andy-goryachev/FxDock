// Copyright (c) 2008-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.options.edit;
import goryachev.common.ui.options.COption;
import goryachev.common.ui.options.OptionEditor;
import javax.swing.JComboBox;
import javax.swing.JComponent;


// implements (strict) list of choices
public abstract class ChoiceOptionEditor<T>
	extends OptionEditor<T>
{
	protected abstract T parseEditorValue(String s);
	
	protected abstract String toEditorValue(T item);
	
	//
	
	public final JComboBox combo;
	
	
	public ChoiceOptionEditor(COption<T> option)
	{
		super(option);

		combo = new JComboBox();
		combo.setBorder(BORDER_THIN);
	}
	
	
	public void setChoices(String[] choices)
	{
		combo.removeAllItems();
		for(String s: choices)
		{
			combo.addItem(s);
		}
	}


	public JComponent getComponent()
	{
		return combo;
	}
	
	
	public void setEditorValue(T value)
	{
		combo.setSelectedItem(value);
	}

	
	public T getEditorValue()
	{
		String s = (String)combo.getSelectedItem();
		return parseEditorValue(s);
	}
	
	
	public void setSelectedValue(T item)
	{
		String s = toEditorValue(item);
		if(s != null)
		{
			combo.setSelectedItem(s);
		}
	}
}
