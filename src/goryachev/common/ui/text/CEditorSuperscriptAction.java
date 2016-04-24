// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.text;
import javax.swing.JEditorPane;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;


public class CEditorSuperscriptAction
	extends CEditorAction
{
	public CEditorSuperscriptAction()
	{
		super("font-superscript");
	}


	public void action()
	{
		JEditorPane ed = getEditor();
		if(ed != null)
		{
			StyledEditorKit k = getStyledEditorKit(ed);
			MutableAttributeSet as = k.getInputAttributes();
			
			boolean on = StyleConstants.isSuperscript(as);
			
			SimpleAttributeSet a = new SimpleAttributeSet();
			StyleConstants.setSuperscript(a, !on);
			setCharacterAttributes(ed, a, false);
		}
	}
}