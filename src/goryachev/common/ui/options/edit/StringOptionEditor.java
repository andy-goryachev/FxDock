// Copyright (c) 2008-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.options.edit;
import goryachev.common.ui.options.OptionEditor;
import goryachev.common.ui.options.StringOption;
import javax.swing.JComponent;
import javax.swing.JTextField;


public class StringOptionEditor
	extends OptionEditor<String>
{
	public final JTextField editor;


	public StringOptionEditor(StringOption op)
	{
		super(op);
		
		editor = new JTextField();
		editor.setBorder(BORDER_EDITOR);
	}
	

	public String getEditorValue()
	{
		return editor.getText();
	}


	public JComponent getComponent()
	{
		return editor;
	}
	
	
	public void setEditorValue(String value)
	{
		editor.setText(value);
	}
	
	
	public String getSearchString()
	{
		return null;
	}
}