// Copyright (c) 2008-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.ui.dialogs.options.COptionDialog;
import goryachev.common.ui.dialogs.options.OptionTreeNode;
import goryachev.common.ui.icons.CIcons;
import goryachev.common.util.CKit;
import goryachev.common.util.CancelledException;
import goryachev.common.util.Log;
import goryachev.common.util.TXT;
import goryachev.common.util.UserException;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.io.File;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.text.JTextComponent;


/** Standard dialogs - replacement for JOptionPane */
public class Dialogs
{
	public static enum Choice
	{
		CANCEL,
		DISCARD,
		SAVE
	}
	
	//
	
	
	public static final Dimension DEFAULT_SIZE = new Dimension(550, 300);
	
	
	public static void startupError(ImageIcon icon, String title, Object exceptionOrMessage)
	{
		JFrame parent = UI.constructInvisibleFrame(icon, title);
		error(parent, title, exceptionOrMessage);
		Application.exit();
	}
	
	
	public static void size(Window d)
	{
		d.setSize(DEFAULT_SIZE);
		d.setMinimumSize(DEFAULT_SIZE);
	}
	
	
	public static void err(Component parent, Object exceptionOrMessage)
	{
		error(parent, exceptionOrMessage);
	}


	public static void error(Component parent, Object exceptionOrMessage)
	{
		if(exceptionOrMessage instanceof UserException)
		{
			String title = TXT.get("Dialogs.error.sorry", "Sorry");
			error(parent, title, exceptionOrMessage);
		}
		else
		{
			String title = TXT.get("Dialogs.error.unexpected", "Unexpected Error");
			error(parent, title, exceptionOrMessage);
		}
	}
	
	
	public static void error(Component parent, String title, Object exceptionOrMessage)
	{
		CDialog d = new CDialog(parent, "ErrorDialog", true);
		d.setDialogTitle(title);
		d.setCloseOnEscape();
		d.borderless();
		size(d);
		
		String msg;
		JTextComponent t;
		CScrollPane s;
		
		if(exceptionOrMessage instanceof Throwable)
		{
			if(exceptionOrMessage instanceof UserException)
			{
				msg = ((UserException)exceptionOrMessage).getMessage();
				t = Panels.textComponent(msg);
				s = Panels.scroll(t);
			}
			else
			{
				Throwable e = (Throwable)exceptionOrMessage;
				
				if(e instanceof CancelledException)
				{
					// ignore
				}
				else if(e instanceof InterruptedException)
				{
					// ignore
				}
				else
				{
					Log.err(e);
				}
				
				msg = CKit.stackTrace(e);
				t = Panels.textArea(msg, false);
				s = Panels.scroll(t, true);
			}
		}
		else
		{
			msg = String.valueOf(exceptionOrMessage);
			t = Panels.textComponent(msg);
			s = Panels.scroll(t);
		}
		
		t.setBorder(new CBorder(20));
		
		d.panel().setLeading(Panels.iconField(CIcons.Error96));
		d.panel().setCenter(s);

		d.buttonPanel().addButton(Menus.CopyToClipboard, ClipboardTools.copyAction(msg));
		d.buttonPanel().fill();
		d.buttonPanel().addButton(Menus.OK, d.closeDialogAction, true);
		d.setDefaultButton();
		d.open();
	}
	
	
	public static void info(String title, String message)
	{
		info(CFocusMonitor.getLastWindow(), title, message);
	}
	
	
	public static void info(Component parent, String title, String message)
	{
		CDialog d = constructDialog(parent, "InfoDialog", CIcons.Info96, title, message);
		
		d.buttonPanel().addButton(Menus.OK, d.closeDialogAction, true);
		d.setDefaultButton();
		d.open();
	}
	

	public static void warn(Component parent, String title, String message)
	{
		CDialog d = constructDialog(parent, "WarnDialog", CIcons.Warning96, title, message);
		
		d.buttonPanel().addButton(Menus.OK, d.closeDialogAction, true);
		d.setDefaultButton();
		d.open();
	}
	
	
	public static boolean confirm(Component parent, String title, String message, String confirmButton)
	{
		ChoiceDialog<Boolean> d = new ChoiceDialog(parent, title, message);
		d.addButton(Menus.Cancel, null);
		d.addButton(confirmButton, Boolean.TRUE, true);
		return Boolean.TRUE.equals(d.openChoiceDialog());
	}
	
	
	private static CDialog constructDialog(Component parent, String name, Icon icon, String title, String msg)
	{
		CDialog d = new CDialog(parent, name, true);
		d.borderless();
		d.setDialogTitle(title);
		d.setCloseOnEscape();
		size(d);
		
		d.panel().setLeading(Panels.iconField(icon));
		
		JTextComponent t = Panels.textComponent(msg);
		t.setBorder(new CBorder(20));
		d.panel().setCenter(Panels.scroll(t));
		
		return d;
	}

	
	protected static Window lastWindow()
	{
		return CFocusMonitor.getLastWindow();
	}
	

	public static void openOptions(Component parent, String title, OptionTreeNode root, String id, int width, int height)
	{
		COptionDialog d = new COptionDialog(parent, title, root, id);
		d.setSize(width, height);
		d.expandTree();
		d.open();
	}


	public static Choice discardChanges(Component parent)
	{
		ChoiceDialog<Choice> d = new ChoiceDialog
		(
			parent, 
			TXT.get("Dialogs.discard changes.title", "Discard changes?"), 
			TXT.get("Dialogs.discard changes.message", "The changes you've made will be lost.  Do you want to save the changes?")
		);
		
		d.setChoiceDefault(Choice.CANCEL);
		d.addButton(Menus.Cancel, Choice.CANCEL);
		d.addButton(Menus.DiscardChanges, Choice.DISCARD, Theme.DESTRUCTIVE_BUTTON_COLOR);
		d.addButton(Menus.Save, Choice.SAVE, true);
		
		return d.openChoiceDialog();
	}


	/** returns true if it's ok to overwrite */
	public static boolean checkFileExistsOverwrite(Component parent, File f)
	{
		if(f.exists())
		{
			ChoiceDialog<Boolean> d = new ChoiceDialog
			(
				parent,
				TXT.get("Dialogs.file exists.title", "File Exists"),
				TXT.get("Dialogs.file exists.message", "File {0} exists.  Do you want to overwrite it?", f)
			);
			d.addButton(Menus.Overwrite, Boolean.TRUE, Theme.DESTRUCTIVE_BUTTON_COLOR);
			d.addButton(Menus.Cancel, Boolean.FALSE);
			Boolean rv = d.openChoiceDialog();
			return Boolean.TRUE.equals(rv);
		}
		return true;
	}


	public static boolean confirmInterruption(Component parent)
    {
		return confirmInterruption
		(
			parent,
			TXT.get("Dialogs.interrupt.title", "Operation Interrupted"), 
			TXT.get("Dialogs.interrupt.description", "Do you want to interrupt the ongoing operation?")
		);
    }
	
	
	public static boolean confirmInterruption(Component parent, String title, String message)
    {
		ChoiceDialog<Boolean> d = new ChoiceDialog(parent, title, message);
		d.addButton(TXT.get("Dialogs.interrupt.button.interrupt", "Interrupt"), Boolean.TRUE, Theme.DESTRUCTIVE_BUTTON_COLOR);
		d.addButton(TXT.get("Dialogs.interrupt.button.allow", "Allow to Continue"), null, true);
		return Boolean.TRUE.equals(d.openChoiceDialog());
    }
}
