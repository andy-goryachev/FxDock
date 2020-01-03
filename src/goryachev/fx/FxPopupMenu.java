// Copyright © 2016-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.beans.property.Property;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;


/**
 * Popup menu.
 */
public class FxPopupMenu
	extends ContextMenu
{
	public FxPopupMenu()
	{
		setHideOnEscape(true);
		setAutoHide(true);
		setAutoFix(true);
	}
	
	
	public void add(MenuItem m)
	{
		getItems().add(m);
	}
	
	
	public FxCheckMenuItem checkItem(String text, FxAction a)
	{
		FxCheckMenuItem m = new FxCheckMenuItem(text, a);
		add(m);
		return m;
	}
	
	
	public FxCheckMenuItem checkItem(String text, Property<Boolean> p)
	{
		FxCheckMenuItem m = new FxCheckMenuItem(text, p);
		add(m);
		return m;
	}
	
	
	public FxCheckMenuItem checkItem(String text)
	{
		return checkItem(text, FxAction.DISABLED);
	}
	
	
	public FxMenuItem item(String text, FxAction a)
	{
		FxMenuItem m = new FxMenuItem(text, a);
		add(m);
		return m;
	}
	
	
	public FxMenuItem item(String text, Runnable a)
	{
		FxMenuItem m = new FxMenuItem(text, a);
		add(m);
		return m;
	}
	
	
	public FxMenuItem item(String text)
	{
		FxMenuItem m = new FxMenuItem(text);
		m.setDisable(true);
		add(m);
		return m;
	}
	
	
	public void separator()
	{
		add(new SeparatorMenuItem());
	}

	
	public void clear()
	{
		getItems().clear();
	}
}