// Copyright (c) 2008-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.options.edit;
import goryachev.common.ui.options.COption;


public abstract class EditableChoiceOptionEditor<T>
	extends ChoiceOptionEditor<T>
{
	public EditableChoiceOptionEditor(COption<T> option)
	{
		super(option);

		combo.setEditable(true);
	}
}
