// Copyright (c) 2011-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import javax.swing.JTextField;
import javax.swing.text.Document;


public class CTextField
	extends JTextField
{
	public CTextField()
	{
		init();
	}


	public CTextField(int columns)
	{
		super(columns);
		init();
	}


	public CTextField(String text)
	{
		super(text);
		init();
	}


	public CTextField(String text, int columns)
	{
		super(text, columns);
		init();
	}


	public CTextField(Document d, String text, int columns)
	{
		super(d, text, columns);
		init();
	}


	private void init()
	{
		UI.installDefaultPopupMenu(this);
	}


	public void clear()
	{
		setText(null);
	}
	
	
	public void setText0(String s)
	{
		setText(s);
		setCaretPosition(0);
	}
}
