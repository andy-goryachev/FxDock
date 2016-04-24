// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.text;
import javax.swing.JEditorPane;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;


public class CEditorSubscriptAction
	extends CEditorAction
{
	public CEditorSubscriptAction()
	{
		super("font-subscript");
	}


	public void action()
	{
		JEditorPane ed = getEditor();
		if(ed != null)
		{
			StyledEditorKit k = getStyledEditorKit(ed);
			MutableAttributeSet as = k.getInputAttributes();
			
			boolean on = StyleConstants.isSubscript(as);
			
			SimpleAttributeSet a = new SimpleAttributeSet();
			StyleConstants.setSubscript(a, !on);
			setCharacterAttributes(ed, a, false);
		}
	}
}