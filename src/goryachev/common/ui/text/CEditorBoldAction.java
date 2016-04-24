// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.text;
import javax.swing.JEditorPane;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;


public class CEditorBoldAction
	extends CEditorAction
{
	public CEditorBoldAction()
	{
		super("font-bold");
	}
	
	
	public void action()
	{
		// FIX bold word if no selection! (only if chars on both sides are letters or digits)
		// nn|nn      ->  BB|BB
		// nn|.       ->  nn|.
		// <sp>|<sp>  ->  <sp>|<sp>
		// BBB|nnn    ->  nnn|nnn
		JEditorPane ed = getEditor();
		if(ed != null)
		{
			StyledEditorKit k = getStyledEditorKit(ed);
			if(k != null)
			{
				MutableAttributeSet as = k.getInputAttributes();
				
				boolean on = StyleConstants.isBold(as);
				
				SimpleAttributeSet newAttrs = new SimpleAttributeSet();
				StyleConstants.setBold(newAttrs, !on);
				setCharacterAttributes(ed, newAttrs, false);
			}
		}
	}
}