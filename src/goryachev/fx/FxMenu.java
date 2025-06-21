// Copyright © 2016-2025 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.beans.property.Property;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;


/**
 * FX Menu.
 */
public class FxMenu
	extends Menu
{
	public FxMenu(String text)
	{
		super(text);
	}
	
	
	public FxMenu(String text, FxAction a)
	{
		super(text);
		a.attach(this);
	}
	
	
	public FxMenu(String text, Runnable r)
	{
		super(text);
		new FxAction(r).attach(this);
	}
	
	
	public SeparatorMenuItem separator()
	{
		SeparatorMenuItem m = new SeparatorMenuItem();
		getItems().add(m);
		return m;
	}
	
	
	public FxMenuItem item(String text, FxAction a)
	{
		FxMenuItem m = new FxMenuItem(text, a);
		getItems().add(m);
		return m;
	}
	
	
	public FxMenuItem item(String text, Runnable r)
	{
		FxMenuItem m = new FxMenuItem(text, r);
		getItems().add(m);
		return m;
	}
	
	
	public FxMenu menu(String text)
	{
		FxMenu m = new FxMenu(text);
		getItems().add(m);
		return m;
	}
	
	
	public FxCheckMenuItem item(String text, Property<Boolean> prop)
	{
		FxCheckMenuItem m = new FxCheckMenuItem(text, prop);
		getItems().add(m);
		return m;
	}
	
	
	/** adds a disabled menu item */
	public FxMenuItem item(String text)
	{
		FxMenuItem m = new FxMenuItem(text);
		m.setDisable(true);
		return add(m);
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
	
	
	/** adds a disabled check menu item */
	public FxCheckMenuItem checkItem(String text)
	{
		return checkItem(text, FxAction.DISABLED);
	}
	
	
	public <M extends MenuItem> M add(M item)
	{
		getItems().add(item);
		return item;
	}
	
	
	/** remove all menu items */
	public void clear()
	{
		getItems().clear();
	}
}
