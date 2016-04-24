// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.text;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;
import javax.swing.text.Utilities;


public class CEditorBeginLineAction
	extends CEditorAction
{
	private boolean select;
	
	
	public CEditorBeginLineAction(String name, boolean select)
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
				int begOffs = Utilities.getRowStart(c, offs);
				if(select)
				{
					c.moveCaretPosition(begOffs);
				}
				else
				{
					c.setCaretPosition(begOffs);
				}
			}
			catch(Exception e)
			{
				UIManager.getLookAndFeel().provideErrorFeedback(c);
			}
		}
	}
}