// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.text;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;


public class CEditorSelectAllAction
	extends CEditorAction
{
	public CEditorSelectAllAction()
	{
		super(DefaultEditorKit.selectAllAction);
	}


	public void action()
	{
		JTextComponent c = getTextComponent();
		if(c != null)
		{
			Document doc = c.getDocument();
			c.setCaretPosition(0);
			c.moveCaretPosition(doc.getLength());
		}
	}
}