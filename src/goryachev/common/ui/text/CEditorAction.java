// Copyright (c) 2012-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.text;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CFocusMonitor;
import java.awt.Component;
import javax.swing.JEditorPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.Caret;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.EditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.Utilities;


public abstract class CEditorAction
	extends CAction
{
	public CEditorAction(String id)
	{
		super(id);
	}


	protected JTextComponent getFocusedComponent()
	{
		// this method is package private
		// return JTextComponent.getFocusedComponent();
		return CFocusMonitor.getLastTextComponent();
	}


	protected JTextComponent getTextComponent()
	{
		Component c = getSourceComponent();
		if(c instanceof JTextComponent)
		{
			return (JTextComponent)c;
		}

		return getFocusedComponent();
	}


	protected JEditorPane getEditor()
	{
		JTextComponent c = getTextComponent();
		if(c instanceof JEditorPane)
		{
			return (JEditorPane)c;
		}
		return null;
	}

	
	protected StyledEditorKit getStyledEditorKit(JEditorPane ed)
	{
		EditorKit k = ed.getEditorKit();
		if(k instanceof StyledEditorKit)
		{
			return (StyledEditorKit)k;
		}
		return null;
	}
	
	
	protected DefaultStyledDocument getStyledDocument(JTextComponent ed)
	{
		return (DefaultStyledDocument)ed.getDocument();
	}


	protected void setCharacterAttributes(JEditorPane ed, AttributeSet as, boolean replace)
	{
		int start = ed.getSelectionStart();
		int end = ed.getSelectionEnd();
		
		// word selection logic a-la MS Word
		if(start == end)
		{
			int ws = getWordStart(ed, start);
			if(ws >= 0)
			{
				int we = getWordEnd(ed, end);
				if(we >= 0)
				{
					start = ws;
					end = we;
				}
			}
		}
		
		if(start != end)
		{
			getStyledDocument(ed).setCharacterAttributes(start, end - start, as, replace);
		}
		
		StyledEditorKit k = getStyledEditorKit(ed);
		MutableAttributeSet a = k.getInputAttributes();
		if(replace)
		{
			a.removeAttributes(a);
		}
		a.addAttributes(as);
	}


	protected int getWordStart(JTextComponent c, int pos)
	{
		try
		{
			return Utilities.getWordStart(c, pos);
		}
		catch(Exception e)
		{ }
		return -1;
	}
	
	
	protected int getWordEnd(JTextComponent c, int pos)
	{
		try
		{
			return Utilities.getWordEnd(c, pos);
		}
		catch(Exception e)
		{ }
		return -1;
	}
	
	
	protected boolean hasSelection(JTextComponent t)
	{
		Caret c = t.getCaret();
		return c.getDot() != c.getMark();
	}
}
