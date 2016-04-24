// Copyright (c) 2011-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.text.Document;


public class CTextFieldWithPrompt
	extends CTextField
{
	private String prompt;
	
	
	public CTextFieldWithPrompt()
	{
	}
	
	
	public CTextFieldWithPrompt(int columns)
	{
		super(columns);
	}
	
	
	public CTextFieldWithPrompt(String prompt)
	{
		setPrompt(prompt);
	}
	
	
	public CTextFieldWithPrompt(String prompt, int columns)
	{
		super(columns);
		setPrompt(prompt);
	}


	public CTextFieldWithPrompt(Document d, String prompt, int columns)
	{
		super(d, null, columns);
		setPrompt(prompt);
	}
	
	
	public void setPrompt(String s)
	{
		prompt = s;
		repaint();
	}
	
	
	public String getPrompt()
	{
		return prompt;
	}
	
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		if(prompt != null)
		{
			if(getDocument().getLength() == 0)
			{
				Font f = getFont();
				int x = 2;
				int y = 1 + f.getSize();
				Insets m = getInsets();
				if(m != null)
				{
					x += m.left;
					y += m.top;
				}
				
				g.setFont(f);
				g.setColor(Color.lightGray);
				g.drawString(prompt, x, y);
			}
		}
	}


	public String getTextAndClear()
	{
		String s = getText();
		clear();
		return s;
	}
}
