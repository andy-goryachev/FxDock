// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.text;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;


public class CEditorCopyAction
	extends CEditorAction
{
	public CEditorCopyAction()
	{
		super(DefaultEditorKit.copyAction);
	}


	public void action()
	{
		JTextComponent c = getTextComponent();
		if(c != null)
		{
//			Caret ca = c.getCaret();
//			int beg = Math.min(ca.getMark(), ca.getDot());
//			int end = Math.max(ca.getMark(), ca.getDot());
//			if(beg == end)
//			{
//				// select word
//				beg = getWordStart(c, beg);
//				end = getWordEnd(c, end);
//				
//				if(beg < 0)
//				{
//					beg = 0;
//				}
//				
//				if(end < 0)
//				{
//					return;
//				}
//				else if(beg == end)
//				{
//					return;
//				}
//			}

			// TODO copy word if no selection
			c.copy();
		}
	}
}