// Copyright (c) 2015-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import javax.swing.JTextArea;
import javax.swing.text.Document;


/** JTextArea with default popup menu installed */
public class CTextArea 
	extends JTextArea
{
	public CTextArea()
	{
		init();
	}
	

	public CTextArea(String text)
	{
		super(text);
		init();
	}


	public CTextArea(int rows, int cols)
	{
		super(rows, cols);
		init();
	}


	public CTextArea(String text, int rows, int cols)
	{
		super(text, rows, cols);
		init();
	}


	public CTextArea(Document doc)
	{
		super(doc);
		init();
	}


	public CTextArea(Document doc, String text, int rows, int cols)
	{
		super(doc, text, rows, cols);
		init();
	}
	

	private void init()
    {
		UI.installDefaultPopupMenu(this);
		setFont(Theme.plainFont());
    }
	
	
	public void setText0(String text)
	{
		super.setText(text);
		setCaretPosition(0);
	}
}
