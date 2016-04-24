// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.wizard;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CButton;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.Menus;
import goryachev.common.ui.Panels;
import goryachev.common.ui.UI;
import goryachev.common.ui.icons.CIcons;
import goryachev.common.util.CKit;
import goryachev.common.util.HasPrompts;
import goryachev.common.util.UserException;
import java.awt.Component;
import java.util.Stack;
import javax.swing.Action;
import javax.swing.JComponent;


public class WizardPanel2
	extends CPanel
{
	public final CAction backAction = new CAction() { public void action() { actionBack(); }};
	private Stack<Entry> stack = new Stack();
	protected final WizardTitleBar titleField;

	
	public WizardPanel2()
	{
		titleField = new WizardTitleBar();
	}
	
	
	public void setCardTitle(String s)
	{
		titleField.setText(s);
	}


	protected void actionBack()
	{
		if(stack.size() > 1)
		{
			stack.pop();

			Entry en = stack.pop();
			setCard(en.component, en.title);

			updatePrompts(en.component);
			updatePrompts(UI.getParentWindow(this));
		}
	}


	/** Replaces current card (if any) with the specified component. */
	public void setCard(JComponent c, String title)
	{
		stack.add(new Entry(c, title));
		
		setCenter(c);
		
		titleField.setText(title);
		
		if(title == null)
		{
			remove(titleField);
		}
		else
		{
			setNorth(titleField);
		}
		
		validate();
		repaint();
		
		// TODO set focus to an appropriate component (which?)
		// TODO do we need updatePrompts?
	}
	
	
	public JComponent getCurrentCard()
	{
		Entry en = stack.peek();
		return en.component;
	}
	
	
	protected void updatePrompts(Component c)
	{
		if(c instanceof HasPrompts)
		{
			((HasPrompts)c).updatePrompts();
		}
	}
	
	
	public CPanel setErrorCard(Object messageOrThrowable, boolean allowBackButton, Action closeAction)
	{
		String s;
		if(messageOrThrowable instanceof Throwable)
		{
			if(messageOrThrowable instanceof UserException)
			{
				s = ((UserException)messageOrThrowable).getMessage();
			}
			else
			{
				s = CKit.stackTrace((Throwable)messageOrThrowable);
			}
		}
		else
		{
			s = messageOrThrowable.toString();
		}
		
		
		CPanel p = new CPanel();
		p.setLeading(Panels.iconField(CIcons.Error96));
		p.setCenter(Panels.textPane(s));
		
		if(allowBackButton)
		{
			p.buttonPanel().add(new CButton(Menus.Back, backAction));
		}
		p.buttonPanel().add(new CButton(Menus.Close, closeAction));
		
		setCard(p, Menus.Error);
		return p;
	}
	
	
	//
	
	
	protected static class Entry
	{
		public JComponent component;
		public String title;
		
		public Entry(JComponent component, String title)
		{
			this.component = component;
			this.title = title;
		}
	}
}
