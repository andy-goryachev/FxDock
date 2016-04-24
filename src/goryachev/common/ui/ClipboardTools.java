// Copyright (c) 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.util.Log;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;


public class ClipboardTools
{
	public static Clipboard getClipboard()
	{
		return Toolkit.getDefaultToolkit().getSystemClipboard();
	}


	public static String readString()
	{
		try
		{
			Clipboard c = getClipboard();
			if(c.isDataFlavorAvailable(DataFlavor.stringFlavor))
			{
				return (String)c.getData(DataFlavor.stringFlavor);
			}
		}
		catch(Exception e)
		{
			Log.err(e);
		}
		return null;
	}


	public static void copy(String s)
	{
		StringSelection sel = new StringSelection(s);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel, null);
	}


	public static CAction copyAction(final String s)
	{
		return new CAction(Menus.Copy)
		{
			public void action() throws Exception
			{
				ClipboardTools.copy(s);
			}
		};
	}
}
