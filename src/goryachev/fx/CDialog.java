// Copyright © 2016-2018 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Window;


/**
 * CDialog is an easier-to-use Dialog.
 * This is a first attempt at this.
 */
public class CDialog
	extends Dialog<Object>
{
	public CDialog(Window owner)
	{
		initOwner(owner);
	}
	
	
	public CDialog()
	{
	}
	
	
	public Object addButton(String text)
	{
		return addButton(text, 	ButtonBar.ButtonData.OTHER);
	}
	
	
	public Object addButton(String text, ButtonBar.ButtonData d)
	{
		ButtonType b = new ButtonType(text, d);
		getDialogPane().getButtonTypes().add(b);
		return b;
	}
}
