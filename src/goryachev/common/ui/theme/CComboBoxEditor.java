// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.theme;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicComboBoxEditor;


public class CComboBoxEditor
	extends BasicComboBoxEditor
{
	protected JTextField createEditorComponent()
	{
		JTextField editor = super.createEditorComponent();
		editor.setBorder(AgComboBoxUI.BORDER_EDITOR);
		editor.setOpaque(false);
		return editor;
	}


	public void setItem(Object item)
	{
		super.setItem(item);
		if(editor.hasFocus())
		{
			editor.selectAll();
		}
	}
}