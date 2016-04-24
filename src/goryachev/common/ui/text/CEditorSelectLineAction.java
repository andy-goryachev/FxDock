// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.text;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.text.DefaultEditorKit;


public class CEditorSelectLineAction
	extends CEditorAction
{
	private Action start;
	private Action end;


	public CEditorSelectLineAction()
	{
		super(DefaultEditorKit.selectLineAction);

		start = new CEditorBeginLineAction("pigdog", false);
		end = new CEditorEndLineAction("pigdog", true);
	}


	public void actionPerformed(ActionEvent ev)
	{
		start.actionPerformed(ev);
		end.actionPerformed(ev);
	}


	public void action()
	{
	}
}