// Copyright (c) 2009-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import javax.swing.JLabel;


public class CToolBar
	extends HorizontalPanel
{
	public CToolBar()
	{
		setOpaque(true);
		setBorder(new CBorder(1,0));
		setBackground(Theme.TOOLBAR_BG);
	}
	
	
	public void add(String text)
	{
		JLabel t = new JLabel(text);
		t.setVerticalAlignment(JLabel.CENTER);
		add(t);
	}
}
