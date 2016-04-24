// Copyright (c) 2008-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.options.edit;
import goryachev.common.ui.CCheckBox;
import goryachev.common.ui.options.BooleanOption;
import goryachev.common.ui.options.OptionEditor;
import javax.swing.JComponent;


public class BooleanOptionEditor
	extends OptionEditor<Boolean>
{
	public final CCheckBox editor;


	public BooleanOptionEditor(BooleanOption op, String text)
	{
		super(op);
		
		editor = new CCheckBox(text);
		editor.setOpaque(false);
	}


	public JComponent getComponent()
	{
		return editor;
	}


	public Boolean getEditorValue()
	{
		return editor.isSelected();
	}


	public void setEditorValue(Boolean on)
	{
		editor.setSelected(Boolean.TRUE.equals(on));
	}


	public String getSearchString()
	{
		return editor.getText();
	}
}