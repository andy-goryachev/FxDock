// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.text;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;
import javax.swing.text.Utilities;


public class CEditorEndLineAction
	extends CEditorAction
{
	private boolean select;
	
	
	public CEditorEndLineAction(String name, boolean select)
	{
		super(name);
		this.select = select;
	}


	public void action()
	{
		JTextComponent c = getTextComponent();
		if(c != null)
		{
			try
			{
				int offs = c.getCaretPosition();
				int endOffs = Utilities.getRowEnd(c, offs);
				if(select)
				{
					c.moveCaretPosition(endOffs);
				}
				else
				{
					c.setCaretPosition(endOffs);
				}
			}
			catch(Exception e)
			{
				UIManager.getLookAndFeel().provideErrorFeedback(c);
			}
		}
	}
}