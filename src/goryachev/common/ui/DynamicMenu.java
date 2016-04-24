// Copyright (c) 2011-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.ui.theme.AssignMnemonic;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;


public abstract class DynamicMenu
	extends CMenu
	implements MenuListener
{
	/** good place to check id the menu is enabed */
	protected abstract boolean isMenuEnabled();

	/** on menu selection, add current menu items to this menu */ 
	protected abstract void rebuild();

	//

	public DynamicMenu(String name)
	{
		super(name);
		addMenuListener(this);
	}


	public String getText()
	{
		boolean old = super.isEnabled();
		boolean on = isMenuEnabled();
		if(old != on)
		{
			super.setEnabled(on);
		}

		return super.getText();
	}


	public void menuSelected(MenuEvent ev)
	{
		removeAll();
		rebuild();
		AssignMnemonic.assign(this);
	}


	public void menuDeselected(MenuEvent ev)
	{
	}


	public void menuCanceled(MenuEvent ev)
	{
	}
}